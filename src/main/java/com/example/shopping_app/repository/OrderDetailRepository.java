package com.example.shopping_app.repository;

import com.example.shopping_app.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
