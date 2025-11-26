package com.project.FurniQ.repository;

import com.project.FurniQ.entity.Oder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface oderRepository extends JpaRepository<Oder, Integer> {
    Optional<Oder> findById(Integer orderId);
    List<Oder> findByUserId(Integer Id);
    List<Oder> findByEmail(String email);

}
