package com.github.annsofip.orderbookservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
@Builder
public class OrderSummary {
    private Double averagePrice;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private Long numberOfOrders;


    public OrderSummary(Double averagePrice, BigDecimal maxPrice, BigDecimal minPrice, Long numberOfOrders) {
        this.averagePrice = averagePrice;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.numberOfOrders = numberOfOrders;
    }
}
