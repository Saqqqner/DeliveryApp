package ru.adel.deliveryapp.util.valid;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.adel.deliveryapp.dto.OrderDTO;
import ru.adel.deliveryapp.dto.OrderItemDTO;
import ru.adel.deliveryapp.models.Product;
import ru.adel.deliveryapp.services.impl.ProductServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderDTOValidator implements Validator {
    private final ProductServiceImpl productServiceImpl;

    public OrderDTOValidator(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return OrderDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OrderDTO orderDTO = (OrderDTO) target;
        List<OrderItemDTO> orderItems = orderDTO.getOrderItems();
        Set<Long> productIds = orderItems.stream().map(OrderItemDTO::getProductId).collect(Collectors.toSet());
        Map<Long, Product> productMap = productServiceImpl.getProductsByIds(productIds)
                .stream().collect(Collectors.toMap(Product::getId, product -> product));
        for (OrderItemDTO orderItemDTO : orderItems) {
            Long productId = orderItemDTO.getProductId();
            Long quantity = orderItemDTO.getQuantity();
            if (quantity == null || quantity <= 0) {
                errors.rejectValue("orderItems[" + orderItems.indexOf(orderItemDTO) + "].quantity", "positive", "Количество должно быть больше нуля");
            } else {
                Product product = productMap.get(productId);
                if (product == null) {
                    errors.rejectValue("orderItems[" + orderItems.indexOf(orderItemDTO) + "].productId", "invalid", "Товар с id " + productId + " не найден");
                } else if (product.getStock() < quantity) {
                    errors.rejectValue("orderItems[" + orderItems.indexOf(orderItemDTO) + "].quantity", "invalid", "Недостаточно товара с id " + productId + " на складе");
                }
            }
        }
    }
}
