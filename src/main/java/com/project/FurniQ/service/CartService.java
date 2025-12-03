package com.project.FurniQ.service;

import com.project.FurniQ.dto.CartDTO;
import com.project.FurniQ.entity.Cart;
import com.project.FurniQ.entity.Furniture;
import com.project.FurniQ.entity.HomeDeco;
import com.project.FurniQ.repository.CartRepository;
import com.project.FurniQ.repository.furnitureRepository;
import com.project.FurniQ.repository.homedecoRepository;
import com.project.FurniQ.util.VarList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final furnitureRepository furnitureRepo;
    private final homedecoRepository homedecoRepo;

    // 1. Add to Cart
    public String addToCart(Cart cartItem) {
        // Check if item already exists in this user's cart
        Optional<Cart> existing = cartRepository.findByUserIdAndProductIdAndProductType(
                cartItem.getUserId(), cartItem.getProductId(), cartItem.getProductType());

        if (existing.isPresent()) {
            Cart updateCart = existing.get();
            updateCart.setQuantity(updateCart.getQuantity() + cartItem.getQuantity());
            cartRepository.save(updateCart);
        } else {
            cartRepository.save(cartItem);
        }
        return VarList.RSP_SUCCESS;
    }

    // 2. Get User Cart (With mapped details)
    public List<CartDTO> getUserCart(Integer userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        List<CartDTO> responseList = new ArrayList<>();

        for (Cart item : cartItems) {
            CartDTO dto = new CartDTO();
            dto.setCartId(item.getCartId());
            dto.setProductId(item.getProductId());
            dto.setProductType(item.getProductType());
            dto.setQuantity(item.getQuantity());

            // Fetch details based on type
            if ("FURNITURE".equalsIgnoreCase(item.getProductType())) {
                Optional<Furniture> f = furnitureRepo.findById(item.getProductId());
                if (f.isPresent()) {
                    dto.setProductName(f.get().getFurnitureName());
                    dto.setPrice(Double.valueOf(f.get().getFurniturePrice()));
                    dto.setImage(f.get().getFurniturePicture());
                    dto.setMaxStock(f.get().getQuantity()); // Assuming field is quantity
                }
            } else if ("HOMEDECO".equalsIgnoreCase(item.getProductType())) {
                Optional<HomeDeco> d = homedecoRepo.findById(item.getProductId());
                if (d.isPresent()) {
                    dto.setProductName(d.get().getDecoName());
                    dto.setPrice(Double.valueOf(d.get().getDecoPrice()));
                    dto.setImage(d.get().getDecoPicture());
                    dto.setMaxStock(d.get().getQuantity());
                }
            }
            responseList.add(dto);
        }
        return responseList;
    }

    // 3. Remove Item
    public String removeFromCart(Integer cartId) {
        if (cartRepository.existsById(cartId)) {
            cartRepository.deleteById(cartId);
            return VarList.RSP_SUCCESS;
        }
        return VarList.RSP_NO_DATA_FOUND;
    }
}