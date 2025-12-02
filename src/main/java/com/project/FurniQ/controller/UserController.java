package com.project.FurniQ.controller;

import com.project.FurniQ.dto.EmailRequestDTO;
import com.project.FurniQ.dto.ResponseDTO;
import com.project.FurniQ.dto.userDTO;
import com.project.FurniQ.entity.User;
import com.project.FurniQ.service.EmailService;
import com.project.FurniQ.service.JwtService;
import com.project.FurniQ.service.UserService;
import com.project.FurniQ.util.VarList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final ResponseDTO responseDTO;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        User savedUser = userService.register(user);

        String token = jwtService.generateToken(savedUser.getEmail(), savedUser.getRole());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", savedUser.getRole(),
                "email", savedUser.getEmail(),
                "username", savedUser.getUsername(),
                "message","Registration successful!"
        ));

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        var userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty() || !userService.checkPassword(password, userOpt.get().getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }

        User user = userOpt.get();
        String token = jwtService.generateToken(user.getEmail(), user.getRole());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", user.getRole(),
                "email", user.getEmail(),
                "username", user.getUsername(),
                "message","User successfully logged in"
        ));
    }


    @PostMapping("/sendEmail")
    public ResponseEntity<?> sendEmailToUser(@RequestBody EmailRequestDTO emailRequest) {
//        find user
        Optional<User> userOpt = userService.findById(emailRequest.getUserId());


        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found with ID: " + emailRequest.getUserId()));
        }

        User user = userOpt.get();

        // Send the email
        emailService.sendCustomEmail(
                user.getEmail(),
                emailRequest.getSubject(),
                emailRequest.getMessageBody()
        );

        // Return  success response
        return ResponseEntity.ok(Map.of(
                "message", "Email successfully dispatched to " + user.getEmail()
        ));
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity getAllUsers() {
        try{
            List<userDTO> userDTOList = userService.getAllUsers();
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Success");
            responseDTO.setContent(userDTOList);
            return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PutMapping("/updateUserDetails")
    public ResponseEntity updateUserDetails(@RequestBody userDTO userDTO) {
        try{
            String res =  userService.updateUserDetails(userDTO);
            if(res.equals("00")){
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success");
                responseDTO.setContent(userDTO);
                return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
            } else if (res.equals("01")) {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("User Not Found");
                responseDTO.setContent(userDTO);
                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
            }else {
                responseDTO.setCode(VarList.RSP_ERROR);
                responseDTO.setMessage("Error");
                responseDTO.setContent(userDTO);
                return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getUser/{id}")
    public ResponseEntity getUser(@PathVariable("id") Integer userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            userDTO userDTO = userService.getUserById(userId);
            if (userDTO != null) {
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("Success");
                responseDTO.setContent(userDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
            } else {
                responseDTO.setCode(VarList.RSP_NO_DATA_FOUND);
                responseDTO.setMessage("User not found");
                responseDTO.setContent(null);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage(ex.getMessage());
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
