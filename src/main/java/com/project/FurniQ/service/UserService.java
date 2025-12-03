package com.project.FurniQ.service;

import com.project.FurniQ.dto.userDTO;
import com.project.FurniQ.entity.User;
import com.project.FurniQ.repository.userRepository;
import com.project.FurniQ.util.VarList;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {


    private final userRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    //user registration
    public User register(User user) {
        // 1. Check if user already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        // 2. Encrypt Password & Set Role
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        // 3. Save to Database
        User savedUser = userRepository.save(user);

        // 4. Send Email (Wrapped in Try-Catch)
        // This prevents the app from crashing if the internet blocks the email port
        try {
            emailService.sendCustomEmail(
                    savedUser.getEmail(),
                    "Welcome to FurniQ",
                    "Your account has been created successfully!"
            );
        } catch (Exception e) {
            // Log the error but DO NOT stop the registration process
            System.out.println("WARNING: Email could not be sent due to network restriction.");
            System.out.println("Error: " + e.getMessage());
            System.out.println("User registered successfully anyway.");
        }

        return savedUser;
    }



    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public boolean checkPassword(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }


    //display username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        var authority = new SimpleGrantedAuthority(u.getRole());
        return new org.springframework.security.core.userdetails.User(u.getEmail(), u.getPassword(), List.of(authority));
    }


    //get all user details
    public List<userDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        Type listType = new TypeToken<List<userDTO>>() {}.getType();
        return modelMapper.map(users, listType);
    }

    //update user details
    public String updateUserDetails(userDTO userDTO) {
        Optional<User> userOpt = userRepository.findById(userDTO.getId());
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            // Update only allowed fields
            user.setUsername(userDTO.getUsername());
//            user.setEmail(userDTO.getEmail());
            user.setMobileNumber(userDTO.getMobileNumber());
            user.setRole(userDTO.getRole());

            userRepository.save(user);
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_NO_DATA_FOUND;
        }
    }

    // getUserById
    public userDTO getUserById(Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            userDTO dto = new userDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setMobileNumber(user.getMobileNumber());
            dto.setRole(user.getRole());

            return dto;
        } else {
            return null;
        }
    }
}
