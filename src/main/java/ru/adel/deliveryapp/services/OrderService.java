package ru.adel.deliveryapp.services;

import ru.adel.deliveryapp.dto.OrderDTO;
import ru.adel.deliveryapp.models.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();

    Order getOrderById(Long id);

    Order createOrder(OrderDTO orderDTO);

    void deleteOrder(Long id);
}
