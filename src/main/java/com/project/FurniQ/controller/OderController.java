package com.project.FurniQ.controller;


import com.project.FurniQ.service.OderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("api/v1/order")
public class OderController {

    @Autowired
    private OderService orderService;


}
