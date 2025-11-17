package com.project.FurniQ.service;

import com.project.FurniQ.dto.HomedecoDTO;
import com.project.FurniQ.entity.HomeDeco;
import com.project.FurniQ.repository.homedecoRepository;
import com.project.FurniQ.util.VarList;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor

public class HomedecoService {

    private final homedecoRepository homedecoRepository;
    private final ModelMapper modelMapper;

    //save new Homedeco
    public String saveNewDeco(HomedecoDTO homedecoDTO) {
        homedecoRepository.save(modelMapper.map(homedecoDTO, HomeDeco.class));
        return VarList.RSP_SUCCESS;
    }


}
