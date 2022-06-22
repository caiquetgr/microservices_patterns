package com.github.caiquetgr.orders.dataprovider;

import com.github.caiquetgr.orders.dataprovider.entity.OrderTransactionalOutboxEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTransactionalOutboxRepository extends CrudRepository<OrderTransactionalOutboxEntity, Long> {
}
