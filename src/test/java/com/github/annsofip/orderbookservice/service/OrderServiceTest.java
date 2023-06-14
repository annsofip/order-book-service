package com.github.annsofip.orderbookservice.service;

import com.github.annsofip.orderbookservice.domain.model.OrderSide;
import com.github.annsofip.orderbookservice.domain.model.OrderSummary;
import com.github.annsofip.orderbookservice.port.incoming.OrderMapper;
import com.github.annsofip.orderbookservice.port.incoming.rest.dto.OrderRequestDTO;
import com.github.annsofip.orderbookservice.port.incoming.rest.dto.OrderResponseDTO;
import com.github.annsofip.orderbookservice.port.incoming.rest.dto.OrderSummaryDTO;
import com.github.annsofip.orderbookservice.port.outgoing.repository.OrderRepository;
import com.github.annsofip.orderbookservice.port.outgoing.repository.entity.Order;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
public class OrderServiceTest {

    private OrderService orderService;
    private OrderRepository orderRepository;
    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderMapper = mock(OrderMapper.class);
        orderService = new OrderService(orderRepository, orderMapper);
    }

    @Test
    void createOrder_shouldReturnOrderResponseDTOWhenOrderIsCreated() {
        OrderRequestDTO orderRequestDTO = createSampleOrderRequestDTO();
        Order order = createSampleOrder();
        Order savedOrder = createSampleOrder();
        OrderResponseDTO expectedResponseDTO = createSampleOrderResponseDTO();

        when(orderMapper.createOrderDtoToOrder(orderRequestDTO)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(savedOrder);
        when(orderMapper.orderToOrderDto(savedOrder)).thenReturn(expectedResponseDTO);

        OrderResponseDTO responseDTO = orderService.createOrder(orderRequestDTO);

        assertNotNull(responseDTO);
        assertEquals(expectedResponseDTO.getId(), responseDTO.getId());
        assertEquals(expectedResponseDTO.getTicker(), responseDTO.getTicker());
        assertEquals(expectedResponseDTO.getOrderSide(), responseDTO.getOrderSide());
        assertEquals(expectedResponseDTO.getVolume(), responseDTO.getVolume());
        assertEquals(expectedResponseDTO.getPrice(), responseDTO.getPrice());
        assertEquals(expectedResponseDTO.getCurrency(), responseDTO.getCurrency());
        assertEquals(expectedResponseDTO.getDate(), responseDTO.getDate());

        verify(orderMapper, times(1)).createOrderDtoToOrder(orderRequestDTO);
        verify(orderRepository, times(1)).save(order);
        verify(orderMapper, times(1)).orderToOrderDto(savedOrder);
    }

    @Test
    void getOrder_shouldReturnOptionalOrderResponseDTOWhenOrderExists() {
        UUID orderId = UUID.randomUUID();
        Order order = createSampleOrder();
        OrderResponseDTO expectedResponseDTO = createSampleOrderResponseDTO();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderDto(order)).thenReturn(expectedResponseDTO);

        Optional<OrderResponseDTO> responseDTO = orderService.getOrder(orderId);

        assertTrue(responseDTO.isPresent());
        assertEquals(expectedResponseDTO.getId(), responseDTO.get().getId());
        assertEquals(expectedResponseDTO.getTicker(), responseDTO.get().getTicker());
        assertEquals(expectedResponseDTO.getOrderSide(), responseDTO.get().getOrderSide());
        assertEquals(expectedResponseDTO.getVolume(), responseDTO.get().getVolume());
        assertEquals(expectedResponseDTO.getPrice(), responseDTO.get().getPrice());
        assertEquals(expectedResponseDTO.getCurrency(), responseDTO.get().getCurrency());
        assertEquals(expectedResponseDTO.getDate(), responseDTO.get().getDate());

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderMapper, times(1)).orderToOrderDto(order);
    }

    @Test
    void getOrder_shouldReturnEmptyOptionalWhenOrderDoesNotExist() {
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Optional<OrderResponseDTO> responseDTO = orderService.getOrder(orderId);

        assertFalse(responseDTO.isPresent());

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderMapper, never()).orderToOrderDto(any());
    }

    @Test
    void getSummary_shouldReturnOrderSummaryDTOWhenSummaryExists() {
        String ticker = "SAVE";
        OrderSide orderSide = OrderSide.BUY;
        LocalDate date = LocalDate.now();
        OrderSummary expectedSummary = OrderSummary.builder()
                .averagePrice(Double.valueOf(12.99))
                .minPrice(BigDecimal.valueOf(10.99))
                .maxPrice(BigDecimal.valueOf(15.99))
                .numberOfOrders(5L)
                .build();

        when(orderRepository.findSummaryByTickerAndOrderSideAndDate(ticker, orderSide, date))
                .thenReturn(Optional.of(expectedSummary));

        OrderSummaryDTO summaryDTO = orderService.getSummary(ticker, orderSide, date);

        assertNotNull(summaryDTO);
        assertEquals(ticker, summaryDTO.getTicker());
        assertEquals(orderSide, summaryDTO.getOrderSide());
        assertEquals(expectedSummary.getAveragePrice().doubleValue(), summaryDTO.getAveragePrice().doubleValue());
        assertEquals(expectedSummary.getMinPrice(), summaryDTO.getMinPrice());
        assertEquals(expectedSummary.getMaxPrice(), summaryDTO.getMaxPrice());
        assertEquals(expectedSummary.getNumberOfOrders(), summaryDTO.getNumberOfOrders());
        assertEquals(date, summaryDTO.getDate());

        verify(orderRepository, times(1)).findSummaryByTickerAndOrderSideAndDate(ticker, orderSide, date);
    }

    @Test
    void getSummary_shouldReturnEmptySummaryDTOWhenSummaryDoesNotExist() {
        String ticker = "SAVE";
        OrderSide orderSide = OrderSide.BUY;
        LocalDate date = LocalDate.now();

        when(orderRepository.findSummaryByTickerAndOrderSideAndDate(ticker, orderSide, date))
                .thenReturn(Optional.empty());

        OrderSummaryDTO summaryDTO = orderService.getSummary(ticker, orderSide, date);

        assertNotNull(summaryDTO);
        assertEquals(ticker, summaryDTO.getTicker());
        assertEquals(orderSide, summaryDTO.getOrderSide());
        assertEquals(BigDecimal.ZERO, summaryDTO.getAveragePrice());
        assertEquals(BigDecimal.ZERO, summaryDTO.getMinPrice());
        assertEquals(BigDecimal.ZERO, summaryDTO.getMaxPrice());
        assertEquals(0L, summaryDTO.getNumberOfOrders());
        assertEquals(date, summaryDTO.getDate());

        verify(orderRepository, times(1)).findSummaryByTickerAndOrderSideAndDate(ticker, orderSide, date);
    }

    private OrderRequestDTO createSampleOrderRequestDTO() {
        return OrderRequestDTO.builder()
                .ticker("SAVE")
                .orderSide(OrderSide.BUY)
                .volume(100)
                .price(BigDecimal.valueOf(12.99))
                .currency("SEK")
                .build();
    }

    private Order createSampleOrder() {
        return Order.builder()
                .id(UUID.randomUUID())
                .ticker("SAVE")
                .orderSide(OrderSide.BUY)
                .volume(100)
                .price(BigDecimal.valueOf(12.99))
                .currency("SEK")
                .date(LocalDateTime.now())
                .build();
    }

    private OrderResponseDTO createSampleOrderResponseDTO() {
        return OrderResponseDTO.builder()
                .id(UUID.randomUUID())
                .ticker("SAVE")
                .orderSide(OrderSide.BUY)
                .volume(100)
                .price(BigDecimal.valueOf(12.99))
                .currency("SEK")
                .date(LocalDate.now())
                .build();
    }
}
