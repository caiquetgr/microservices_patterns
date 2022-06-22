package com.github.caiquetgr.orders.domain;

import com.github.caiquetgr.orders.common.SelfValidatingEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
public class Order extends SelfValidatingEntity {

    @NotNull
    private Long id;

    @NotBlank(groups = BeforeCreate.class)
    private String item;

    @NotNull(groups = BeforeCreate.class)
    @Positive(groups = BeforeCreate.class)
    private BigDecimal totalValue;

    @NotNull(groups = BeforeCreate.class)
    @Positive(groups = BeforeCreate.class)
    private BigDecimal installmentsValue;

    @NotNull(groups = BeforeCreate.class)
    @Positive(groups = BeforeCreate.class)
    private Integer installmentsQuantity;

    public void validateOrderToCreate() throws ConstraintViolationException {
        validateFieldsBeforeCreate();
        validateInstallments();
    }

    private void validateFieldsBeforeCreate() {
        validateSelf(BeforeCreate.class);
    }

    private void validateInstallments() {
        final BigDecimal calculatedTotalValue = installmentsValue
                .multiply(BigDecimal.valueOf(installmentsQuantity))
                .setScale(2, RoundingMode.HALF_UP);

        if (calculatedTotalValue.compareTo(totalValue) != 0) {
            throw new ConstraintViolationException("installments and total value are not compatible", null);
        }
    }

    interface BeforeCreate {
    }
}
