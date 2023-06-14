package com.github.annsofip.orderbookservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.annsofip.orderbookservice.domain.model.OrderSide;
import com.github.annsofip.orderbookservice.port.incoming.rest.dto.OrderRequestDTO;
import com.github.annsofip.orderbookservice.port.incoming.rest.dto.OrderResponseDTO;
import com.github.annsofip.orderbookservice.port.outgoing.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();
    }


    @Test
    public void shouldReturn400WhenRequestBodyIsMissing() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn404WhenOrderDoesNotExist() throws Exception {
        UUID nonExistingOrderId = UUID.randomUUID();
        mockMvc.perform(get("/orders/{id}", nonExistingOrderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateAndReturnOrder() throws Exception {
        OrderRequestDTO orderDTO = new OrderRequestDTO("GME", OrderSide.BUY, 100, BigDecimal.valueOf(300), "USD");

        String responseBody = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        OrderResponseDTO orderResponseDTO = objectMapper.readValue(responseBody, OrderResponseDTO.class);

        mockMvc.perform(get("/orders/" + orderResponseDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(orderResponseDTO.getId().toString())))
                .andExpect(jsonPath("$.ticker", is(orderDTO.getTicker())))
                .andExpect(jsonPath("$.orderSide", is(orderDTO.getOrderSide().toString())))
                .andExpect(jsonPath("$.volume", is(orderDTO.getVolume())))
                .andExpect(jsonPath("$.price", is(orderDTO.getPrice().doubleValue())))
                .andExpect(jsonPath("$.currency", is(orderDTO.getCurrency())));

    }

    @Test
    public void shouldGetOrder() throws Exception {
        OrderRequestDTO orderDTO = new OrderRequestDTO("GME", OrderSide.BUY, 100, BigDecimal.valueOf(300), "USD");

        // Create Order
        String responseBody = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andReturn().getResponse().getContentAsString();

        OrderResponseDTO createdOrder = objectMapper.readValue(responseBody, OrderResponseDTO.class);

        // Get Order
        mockMvc.perform(get("/orders/" + createdOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(createdOrder.getId().toString())))
                .andExpect(jsonPath("$.ticker", is(createdOrder.getTicker())))
                .andExpect(jsonPath("$.orderSide", is(createdOrder.getOrderSide().toString())))
                .andExpect(jsonPath("$.volume", is(createdOrder.getVolume())))
                .andExpect(jsonPath("$.price", is(createdOrder.getPrice().doubleValue())))
                .andExpect(jsonPath("$.currency", is(createdOrder.getCurrency())));
    }

    @Test
    public void shouldGetOrderSummary() throws Exception {
        String ticker = "GME";
        OrderSide orderSide = OrderSide.BUY;
        LocalDate date = LocalDate.now();
        BigDecimal orderPrice = BigDecimal.valueOf(300);

        OrderRequestDTO orderDTO = new OrderRequestDTO(ticker, orderSide, 100, orderPrice, "USD");
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andReturn().getResponse().getContentAsString();

        // Get Summary for one order
        mockMvc.perform(get("/orders/summary")
                        .param("ticker", ticker)
                        .param("orderSide", orderSide.toString())
                        .param("date", date.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ticker", is(ticker)))
                .andExpect(jsonPath("$.orderSide", is(orderSide.toString())))
                .andExpect(jsonPath("$.averagePrice", is(orderPrice.doubleValue())))
                .andExpect(jsonPath("$.maxPrice", is(orderPrice.doubleValue())))
                .andExpect(jsonPath("$.minPrice", is(orderPrice.doubleValue())))
                .andExpect(jsonPath("$.numberOfOrders", is(1)))
                .andExpect(jsonPath("$.date", is(date.toString())));
    }

    @Test
    public void shouldReturnEmptySummaryWhenSummaryForNonExistingTickerIsRequested() throws Exception {
        String nonExistingTicker = "NONEXIST";
        OrderSide orderSide = OrderSide.BUY;
        LocalDate date = LocalDate.now();

        mockMvc.perform(get("/orders/summary")
                        .param("ticker", nonExistingTicker)
                        .param("orderSide", orderSide.toString())
                        .param("date", date.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ticker", is(nonExistingTicker)))
                .andExpect(jsonPath("$.orderSide", is(orderSide.toString())))
                .andExpect(jsonPath("$.averagePrice", is(0)))
                .andExpect(jsonPath("$.maxPrice", is(0)))
                .andExpect(jsonPath("$.minPrice", is(0)))
                .andExpect(jsonPath("$.numberOfOrders", is(0)));
    }


}
