package com.backend.library.backend.services.implementations;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.backend.library.backend.dto.carts.CartDTO;
import com.backend.library.backend.handlers.exceptions.UnexpectedException;
import com.backend.library.backend.mappers.interfaces.CartMapper;
import com.backend.library.backend.persistence.entities.Book;
import com.backend.library.backend.persistence.entities.Cart;
import com.backend.library.backend.persistence.entities.CartBook;
import com.backend.library.backend.persistence.entities.OrderEntity;
import com.backend.library.backend.persistence.entities.Sale;
import com.backend.library.backend.persistence.repositories.BookRepository;
import com.backend.library.backend.persistence.repositories.SalesRepository;
import com.backend.library.backend.services.interfaces.ICartService;
import com.backend.library.backend.services.interfaces.IOrderEntityService;
import com.backend.library.backend.services.interfaces.IWalletService;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.LinkDescription;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.OrdersGetRequest;
import com.paypal.orders.PurchaseUnitRequest;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PayPalService {

    private final PayPalHttpClient payPalClient;
    private final ICartService cartService;
    private final IOrderEntityService orderService;
    private final BookRepository bookRepository;
    private final CartMapper cartMapper;
    private final SalesRepository salesRepository;
    private final IWalletService walletService;
    private final EmailServiceImpl emailService;;

    @Value("${paypal.success.url}")
    private String returnUrl;

    @Value("${paypal.cancel.url}")
    private String cancelUrl;

    @Value("${app.name}")
    private String appName;

    // Hacer los cambios para añadir la dirección de envio.
    @Transactional
    public Map<String, String> createOrder(Long cartId, String currencyCode) {
        try {
            // Obtener el carrito de compra
            Cart cart = cartService.getCartById(cartId);

            OrdersCreateRequest request = new OrdersCreateRequest();
            request.requestBody(buildRequestBody(cart, currencyCode));

            Order order = payPalClient.execute(request).result();

            Map<String, String> response = new HashMap<>();
            for (LinkDescription links : order.links()) {
                if (links.rel().equals("approve")) {
                    response.put("redirect_url", links.href());
                }
            }
            response.put("orderId", order.id());
            if (order.status().toLowerCase().equals("created")) {
                orderService.saveOrder(order, cart);
            }
            return response;
        } catch (Exception e) {
            log.error("Unexpected exception while creating order ", e);
            throw new UnexpectedException("Unexpected exception while creating order " + e.getMessage());
        }
    }

    @Transactional
    public void completeOrder(String token) {
        OrdersGetRequest request = new OrdersGetRequest(token);

        try {
            Order order = payPalClient.execute(request).result();
            // 1. VErficar si el order si ha completado
            if ("COMPLETED".equals(order.status())) {
                // 2. Actualizar el status OrderEntity
                OrderEntity existingOrderEntity = orderService.updatOrder(order);

                Map<Object, List<CartBook>> booksBySeller = existingOrderEntity.getCart().getBooks().stream()
                        .collect(Collectors.groupingBy(cartItem -> cartItem.getBook().getSellerId()));

                // actualizar el stock.
                existingOrderEntity.getCart().getBooks().stream().forEach(b -> {
                    Book book = bookRepository.findByIsbn(b.getBook().getIsbn());
                    if (book != null) {
                        book.setStock(book.getStock() - b.getQuantity());
                        bookRepository.save(book);

                        // añadir la compra al hostorial de compras
                        Sale purchase = Sale.builder()
                                .book(book)
                                .quantity(b.getQuantity())
                                .saleDate(new Date())
                                .salerId(book.getSellerId())
                                .clientId(existingOrderEntity.getCart().getUserId())
                                .build();
                        salesRepository.save(purchase);
                        // Añadir el importe de venta a la cartera del vendedor
                        walletService.addAmountToWallet(purchase.getQuantity() * purchase.getBook().getPrice(),
                                purchase.getSalerId());
                    }
                });

                booksBySeller.forEach((sellerId, cartItems) -> {
                    cartItems.forEach(cartItem -> {
                        try {
                            // Reemplazar "seller.email@example.com" con el email real del vendedor
                            emailService.sendEmail("khalifa.boulbayem.external@eviden.com", "Asunto",
                                    "Esto es un cuerpo del mensaje para el vendedor con ID: " + sellerId);
                        } catch (UnsupportedEncodingException | MessagingException e) {
                            log.error("Error sending email to seller with ID: " + sellerId);
                            e.printStackTrace();
                        }
                    });
                    // Notificar al vendedor por email (una vez por vendedor)

                });

                // Vaciar el carrito
                cartService.clearCart();

            }
        } catch (Exception e) {
            log.error("Unexpected exception while updating database ", e);
            throw new UnexpectedException("Unexpected exception while updating database " + e.getMessage());
        }
    }

    private OrderRequest buildRequestBody(Cart cart, String currncyCode) {
        CartDTO cartDto = cartMapper.cartToCartDTO(cart);
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext().brandName(appName)
                .landingPage("BILLING").cancelUrl(cancelUrl).returnUrl(returnUrl);

        AmountWithBreakdown amount = new AmountWithBreakdown().currencyCode(currncyCode)
                .value(String.format(Locale.forLanguageTag(currncyCode), "%.2f",
                        cartDto.amount() + cartDto.shipping()));

        orderRequest.applicationContext(applicationContext);
        orderRequest.purchaseUnits(Collections.singletonList(new PurchaseUnitRequest()
                .amountWithBreakdown(amount)));
        return orderRequest;
    }
}
