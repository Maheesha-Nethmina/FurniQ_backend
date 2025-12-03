package com.project.FurniQ.service;

import com.project.FurniQ.dto.OderDTO;
import com.project.FurniQ.entity.Oder;
import com.project.FurniQ.entity.Furniture;
import com.project.FurniQ.entity.HomeDeco;
import com.project.FurniQ.repository.furnitureRepository;
import com.project.FurniQ.repository.homedecoRepository;
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

    private final furnitureRepository furnitureRepo;
    private final homedecoRepository homedecoRepo;

    // Save new order
    public String saveNewOrder(OderDTO orderDTO) {
        // Check if user exists
        if(!userRepository.existsById(orderDTO.getUserId())) {
            return VarList.RSP_FAIL;
        }

        boolean stockUpdated = false;

        // 1. Logic to update Quantity based on Type
        if ("FURNITURE".equalsIgnoreCase(orderDTO.getOderType())) {
            Optional<Furniture> furnOpt = furnitureRepo.findById(orderDTO.getProductId());
            if (furnOpt.isPresent()) {
                Furniture furniture = furnOpt.get();
                if (furniture.getQuantity() >= orderDTO.getQuantity()) {
                    furniture.setQuantity(furniture.getQuantity() - orderDTO.getQuantity());
                    furnitureRepo.save(furniture);
                    stockUpdated = true;
                } else {
                    return "OUT_OF_STOCK";
                }
            }
        } else if ("HOMEDECO".equalsIgnoreCase(orderDTO.getOderType())) {
            Optional<HomeDeco> decoOpt = homedecoRepo.findById(orderDTO.getProductId());
            if (decoOpt.isPresent()) {
                HomeDeco homeDeco = decoOpt.get();
                if (homeDeco.getQuantity() >= orderDTO.getQuantity()) {
                    homeDeco.setQuantity(homeDeco.getQuantity() - orderDTO.getQuantity());
                    homedecoRepo.save(homeDeco);
                    stockUpdated = true;
                } else {
                    return "OUT_OF_STOCK";
                }
            }
        }

        // 2. Save Order and Send Email
        if (stockUpdated) {
            Oder order = modelMapper.map(orderDTO, Oder.class);
            order.setOderStatus("To Be Ship");
            order.setPaymentStatus("yes");

            // Capture the saved order to get the generated ID
            Oder savedOrder = orderRepository.save(order);

            // --- NEW EMAIL LOGIC STARTS HERE ---
            try {
                String subject = "Order Confirmation" ;
                String body = "Dear " + savedOrder.getUsername() + ",\n\n" +
                        "Thank you for shopping with FurniQ! Your order has been placed successfully.\n\n" +
                        "--- Order Details ---\n" +
                        "Product: " + savedOrder.getProductName() + "\n" +
                        "Quantity: " + savedOrder.getQuantity() + "\n" +
                        "Total Price: " + savedOrder.getPrice() + "\n" +
                        "Address: " + savedOrder.getAddress() + "\n" +
                        "Oder Status: " + savedOrder.getOderStatus() + "\n\n" +
                        "We will notify you again when your item is shipped.\n\n" +
                        "Best Regards,\n" +
                        "FurniQ Team";

                emailService.sendCustomEmail(savedOrder.getEmail(), subject, body);
            } catch (Exception e) {
                System.out.println("Failed to send confirmation email: " + e.getMessage());
            }


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

    // get oder by id
    public List<OderDTO> getOrdersByUserId(Integer userId) {
        List<Oder> orderList = orderRepository.findByUserId(userId);
        Type listType = new TypeToken<List<OderDTO>>() {}.getType();
        return modelMapper.map(orderList, listType);
    }
}

