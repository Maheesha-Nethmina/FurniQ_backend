package com.project.FurniQ.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project.FurniQ.dto.FurnitureDTO;
import com.project.FurniQ.entity.Furniture;
import com.project.FurniQ.repository.furnitureRepository;
import com.project.FurniQ.util.VarList;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class FurnitureService {

    private final furnitureRepository furnitureRepository;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;
    //save new item
    public String saveNewfurniture(FurnitureDTO furnitureDTO, MultipartFile file) {

        List<Furniture> existing = furnitureRepository.findByFurnitureName(furnitureDTO.getFurnitureName());
        if (!existing.isEmpty()) {
            return VarList.RSP_DUPLICATED;
        }

        try {
            if (file != null && !file.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                furnitureDTO.setFurniturePicture(uploadResult.get("url").toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return VarList.RSP_FAIL;
        }
        furnitureRepository.save(modelMapper.map(furnitureDTO, Furniture.class));
        return VarList.RSP_SUCCESS;
    }

    // Update saved item
    public String updateFurniture(FurnitureDTO furnitureDTO) {
        if (furnitureRepository.existsById(furnitureDTO.getId())) {
            furnitureRepository.save(modelMapper.map(furnitureDTO, Furniture.class));
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_NO_DATA_FOUND;
        }
    }

    // Get All furnitures
    public List<FurnitureDTO> getAllFurniture() {
        List<Furniture> furnitureList = furnitureRepository.findAll();
        Type listType = new TypeToken<List<FurnitureDTO>>() {}.getType();
        return modelMapper.map(furnitureList, listType);
    }

    // Search furniture
    public FurnitureDTO getFurnitureById(Integer id) {
        if (furnitureRepository.existsById(id)) {
            Furniture furniture = furnitureRepository.findById(id).orElse(null);
            return modelMapper.map(furniture, FurnitureDTO.class);
        } else {
            return null;
        }
    }

    // Delete furniture
    public String DeleteFurniture(Integer id) {
        if (furnitureRepository.existsById(id)) {
            furnitureRepository.deleteById(id);
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_NO_DATA_FOUND;
        }
    }
}