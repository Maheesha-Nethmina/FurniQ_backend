package com.project.FurniQ.controller;

import com.project.FurniQ.dto.CartDTO;
import com.project.FurniQ.dto.ResponseDTO;
import com.project.FurniQ.entity.Cart;
import com.project.FurniQ.service.CartService;
import com.project.FurniQ.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/cart")
@CrossOrigin
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> addToCart(@RequestBody Cart cart) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            String res = cartService.addToCart(cart);
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Added to cart");
            responseDTO.setContent(cart);
            return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<ResponseDTO> getUserCart(@PathVariable Integer userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<CartDTO> cartList = cartService.getUserCart(userId);
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Success");
            responseDTO.setContent(cartList);
            return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<ResponseDTO> removeFromCart(@PathVariable Integer cartId) {
        ResponseDTO responseDTO = new ResponseDTO();
        String res = cartService.removeFromCart(cartId);
        responseDTO.setCode(res);
        responseDTO.setMessage("Item removed");
        return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
    }
}