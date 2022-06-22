package com.github.caiquetgr.orders.dataprovider;

import com.github.caiquetgr.orders.dataprovider.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDataJpaRepository extends CrudRepository<OrderEntity, Long> {
}
