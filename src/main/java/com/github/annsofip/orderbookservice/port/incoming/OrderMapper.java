package com.github.annsofip.orderbookservice.port.incoming;

import com.github.annsofip.orderbookservice.port.incoming.rest.dto.OrderRequestDTO;
import com.github.annsofip.orderbookservice.port.incoming.rest.dto.OrderResponseDTO;
import com.github.annsofip.orderbookservice.port.outgoing.repository.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface OrderMapper {
    OrderResponseDTO orderToOrderDto(Order order);
    Order createOrderDtoToOrder(OrderRequestDTO orderDTO);
}
