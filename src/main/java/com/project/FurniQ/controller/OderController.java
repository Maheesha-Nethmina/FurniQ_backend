package com.project.FurniQ.controller;

import com.project.FurniQ.dto.OderDTO;
import com.project.FurniQ.dto.ResponseDTO;
import com.project.FurniQ.service.OderService;
import com.project.FurniQ.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.FurniQ.dto.CheckoutRequestDTO;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/order")
public class OderController {

    @Autowired
    private OderService orderService;
    

    // SaveNew  Order
    @PostMapping(value = "/saveNewOrder")
    public ResponseEntity<ResponseDTO> saveNewOrder(@RequestBody OderDTO oderDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            String res = orderService.saveNewOrder(oderDTO);

            if (res.equals(VarList.RSP_SUCCESS)) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully placed new order");
                responseDTO.setContent(oderDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

            } else if (res.equals("OUT_OF_STOCK")) {
                // New Error Handling
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Order Failed: Item is Out of Stock");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);

            } else {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Failed to place order (User or Product not found)");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error");
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get All Orders
    @GetMapping(value = "/getAllOrders")
    public ResponseEntity<ResponseDTO> getAllOrders() {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<OderDTO> orderList = orderService.getAllOrders();
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Successfully fetched all orders");
            responseDTO.setContent(orderList);
            return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);

        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error");
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Delete Order (Cancel)
    @DeleteMapping(value = "/deleteOrder/{orderId}")
    public ResponseEntity<ResponseDTO> deleteOrder(@PathVariable Integer orderId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            String res = orderService.deleteOrder(orderId);
            if (res.equals(VarList.RSP_SUCCESS)) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Order cancelled and email sent successfully");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Order ID not found");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error");
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Mark as Shipped
    @PutMapping(value = "/markAsShipped/{orderId}")
    public ResponseEntity<ResponseDTO> markAsShipped(@PathVariable Integer orderId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            String res = orderService.markAsShipped(orderId);
            if (res.equals(VarList.RSP_SUCCESS)) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Order marked as shipped and email sent");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Order ID not found");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error");
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getOrdersByUserId/{userId}")
    public ResponseEntity<ResponseDTO> getOrdersByUserId(@PathVariable Integer userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            // Call the service
            List<OderDTO> userOrders = orderService.getOrdersByUserId(userId);

            if (userOrders != null && !userOrders.isEmpty()) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Successfully fetched user orders");
                responseDTO.setContent(userOrders);
                return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("No orders found for this user");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error fetching data");
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/checkout")
    public ResponseEntity<ResponseDTO> checkoutCart(@RequestBody CheckoutRequestDTO request) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            String res = orderService.checkoutCart(request);
            if (res.equals(VarList.RSP_SUCCESS)) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Order placed successfully!");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
            } else if (res.equals("CART_EMPTY")) {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("Cart is empty");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            } else {
                responseDTO.setCode(VarList.RSP_FAIL);
                responseDTO.setMessage("Failed to place order");
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