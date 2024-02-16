package com.boldvision.ocr.service;


import com.boldvision.ocr.config.DataAccessConstants;
import com.boldvision.ocr.exceptions.AuthenticationFailException;
import com.boldvision.ocr.model.AuthenticationToken;
import com.boldvision.ocr.model.User;
import com.boldvision.ocr.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationService {
    @Autowired
    TokenRepository repo;

    //save token
    public void saveAuthenticationToken(AuthenticationToken authenticationToken){
        repo.save(authenticationToken);
    }
    //get token from user
    public AuthenticationToken getToken(User user){
        return repo.findTokenByUser(user);
    }

    //get user from token
    public User getUser(String token){
        AuthenticationToken auth_token = repo.findTokenByToken(token);
        if(Objects.nonNull(auth_token)){
            if(Objects.nonNull(auth_token.getUser())){
                return auth_token.getUser();
            }
        }
        return null;
    }

    //validate token
    public void authenticate(String token) throws AuthenticationFailException{
        if(!Objects.nonNull(token)){
            throw new AuthenticationFailException(DataAccessConstants.AUTH_TOKEN_NOT_PRESENT);
        }
        if(!Objects.nonNull(getUser(token))){
            throw new AuthenticationFailException(DataAccessConstants.INVALID_AUTH_TOKEN);
        }
    }
}
