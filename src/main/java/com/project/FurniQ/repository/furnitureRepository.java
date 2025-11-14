package com.project.FurniQ.repository;

import com.project.FurniQ.entity.Furniture;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface furnitureRepository extends JpaRepository<Furniture, Integer> {
    Optional<Furniture> findById(Integer id);
    List<Furniture> findByFurnitureName(String furnitureName);
}
