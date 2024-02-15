package com.boldvision.ocr.service;

import com.boldvision.ocr.dto.ResponseDto;
import com.boldvision.ocr.dto.SignupDto;
import com.boldvision.ocr.exceptions.CustomException;
import com.boldvision.ocr.model.User;
import com.boldvision.ocr.repository.UserRepository;
import jakarta.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserService.class);
    public ResponseDto signUp(SignupDto signupDto) throws CustomException {
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
            return new ResponseDto("success", "Signed up successfully!!!");
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
}
