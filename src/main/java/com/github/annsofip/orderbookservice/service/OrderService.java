package com.github.annsofip.orderbookservice.service;

import com.github.annsofip.orderbookservice.model.OrderSide;
import com.github.annsofip.orderbookservice.model.OrderMapper;
import com.github.annsofip.orderbookservice.api.OrderRequestDTO;
import com.github.annsofip.orderbookservice.api.OrderResponseDTO;
import com.github.annsofip.orderbookservice.api.OrderSummaryDTO;
import com.github.annsofip.orderbookservice.repositories.OrderRepository;
import com.github.annsofip.orderbookservice.repositories.entities.Order;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static java.math.RoundingMode.HALF_UP;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderResponseDTO createOrder(@NotNull OrderRequestDTO orderDTO) {
        Order order = orderMapper.createOrderDtoToOrder(orderDTO);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.orderToOrderDto(savedOrder);
    }

    public Optional<OrderResponseDTO> getOrder(@NotNull UUID id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(orderMapper::orderToOrderDto);
    }

    public OrderSummaryDTO getSummary(@NotNull String ticker, @NotNull OrderSide orderSide, @NotNull LocalDate date) {
        return orderRepository.findSummaryByTickerAndOrderSideAndDate(ticker, orderSide, date)
                .map(order -> OrderSummaryDTO.builder()
                        .ticker(ticker)
                        .orderSide(orderSide)
                        .averagePrice(BigDecimal.valueOf(order.getAveragePrice()).setScale(2, HALF_UP))
                        .minPrice(order.getMinPrice().setScale(2, HALF_UP))
                        .maxPrice(order.getMaxPrice().setScale(2, HALF_UP))
                        .numberOfOrders(order.getNumberOfOrders())
                        .date(date)
                        .build())
                .orElse(OrderSummaryDTO.builder()
                        .ticker(ticker)
                        .orderSide(orderSide)
                        .averagePrice(BigDecimal.ZERO)
                        .maxPrice(BigDecimal.ZERO)
                        .minPrice(BigDecimal.ZERO)
                        .numberOfOrders(0L)
                        .date(date)
                        .build());

    }
}

