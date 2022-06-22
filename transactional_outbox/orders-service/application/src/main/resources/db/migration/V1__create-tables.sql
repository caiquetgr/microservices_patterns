CREATE TABLE orders
(
    id                    SERIAL,
    item                  character varying NOT NULL,
    total_value           decimal(18, 8)    NOT NULL,
    installments_value    decimal(18, 8)    NOT NULL,
    installments_quantity integer           NOT NULL,
    CONSTRAINT orders_pkey PRIMARY KEY (id)
);

CREATE TABLE orders_outbox
(
    id            SERIAL,
    order_message character varying        NOT NULL,
    created_at    timestamp with time zone NOT NULL,
    CONSTRAINT orders_outbox_pkey PRIMARY KEY (id)
);
