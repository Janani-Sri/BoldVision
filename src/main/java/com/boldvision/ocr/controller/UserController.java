package com.boldvision.ocr.controller;

import com.boldvision.ocr.dto.ResponseDto;
import com.boldvision.ocr.dto.SignInDto;
import com.boldvision.ocr.dto.SignInResponseDto;
import com.boldvision.ocr.dto.SignupDto;
import com.boldvision.ocr.exceptions.AuthenticationFailException;
import com.boldvision.ocr.exceptions.CustomException;
import com.boldvision.ocr.model.User;
import com.boldvision.ocr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("user")
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody SignupDto signupDto) throws CustomException {
        return userService.signUp(signupDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<Map<String, Object>> signIn(@RequestBody SignInDto signInDto) throws CustomException, AuthenticationFailException{
        return userService.signIn(signInDto);
    }
}
