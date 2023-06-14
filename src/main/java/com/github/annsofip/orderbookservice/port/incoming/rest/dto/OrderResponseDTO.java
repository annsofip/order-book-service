package com.github.annsofip.orderbookservice.port.incoming.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.annsofip.orderbookservice.domain.model.OrderSide;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class OrderResponseDTO {
    @JsonProperty(value = "id")
    private UUID id;
    @Schema(description = "Ticker", example = "SAVE")
    @JsonProperty(value = "ticker")
    private String ticker;
    @Schema(description = "Side indicating if the order is for buying or selling", example = "BUY")
    @JsonProperty(value = "orderSide")
    private OrderSide orderSide;
    @Schema(description = "How many stocks to purchase", example = "100")
    @JsonProperty(value = "volume")
    private Integer volume;
    @Schema(description = "Price information indicating at which price to buy or sell", example = "12.99")
    @JsonProperty(value = "price")
    private BigDecimal price;
    @Schema(description = "Currency of the order", example = "SEK")
    @JsonProperty(value = "currency")
    private String currency;
    @Schema(description = "Date of the order", example = "2023-06-14", format = "yyyy-MM-dd")
    @JsonProperty(value = "date")
    private LocalDate date;
}
