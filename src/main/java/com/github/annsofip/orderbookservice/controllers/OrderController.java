package com.github.annsofip.orderbookservice.controllers;

import com.github.annsofip.orderbookservice.api.OrderRequestDTO;
import com.github.annsofip.orderbookservice.api.OrderResponseDTO;
import com.github.annsofip.orderbookservice.api.OrderSummaryDTO;
import com.github.annsofip.orderbookservice.model.OrderSide;
import com.github.annsofip.orderbookservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    @Operation(summary = "Create an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO order) {
        log.info("operation=createOrder, action=start, order={}", order);

        OrderResponseDTO savedOrder = orderService.createOrder(order);

        log.info("operation=createOrder, action=success, order={}", savedOrder);

        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @Operation(summary = "Get an order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable UUID id) {
        log.info("operation=getOrder, action=start, id={}", id);

        Optional<OrderResponseDTO> order = orderService.getOrder(id);

        return order.map(value -> {
            log.info("operation=getOrder, action=success, id={}, order={}", id, value);
            return new ResponseEntity<>(value, HttpStatus.OK);
        }).orElseGet(() -> {
            log.info("operation=getOrder, action=fail, id={}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        });
    }

    @Operation(summary = "Get a summary of orders for ticker, order side, and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/summary")
    public ResponseEntity<OrderSummaryDTO> getSummary(
            @Parameter(description = "ticker", example = "SAVE") @RequestParam("ticker") String ticker,
            @Parameter(description = "Buy or sell", example = "BUY") @RequestParam("orderSide") OrderSide orderSide,
            @Parameter(description = "Date of the order", example = "2023-06-13") @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("operation=getSummary, action=start, ticker={}, orderSide={}, date={}", ticker, orderSide, date);

        OrderSummaryDTO summary = orderService.getSummary(ticker, orderSide, date);

        log.info("operation=getSummary, action=success, ticker={}, orderSide={}, date={}, summary={}", ticker, orderSide, date, summary);

        return new ResponseEntity<>(summary, HttpStatus.OK);
    }
}

