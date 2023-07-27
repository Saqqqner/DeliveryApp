package ru.adel.deliveryapp.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.adel.deliveryapp.dto.OrderDTO;
import ru.adel.deliveryapp.dto.OrderItemDTO;
import ru.adel.deliveryapp.models.Customer;
import ru.adel.deliveryapp.models.Order;
import ru.adel.deliveryapp.models.OrderItem;
import ru.adel.deliveryapp.models.Product;
import ru.adel.deliveryapp.repositories.OrderItemRepository;
import ru.adel.deliveryapp.repositories.OrderRepository;
import ru.adel.deliveryapp.services.OrderService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;


    private final CustomerServiceImpl customerServiceImpl;


    private final ProductServiceImpl productServiceImpl;
    private final OrderItemRepository orderItemRepository;

    // Получение всех заказов
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    //    Получение заказа по Id
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }

    //    Создание заказа
    public Order createOrder(OrderDTO orderDTO) {
        Long customerId = orderDTO.getCustomerId();
        Customer customer = customerServiceImpl.getCustomerById(customerId);
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDate.now());
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDTO orderItemDTO : orderDTO.getOrderItems()) {
            Product product = productServiceImpl.getProductById(orderItemDTO.getProductId());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemDTO.getQuantity());
            orderItem.setPrice();
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        order = orderRepository.save(order);  // Сохранить заказ, чтобы получить его идентификатор
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);  // Установить правильное значение для поля "order"
        }
        List<OrderItem> savedOrderItems = orderItemRepository.saveAll(orderItems);  // Сохраните все OrderItem пакетно
        order.setOrderItems(savedOrderItems);
        return order;
    }

    //    Удаление заказа
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }


}
