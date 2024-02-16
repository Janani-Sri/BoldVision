package com.boldvision.ocr.service;

import com.boldvision.ocr.config.DataAccessConstants;
import com.boldvision.ocr.dto.ResponseDto;
import com.boldvision.ocr.dto.SignInDto;
import com.boldvision.ocr.dto.SignInResponseDto;
import com.boldvision.ocr.dto.SignupDto;
import com.boldvision.ocr.exceptions.AuthenticationFailException;
import com.boldvision.ocr.exceptions.CustomException;
import com.boldvision.ocr.model.AuthenticationToken;
import com.boldvision.ocr.model.User;
import com.boldvision.ocr.repository.UserRepository;
import jakarta.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationService authenticationService;

    Logger logger = LoggerFactory.getLogger(UserService.class);
    public ResponseEntity<Map<String, Object>> signUp(SignupDto signupDto) throws CustomException {
        if(Objects.nonNull(userRepository.findByEmail(signupDto.getEmail()))){
            throw new CustomException("User already exists!!!");
        }
        String password = signupDto.getPassword();
        String encrypted_password="";
        try {
            encrypted_password = encryptPassword(password);
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            logger.error("Password encryption failed for user with email :: ", signupDto.getEmail());
        }

        User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(), encrypted_password);
        try {
            userRepository.save(user);
            final AuthenticationToken authenticationToken = new AuthenticationToken(user);
            authenticationService.saveAuthenticationToken(authenticationToken);
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("token", authenticationToken.getToken());
//            return new ResponseDto("success", "Signed up successfully!!!");
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e){
            throw new CustomException(e.getMessage());
        }
    }

    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    public ResponseEntity<Map<String, Object>> signIn(SignInDto signInDto) throws AuthenticationFailException, CustomException{
        User user = userRepository.findByEmail(signInDto.getEmail());
        if(!Objects.nonNull(user)){
            throw new CustomException("User not found!!");
        }
        try{
            logger.info("user password : ", user.getPassword());
            logger.info("signindto password: ", signInDto.getPassword());
            logger.info("signin dto encrypted password : ", encryptPassword(signInDto.getPassword()));
            if(!user.getPassword().equals(encryptPassword(signInDto.getPassword()))){
                throw new AuthenticationFailException(DataAccessConstants.WRONG_PASSWORD);
            }
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            logger.error("password encryption failed");
            throw new CustomException(e.getMessage());
        }
        AuthenticationToken authenticationToken = authenticationService.getToken(user);
        Map<String, Object> jsonResponse = new HashMap<>();

        if(!Objects.nonNull(authenticationToken)){
            throw new AuthenticationFailException(DataAccessConstants.AUTH_TOKEN_NOT_PRESENT);
        }
        jsonResponse.put("token", authenticationToken.getToken());
//        return new SignInResponseDto("success", authenticationToken.getToken());
        return ResponseEntity.ok(jsonResponse);
    }
}
