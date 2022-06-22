package com.github.caiquetgr.orders.fixture;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.github.caiquetgr.orders.entrypoint.rest.request.OrderRequest;

import java.math.BigDecimal;

public class OrderRequestTemplateLoader implements TemplateLoader {

    public static final String VALID = "valid";

    @Override
    public void load() {
        addTemplateValid();
    }

    private void addTemplateValid() {
        Fixture.of(OrderRequest.class).addTemplate(VALID, new Rule() {{
            add("item", "Polystation 5");
            add("totalValue", new BigDecimal("5499.99"));
            add("installmentsValue", new BigDecimal("458.3325"));
            add("installmentsQuantity", Integer.valueOf(12));
        }});
    }
}
