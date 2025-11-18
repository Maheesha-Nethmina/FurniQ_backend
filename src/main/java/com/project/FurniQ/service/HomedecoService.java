package com.project.FurniQ.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project.FurniQ.dto.HomedecoDTO;
import com.project.FurniQ.entity.HomeDeco;
import com.project.FurniQ.repository.homedecoRepository;
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
public class HomedecoService {

    private final homedecoRepository homedecoRepository;
    private final ModelMapper modelMapper;

    private final Cloudinary cloudinary;
    //save new deco item
    public String saveNewDeco(HomedecoDTO homedecoDTO, MultipartFile file) {

        List<HomeDeco> existing = homedecoRepository.findByDecoName(homedecoDTO.getDecoName());
        if (!existing.isEmpty()) {
            return VarList.RSP_DUPLICATED;
        }

        try {
            if (file != null && !file.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                homedecoDTO.setDecoPicture(uploadResult.get("url").toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return VarList.RSP_FAIL;
        }
        homedecoRepository.save(modelMapper.map(homedecoDTO, HomeDeco.class));
        return VarList.RSP_SUCCESS;
    }
    //update deco item
    public String updateDeco(HomedecoDTO homedecoDTO) {
        if(homedecoRepository.existsById(homedecoDTO.getId())) {
            homedecoRepository.save(modelMapper.map(homedecoDTO, HomeDeco.class));
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_NO_DATA_FOUND;
        }
    }
    //get All deco items
    public List<HomedecoDTO> getAllHomedeco() {
        List<HomeDeco> homedecoList = homedecoRepository.findAll();
        Type listType = new TypeToken<List<HomedecoDTO>>() {}.getType();
        return modelMapper.map(homedecoList, listType);
    }

    //get deco item using id
    public HomedecoDTO getHomedecoById(Integer id) {
        if(homedecoRepository.existsById(id)) {
            HomeDeco homeDeco = homedecoRepository.findById(id).get();
            return modelMapper.map(homeDeco, HomedecoDTO.class);
        } else {
            return null;
        }
    }
    //remove listed deco item
    public String deleteHomedeco(Integer id) {
        if(homedecoRepository.existsById(id)) {
            homedecoRepository.deleteById(id);
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_NO_DATA_FOUND;
        }
    }
}