package com.github.annsofip.orderbookservice.model;

import com.github.annsofip.orderbookservice.api.OrderRequestDTO;
import com.github.annsofip.orderbookservice.api.OrderResponseDTO;
import com.github.annsofip.orderbookservice.repositories.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface OrderMapper {
    OrderResponseDTO orderToOrderDto(Order order);

    Order createOrderDtoToOrder(OrderRequestDTO orderDTO);
}
