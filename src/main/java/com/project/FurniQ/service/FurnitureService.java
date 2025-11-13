package com.project.FurniQ.service;

import com.project.FurniQ.dto.FurnitureDTO;
import com.project.FurniQ.entity.Furniture;
import com.project.FurniQ.repository.furnitureRepository;
import com.project.FurniQ.util.VarList;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FurnitureService {

    private final furnitureRepository furnitureRepository;
    private final ModelMapper modelMapper;


    //save furniture details
    public String saveNewfurniture(FurnitureDTO furnitureDTO){
        furnitureRepository.save(modelMapper.map(furnitureDTO, Furniture.class));
        return VarList.RSP_SUCCESS;
    }
}