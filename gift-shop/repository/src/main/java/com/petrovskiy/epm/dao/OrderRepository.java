package com.petrovskiy.epm.dao;

import com.petrovskiy.epm.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findOrderByUserId(Long userId, Pageable pageable);

    boolean existsOrderByUserId(Long userId);

    Optional<Order> findFirstByProductListId(Long certificateId);
}
