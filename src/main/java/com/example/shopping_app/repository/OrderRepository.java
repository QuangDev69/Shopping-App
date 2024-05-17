package com.example.shopping_app.repository;

import com.example.shopping_app.entity.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface OrderRepository extends JpaRepository<Order, Long> {
    //Tìm các đơn hàng của 1 user nào đó
    List<Order> findByUserId(Long userId);


    @Query("SELECT o FROM Order o WHERE " +
            "(o.isActive = true) AND " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "o.fullName LIKE CONCAT('%', :keyword, '%') OR " +
            "o.address LIKE CONCAT('%', :keyword, '%') OR " +
            "o.note LIKE CONCAT('%', :keyword, '%'))")
    Page<Order> findOrderByKeyword(String keyword, Pageable pageable);
}
