package com.project.FurniQ.service;

import com.project.FurniQ.dto.OderDTO;
import com.project.FurniQ.entity.Oder;
import com.project.FurniQ.repository.oderRepository;
import com.project.FurniQ.repository.userRepository;
import com.project.FurniQ.util.VarList;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OderService {

    private final ModelMapper modelMapper;
    private final oderRepository orderRepository;
    private final userRepository userRepository;
    private final EmailService emailService;

    // Save new order
    public String saveNewOrder(OderDTO orderDTO) {
        if(userRepository.existsById(orderDTO.getUserId())) {
            Oder order = modelMapper.map(orderDTO, Oder.class);
            orderRepository.save(order);
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_FAIL;
        }
    }

    // Get All Orders
    public List<OderDTO> getAllOrders() {
        List<Oder> orderList = orderRepository.findAll();
        Type listType = new TypeToken<List<OderDTO>>() {}.getType();
        return modelMapper.map(orderList, listType);
    }

    // Cancel Order (Delete and Email)
    public String deleteOrder(Integer orderId) {
        Optional<Oder> orderOpt = orderRepository.findById(orderId);

        if (orderOpt.isPresent()) {
            Oder order = orderOpt.get();
            String userEmail = order.getEmail();
            orderRepository.deleteById(orderId);

            //Send Cancellation Email
            emailService.sendCustomEmail(
                    userEmail,
                    "Order Cancellation",
                    "Your order (ID: " + orderId + ") has been successfully cancelled."
            );

            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_FAIL;
        }
    }

    // Mark as Shipped (Update Status and Email)
    public String markAsShipped(Integer orderId) {
        Optional<Oder> orderOpt = orderRepository.findById(orderId);

        if (orderOpt.isPresent()) {
            Oder order = orderOpt.get();

            // Send Shipping Email
            emailService.sendCustomEmail(
                    order.getEmail(),
                    "Order Shipped!",
                    "Good news! Your order (ID: " + orderId + ") " +
                            "containing " + order.getProductName() + " has been shipped."
            );
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_FAIL;
        }
    }
}