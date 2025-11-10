package com.project.FurniQ.repository;

import com.project.FurniQ.entity.Furniture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface furnitureRepository extends JpaRepository<Furniture, Integer> {
}
