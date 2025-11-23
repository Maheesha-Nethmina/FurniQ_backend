package com.project.FurniQ.repository;

import com.project.FurniQ.entity.HomeDeco;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface homedecoRepository extends JpaRepository<HomeDeco, Integer> {
    List<HomeDeco> findByDecoName(String decoName);
}