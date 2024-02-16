package com.boldvision.ocr.exceptions;

import com.boldvision.ocr.model.AuthenticationToken;

public class AuthenticationFailException extends Exception{
    public AuthenticationFailException(String msg){
        super(msg);
    }
}
