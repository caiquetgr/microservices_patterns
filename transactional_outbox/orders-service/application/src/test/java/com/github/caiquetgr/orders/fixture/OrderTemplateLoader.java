package com.github.caiquetgr.orders.fixture;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.github.caiquetgr.orders.domain.Order;

import java.math.BigDecimal;

public class OrderTemplateLoader implements TemplateLoader {

    public static final String VALID_AFTER_CREATE = "validAfterCreate";
    public static final String VALID_BEFORE_CREATE = "validBeforeCreate";

    @Override
    public void load() {
        addTemplateValid();
    }

    private void addTemplateValid() {
        Fixture.of(Order.class).addTemplate(VALID_BEFORE_CREATE, new Rule() {{
            add("item", "Polystation 5");
            add("totalValue", new BigDecimal("5499.99"));
            add("installmentsValue", new BigDecimal("458.3325"));
            add("installmentsQuantity", Integer.valueOf(12));
        }});

        Fixture.of(Order.class).addTemplate(VALID_AFTER_CREATE).inherits(VALID_BEFORE_CREATE, new Rule() {{
            add("id", 1L);
        }});
    }
}
