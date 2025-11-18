package com.project.FurniQ.service;

import com.project.FurniQ.dto.HomedecoDTO;
import com.project.FurniQ.entity.HomeDeco;
import com.project.FurniQ.repository.homedecoRepository;
import com.project.FurniQ.util.VarList;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HomedecoService {

    private final homedecoRepository homedecoRepository;
    private final ModelMapper modelMapper;

    // Save new Homedeco
    public String saveNewDeco(HomedecoDTO homedecoDTO) {
        List<HomeDeco> existing = homedecoRepository.findByDecoName(homedecoDTO.getDecoName());

        if (!existing.isEmpty()) {
            return VarList.RSP_DUPLICATED;
        }

        homedecoRepository.save(modelMapper.map(homedecoDTO, HomeDeco.class));
        return VarList.RSP_SUCCESS;
    }

    // Update saved deco items
    public String updateDeco(HomedecoDTO homedecoDTO) {
        if(homedecoRepository.existsById(homedecoDTO.getId())) {
            homedecoRepository.save(modelMapper.map(homedecoDTO, HomeDeco.class));
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_NO_DATA_FOUND;
        }
    }

    // Get all
    public List<HomedecoDTO> getAllHomedeco() {
        List<HomeDeco> homedecoList = homedecoRepository.findAll();
        Type listType = new TypeToken<List<HomedecoDTO>>() {}.getType();
        return modelMapper.map(homedecoList, listType);
    }

    // Search by ID
    public HomedecoDTO getHomedecoById(Integer id) {
        if(homedecoRepository.existsById(id)) {
            HomeDeco homeDeco = homedecoRepository.findById(id).get();
            return modelMapper.map(homeDeco, HomedecoDTO.class);
        } else {
            return null;
        }
    }

    // Delete
    public String deleteHomedeco(Integer id) {
        if(homedecoRepository.existsById(id)) {
            homedecoRepository.deleteById(id);
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_NO_DATA_FOUND;
        }
    }
}