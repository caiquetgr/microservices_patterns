package com.github.caiquetgr.orders.fixture;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.github.caiquetgr.orders.entrypoint.rest.request.OrderRequest;

import java.math.BigDecimal;

public class OrderRequestIntegrationTestTemplateLoader implements TemplateLoader {

    public static final String VALID_IT = "valid_it";

    @Override
    public void load() {
        addTemplateValid();
    }

    private void addTemplateValid() {
        Fixture.of(OrderRequest.class).addTemplate(VALID_IT, new Rule() {{
            add("item", "Polystation 5");
            add("totalValue", new BigDecimal("5499.99000000").setScale(8));
            add("installmentsValue", new BigDecimal("458.33250000").setScale(8));
            add("installmentsQuantity", Integer.valueOf(12));
        }});
    }
}
