package com.EntregableAndres.Repositories;

import com.EntregableAndres.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
