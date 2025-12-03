package com.project.FurniQ.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.FurniQ.dto.HomedecoDTO;
import com.project.FurniQ.dto.ResponseDTO;
import com.project.FurniQ.service.HomedecoService;
import com.project.FurniQ.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/homedeco")
@CrossOrigin
public class HomedecoController {

    @Autowired
    private HomedecoService homedecoService;

    @PostMapping(value = "/saveHomedeco", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> saveHomedeco(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("data") String homedecoData
    ) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            HomedecoDTO homedecoDTO = objectMapper.readValue(homedecoData, HomedecoDTO.class);
            String res = homedecoService.saveNewDeco(homedecoDTO, file);

            if (res.equals(VarList.RSP_SUCCESS)) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success");
                responseDTO.setContent(homedecoDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
            } else if (res.equals(VarList.RSP_DUPLICATED)) {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Home Deco already exists");
                responseDTO.setContent(homedecoDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            } else {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Fail");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error: " + ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/updateDeco")
    public ResponseEntity<ResponseDTO> updateDeco(@RequestBody HomedecoDTO homedecoDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            String res = homedecoService.updateDeco(homedecoDTO);
            if (res.equals(VarList.RSP_SUCCESS)) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully updated");
                responseDTO.setContent(homedecoDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Home Deco does not exist");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error: ");
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getAllHomedeco")
    public ResponseEntity<ResponseDTO> getAllHomedeco() {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<HomedecoDTO> homeDecoList = homedecoService.getAllHomedeco();
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Success");
            responseDTO.setContent(homeDecoList);
            return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error: ");
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getHomedecoById")
    public ResponseEntity<ResponseDTO> getHomedecoById(@RequestParam Integer id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            HomedecoDTO homedecoDTO = homedecoService.getHomedecoById(id);
            if (homedecoDTO != null) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success");
                responseDTO.setContent(homedecoDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No Home Deco found with that ID");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error: ");
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/deleteHomedeco")
    public ResponseEntity<ResponseDTO> deleteHomedeco(@RequestParam Integer id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            String res = homedecoService.deleteHomedeco(id);
            if (res.equals(VarList.RSP_SUCCESS)) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully deleted");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No HomeDeco found");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Error: ");
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}