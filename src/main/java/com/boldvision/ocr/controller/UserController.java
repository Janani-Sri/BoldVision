package com.boldvision.ocr.controller;

import com.boldvision.ocr.dto.ResponseDto;
import com.boldvision.ocr.dto.SignupDto;
import com.boldvision.ocr.exceptions.CustomException;
import com.boldvision.ocr.model.User;
import com.boldvision.ocr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("user")
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseDto Signup(@RequestBody SignupDto signupDto) throws CustomException {
        return userService.signUp(signupDto);
    }
}
