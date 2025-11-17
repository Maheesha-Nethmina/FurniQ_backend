package com.project.FurniQ.service;

import com.project.FurniQ.dto.FurnitureDTO;
import com.project.FurniQ.entity.Furniture;
import com.project.FurniQ.repository.furnitureRepository;
import com.project.FurniQ.util.VarList;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    //update saved item
    public String updateFurniture(FurnitureDTO furnitureDTO){
        if(furnitureRepository.existsById(furnitureDTO.getId())){
            furnitureRepository.save(modelMapper.map(furnitureDTO, Furniture.class));
            return VarList.RSP_SUCCESS;
        }else{
            return VarList.RSP_NO_DATA_FOUND;
        }
    }

    //get All furnitures
    public List<FurnitureDTO> getAllFurniture(){
        List<Furniture> furnitureList = furnitureRepository.findAll();
        Type listType = new TypeToken<List<FurnitureDTO>>(){}.getType();
        return modelMapper.map(furnitureList, listType);
    }

    //search furniture
    public FurnitureDTO getFurnitureById(Integer id){
        if(furnitureRepository.existsById(id)){
            Furniture furniture = furnitureRepository.findById(id).orElse(null);
            return modelMapper.map(furniture, FurnitureDTO.class);
        }
        else {
            return null;
        }
    }

    //delete furniture
    public String DeleteFurniture(Integer id){
        if(furnitureRepository.existsById(id)){
            furnitureRepository.deleteById(id);
            return VarList.RSP_SUCCESS;
        }else{
            return VarList.RSP_NO_DATA_FOUND;
        }
    }



}