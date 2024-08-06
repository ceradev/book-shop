package com.backend.library.backend.services.interfaces;

import com.backend.library.backend.persistence.entities.Cart;
import com.backend.library.backend.persistence.entities.OrderEntity;
import com.paypal.orders.Order;

public interface IOrderEntityService {
    void saveOrder(Order order, Cart cart);

    OrderEntity getOrder(String paypalOrderId);

    OrderEntity updatOrder(Order order);

    void deleteOrder(String orderId);
}
