package ru.adel.deliveryapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDTO {
    @JsonProperty("customerId")
    private Long customerId;

    @JsonProperty("orderItems")
    private List<OrderItemDTO> orderItems;
}
