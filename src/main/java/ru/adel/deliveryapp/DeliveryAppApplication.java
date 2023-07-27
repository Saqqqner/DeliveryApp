package ru.adel.deliveryapp;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.adel.deliveryapp.dto.OrderDTO;
import ru.adel.deliveryapp.dto.OrderItemDTO;
import ru.adel.deliveryapp.models.Order;
import ru.adel.deliveryapp.models.OrderItem;

@SpringBootApplication
public class DeliveryAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryAppApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        modelMapper.createTypeMap(Order.class, OrderDTO.class)
                .addMapping(src -> src.getCustomer().getId(), OrderDTO::setCustomerId)
                .addMapping(Order::getOrderItems, OrderDTO::setOrderItems);

        modelMapper.createTypeMap(OrderItem.class, OrderItemDTO.class)
                .addMapping(OrderItem::getId, OrderItemDTO::setId)
                .addMapping(orderItem -> orderItem.getOrder().getId(), OrderItemDTO::setOrderId)
                .addMapping(orderItem -> orderItem.getProduct().getId(), OrderItemDTO::setProductId)
                .addMapping(OrderItem::getQuantity, OrderItemDTO::setQuantity);

        return modelMapper;
    }
}
