package com.github.annsofip.orderbookservice.port.incoming.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.annsofip.orderbookservice.domain.model.OrderSide;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    @Schema(description = "Ticker", example = "SAVE")
    @JsonProperty(value = "ticker")
    @NotNull(message = "Ticker may not be null")
    @Size(min = 2, max = 10, message = "Ticker must be between 2 and 10 characters long")
    private String ticker;
    @Schema(description = "Side indicating if the order is for buying or selling", example = "BUY")
    @JsonProperty(value = "orderSide")
    @NotNull(message = "OrderSide may not be null")
    private OrderSide orderSide;
    @Schema(description = "How many stocks to purchase", example = "100")
    @JsonProperty(value = "volume")
    @NotNull(message = "volume may not be null")
    private Integer volume;
    @Schema(description = "Price information indicating at which price to buy or sell", example = "12.99")
    @JsonProperty(value = "price")
    @NotNull(message = "price may not be null")
    private BigDecimal price;
    @Schema(description = "Currency of the order", example = "SEK")
    @JsonProperty(value = "currency")
    @NotNull(message = "currency may not be null")
    @Size(min = 2, max = 10, message = "currency must be between 2 and 10 characters long")
    private String currency;
}
