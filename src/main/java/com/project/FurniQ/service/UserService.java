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
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");

        User savedUser = userRepository.save(user);

        try {
            emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getUsername());
        } catch (Exception e) {
            // Log if the email dispatch fails (but don't stop the registration)
            System.err.println("Email service failed to dispatch: " + e.getMessage());
        }

        return savedUser;
    }



    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
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
}