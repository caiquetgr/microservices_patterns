package com.github.caiquetgr.orders.entrypoint.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Getter
public class OrderResponse {
    private Integer id;
    private String item;
    private BigDecimal totalValue;
    private BigDecimal installmentsValue;
    private Integer installmentsQuantity;
}
