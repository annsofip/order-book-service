package com.github.annsofip.orderbookservice.port.incoming.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.annsofip.orderbookservice.domain.model.OrderSide;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class OrderSummaryDTO {
    @JsonProperty(value = "ticker")
    private String ticker;
    @JsonProperty(value = "orderSide")
    private OrderSide orderSide;
    @JsonProperty(value = "averagePrice")
    private BigDecimal averagePrice;
    @JsonProperty(value = "maxPrice")
    private BigDecimal maxPrice;
    @JsonProperty(value = "minPrice")
    private BigDecimal minPrice;
    @JsonProperty(value = "numberOfOrders")
    private Long numberOfOrders;
    @JsonProperty(value = "date")
    private LocalDate date;

}
