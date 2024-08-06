package com.backend.library.backend.services.implementations;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.backend.library.backend.handlers.exceptions.UnexpectedException;
import com.backend.library.backend.persistence.entities.Cart;
import com.backend.library.backend.persistence.entities.OrderEntity;
import com.backend.library.backend.persistence.entities.OrderItem;
import com.backend.library.backend.persistence.repositories.OrderEntityRepository;
import com.backend.library.backend.persistence.repositories.OrderItemRepository;
import com.backend.library.backend.services.interfaces.IOrderEntityService;
import com.paypal.orders.Order;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEntityServiceImpl implements IOrderEntityService {

    private final OrderEntityRepository orderEntityRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public void saveOrder(Order order, Cart cart) {
        try {

            OrderEntity orderEntity = OrderEntity.builder()
                    .paypalOrderId(order.id())
                    .paypalOrderStatus(order.status())
                    .cart(cart)
                    .clientId(cart.getUserId())
                    .build();
            orderEntity.setAmount(cart.getBooks().stream()
                    .mapToDouble(item -> item.getBook().getPrice() * item.getQuantity())
                    .sum());
            List<OrderItem> orderItems = cart.getBooks().stream()
                    .map(cartItem -> OrderItem.builder()
                            .book(cartItem.getBook())
                            .quantity(cartItem.getQuantity())
                            .price(cartItem.getBook().getPrice())
                            .order(orderEntity)
                            .build())
                    .collect(Collectors.toList());

            orderEntity.setOrderItems(orderItems);
            orderEntityRepository.save(orderEntity);
            orderItemRepository.saveAll(orderItems);

        } catch (Exception e) {
            log.error("ERROR: Unexpected exception while saving order: " + e.getMessage());
            throw new RuntimeException("ERROR: Unexpected exception while saving order: " + e.getMessage());
        }

        // try {
        // OrderEntity entity = orderEntityRepositoty.findByPaypalOrderId(order.id());
        // // El usuario debe completar el order pendiente antes de crear un nuevo.
        // // si el usuario tiene un order incompleto y si ententa crear un nuevo, si
        // borra
        // // el anterior.
        // OrderEntity entityByCartId =
        // orderEntityRepositoty.findByCartId(cart.getId());

        // if (entity == null && entityByCartId != null) {
        // // Se borra el orden anterior
        // orderEntityRepositoty.delete(entityByCartId);
        // }

        // OrderEntity orderEntity = OrderEntity.builder()
        // .paypalOrderId(order.id())
        // .paypalOrderStatus(order.status())
        // .cart(cart)
        // .build();
        // orderEntityRepositoty.save(orderEntity);

        // } catch (Exception e) {
        // log.error("ERROR: Unexpected exception while saving cart: " +
        // e.getMessage());
        // throw new UnexpectedException("ERROR: Unexpected exception while saving
        // order: " + e.getMessage());
        // }

    }

    @Override
    public OrderEntity getOrder(String paypalOrderId) {
        try {
            OrderEntity orderEntity = orderEntityRepository.findByPaypalOrderId(paypalOrderId);
            if (orderEntity == null) {
                throw new NotFoundException("No such order entity find for paypal with id " + paypalOrderId);
            }
            return orderEntity;
        } catch (Exception e) {
            log.error("ERROR: Unexpected exception while saving cart: " + e.getMessage());
            throw new UnexpectedException("ERROR: Unexpected exception while saving order: " + e.getMessage());
        }
    }

    @Override
    public OrderEntity updatOrder(Order order) {
        OrderEntity existingOrderEntity = getOrder(order.id());
        existingOrderEntity.setPaypalOrderStatus(order.status());
        return orderEntityRepository.save(existingOrderEntity);
    }

    @Override
    public void deleteOrder(String orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteOrder'");
    }

}
