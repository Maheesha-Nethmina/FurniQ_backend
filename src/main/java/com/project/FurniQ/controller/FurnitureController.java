package com.project.FurniQ.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.FurniQ.dto.FurnitureDTO;
import com.project.FurniQ.dto.ResponseDTO;
import com.project.FurniQ.service.FurnitureService;
import com.project.FurniQ.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/furniture")
@CrossOrigin // Added for Frontend access
public class FurnitureController {

    @Autowired
    private FurnitureService furnitureService;

    // ADD THIS PART
    @GetMapping("/all")
    public ResponseEntity<List<FurnitureDTO>> getAllFurniture() {
        return ResponseEntity.ok(furnitureService.getAllFurniture());
    }


    @PostMapping(value = "/saveNewfurniture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> saveNewfurniture(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("data") String furnitureData) {

        ResponseDTO responseDTO = new ResponseDTO();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            FurnitureDTO furnitureDTO = objectMapper.readValue(furnitureData, FurnitureDTO.class);
            String res = furnitureService.saveNewfurniture(furnitureDTO, file);

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
            responseDTO.setMessage("Error: " + ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/updateFurniture")
    public ResponseEntity<ResponseDTO> updateFurniture(@RequestBody FurnitureDTO furnitureDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            String res = furnitureService.updateFurniture(furnitureDTO);
            if (res.equals(VarList.RSP_SUCCESS)) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully updated furniture");
                responseDTO.setContent(furnitureDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Furniture does not exist");
                responseDTO.setContent(furnitureDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getAllfurnitures")
    public ResponseEntity<ResponseDTO> getAllFurnitures() {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<FurnitureDTO> furnitureDTOList = furnitureService.getAllFurniture();
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Successfully retrieved all furniture");
            responseDTO.setContent(furnitureDTOList);
            return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred");
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getFurnitureById")
    public ResponseEntity<ResponseDTO> getFurnitureById(@RequestParam Integer id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            FurnitureDTO furnitureDTO = furnitureService.getFurnitureById(id);
            if (furnitureDTO != null) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully retrieved furniture");
                responseDTO.setContent(furnitureDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Furniture found");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred");
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/deleteFurniture")
    public ResponseEntity<ResponseDTO> deleteFurniture(@RequestParam Integer id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            String res = furnitureService.DeleteFurniture(id);
            if (res.equals(VarList.RSP_SUCCESS)) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully deleted");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Furniture found");
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
}