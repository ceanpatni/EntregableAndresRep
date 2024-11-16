package com.EntregableAndres.Repositories;


import com.EntregableAndres.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
