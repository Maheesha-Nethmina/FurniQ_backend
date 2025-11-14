package com.project.FurniQ.controller;

import com.project.FurniQ.dto.FurnitureDTO;
import com.project.FurniQ.dto.ResponseDTO;
import com.project.FurniQ.service.FurnitureService;
import com.project.FurniQ.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/furniture")
public class FurnitureController {

@Autowired
private FurnitureService furnitureService;
    @Autowired
    private ResponseDTO responseDTO;

    @PostMapping(value = "/saveNewfurniture")

public ResponseEntity<ResponseDTO> saveNewfurniture(@RequestBody FurnitureDTO furnitureDTO) {

    ResponseDTO responseDTO = new ResponseDTO();
    try {
        String res = furnitureService.saveNewfurniture(furnitureDTO);

        if (res.equals(VarList.RSP_SUCCESS)) {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Successfully saved new furniture");
            responseDTO.setContent(furnitureDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        } else if (res.equals(VarList.RSP_DUPLICATED)) {
            responseDTO.setCode(VarList.RSP_DUPLICATED);
            responseDTO.setMessage("Furniture already exists");
            responseDTO.setContent(furnitureDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);

        } else {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error");
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        }
    } catch (Exception ex) {
        responseDTO.setCode(VarList.RSP_ERROR);
        responseDTO.setMessage(ex.getMessage());
        responseDTO.setContent(null);
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@PutMapping(value = "/updateFurniture")
public ResponseEntity<ResponseDTO> updateFurniture(@RequestBody FurnitureDTO furnitureDTO) {
    ResponseDTO responseDTO = new ResponseDTO();
    try{
        String res = furnitureService.updateFurniture(furnitureDTO);
        if(res.equals("00")){
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Successfully updated furniture");
            responseDTO.setContent(furnitureDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        }else if(res.equals("01")){
            responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
            responseDTO.setMessage("Furniture already exists");
            responseDTO.setContent(furnitureDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);

        }else{
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error");
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        }

    } catch (Exception ex) {
        responseDTO.setCode(VarList.RSP_ERROR);
        responseDTO.setMessage(ex.getMessage());
        responseDTO.setContent(null);
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}

@GetMapping(value = "getAllfurnitures")
public ResponseEntity getAllFurnitures() {
    try{
        List<FurnitureDTO> furnitureDTOList = furnitureService.getAllFurniture();
        responseDTO.setCode(VarList.RSP_SUCCESS);
        responseDTO.setMessage("Successfully retrieved all furniture");
        responseDTO.setContent(furnitureDTOList);
        return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);

    } catch (Exception ex) {
        responseDTO.setCode(VarList.RSP_ERROR);
        responseDTO.setMessage("Error ocurred");
        responseDTO.setContent(null);
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}

}