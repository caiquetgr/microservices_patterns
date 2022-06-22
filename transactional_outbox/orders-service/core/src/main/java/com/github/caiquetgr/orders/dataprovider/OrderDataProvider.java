package com.github.caiquetgr.orders.dataprovider;

import com.github.caiquetgr.orders.domain.Order;

public interface OrderDataProvider {
    Order create(final Order order);
}
