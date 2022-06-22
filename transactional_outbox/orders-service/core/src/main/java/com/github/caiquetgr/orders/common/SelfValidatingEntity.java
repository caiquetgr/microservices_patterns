package com.github.caiquetgr.orders.common;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public abstract class SelfValidatingEntity {
    private final Validator validator;

    protected SelfValidatingEntity() {
        this.validator = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator();
    }

    protected void validateSelf(final Class<?>... validatingGroups) throws ConstraintViolationException {
        final Set<ConstraintViolation<SelfValidatingEntity>> violations = validator
                .validate(this, validatingGroups);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
