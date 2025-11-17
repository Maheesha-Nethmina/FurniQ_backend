package com.project.FurniQ.repository;

import com.project.FurniQ.entity.Furniture;
import com.project.FurniQ.entity.HomeDeco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface homedecoRepository extends JpaRepository<HomeDeco, Integer> {
    Optional<HomeDeco> findById(Integer id);
    List<HomeDeco> findByHodeDecoName(String decoName);

}
