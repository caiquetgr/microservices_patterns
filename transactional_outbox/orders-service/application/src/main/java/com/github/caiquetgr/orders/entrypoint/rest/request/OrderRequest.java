package com.github.caiquetgr.orders.entrypoint.rest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderRequest {
    @NotBlank
    private String item;

    @NotNull
    @Positive
    private BigDecimal totalValue;

    @NotNull
    @Positive
    private BigDecimal installmentsValue;

    @NotNull
    @Positive
    private Integer installmentsQuantity;
}
