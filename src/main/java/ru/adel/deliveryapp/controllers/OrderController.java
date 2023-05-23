package ru.adel.deliveryapp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.adel.deliveryapp.dto.OrderDTO;
import ru.adel.deliveryapp.models.Customer;
import ru.adel.deliveryapp.models.Order;
import ru.adel.deliveryapp.security.CustomerDetails;
import ru.adel.deliveryapp.services.impl.OrderServiceImpl;
import ru.adel.deliveryapp.services.impl.ProductServiceImpl;
import ru.adel.deliveryapp.util.valid.OrderDTOValidator;

import java.util.List;
import java.util.stream.Collectors;

import static ru.adel.deliveryapp.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderServiceImpl orderServiceImpl;
    private final ModelMapper modelMapper;
    private final OrderDTOValidator orderDTOValidator;
    private final ProductServiceImpl productServiceImpl;

    @Autowired
    public OrderController(OrderServiceImpl orderServiceImpl, ModelMapper modelMapper, OrderDTOValidator orderDTOValidator, ProductServiceImpl productServiceImpl) {
        this.orderServiceImpl = orderServiceImpl;
        this.modelMapper = modelMapper;
        this.orderDTOValidator = orderDTOValidator;
        this.productServiceImpl = productServiceImpl;
    }

//    Получение всех заказов
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders() {
        List<Order> orders = orderServiceImpl.getAllOrders();
        if (orders.isEmpty())
            return ResponseEntity.notFound().build();
        List<OrderDTO> orderDTOS = orders.stream()
                .map(order -> modelMapper.map(order,OrderDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOS);

    }

//    Получение заказа по id
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        Order order = orderServiceImpl.getOrderById(orderId);
        if (order == null)
            return ResponseEntity.notFound().build();
        OrderDTO orderDTO = modelMapper.map(order,OrderDTO.class);
        return ResponseEntity.ok(orderDTO);
    }

//    Создание заказа
@PostMapping
public ResponseEntity<OrderDTO> createOrder(Authentication authentication, @RequestBody @Validated OrderDTO orderDTO, BindingResult bindingResult) {
    orderDTOValidator.validate(orderDTO, bindingResult);
    if (bindingResult.hasErrors())
        returnErrorsToClient(bindingResult);


    CustomerDetails customerDetails = (CustomerDetails) authentication.getPrincipal();
    Customer authenticatedCustomer = customerDetails.getCustomer();
    Long customerId = authenticatedCustomer.getId();

    // Проверка, что заказ делает авторизованный пользователь

    // Остальной код создания заказа
    orderDTO.setCustomerId(customerId);
    Order createdOrder = orderServiceImpl.createOrder(orderDTO);
    // Обновление количества продуктов на складе через ProductService
    productServiceImpl.updateProductStock(createdOrder.getOrderItems());
    OrderDTO createdOrderDTO = modelMapper.map(createdOrder, OrderDTO.class);
    return new ResponseEntity<>(createdOrderDTO, HttpStatus.CREATED);
}


//    Удаление заказа
    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        orderServiceImpl.deleteOrder(orderId);
    }
}
