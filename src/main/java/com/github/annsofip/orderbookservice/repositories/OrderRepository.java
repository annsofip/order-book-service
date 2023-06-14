package com.github.annsofip.orderbookservice.repositories;

import com.github.annsofip.orderbookservice.model.OrderSide;
import com.github.annsofip.orderbookservice.model.OrderSummary;
import com.github.annsofip.orderbookservice.repositories.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT new com.github.annsofip.orderbookservice.model.OrderSummary(" +
            "avg(o.price), " +
            "max(o.price), " +
            "min(o.price), " +
            "count(o)) " +
            "FROM Order o " +
            "WHERE o.ticker = :ticker " +
            "AND o.orderSide = :orderSide " +
            "AND CAST(o.date AS DATE) = :date " +
            "GROUP BY o.ticker, o.orderSide, o.date")
    Optional<OrderSummary> findSummaryByTickerAndOrderSideAndDate(
            @Param("ticker") String ticker,
            @Param("orderSide") OrderSide orderSide,
            @Param("date") LocalDate date);

}
