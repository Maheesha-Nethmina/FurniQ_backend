package com.project.FurniQ.repository;

import com.project.FurniQ.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByUserId(Integer userId);
    Optional<Cart> findByUserIdAndProductIdAndProductType(Integer userId, Integer productId, String productType);
    void deleteByUserId(Integer userId);
}