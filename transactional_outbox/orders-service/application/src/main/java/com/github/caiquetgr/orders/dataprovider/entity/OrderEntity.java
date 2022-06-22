package com.github.caiquetgr.orders.dataprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String item;

    @NotNull
    @Positive
    @Column(name = "total_value", nullable = false)
    private BigDecimal totalValue;

    @NotNull
    @Positive
    @Column(name = "installments_value", nullable = false)
    private BigDecimal installmentsValue;

    @NotNull
    @Positive
    @Column(name = "installments_quantity", nullable = false)
    private Integer installmentsQuantity;

}
