//package com.healthy.BackEnd.Service;
//
//import com.healthy.BackEnd.Interface.AuthenticationInterface;
//import com.healthy.BackEnd.dto.auth.AuthenticationRequest;
//import com.healthy.BackEnd.repository.AuthenticationRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthenticationService implements AuthenticationInterface {
//
//    @Autowired
//    private AuthenticationRepository authenticationRepository;
//
//    @Override
//    public AuthenticationResponse authenticate(AuthenticationRequest request, HttpSession session) {
//        return null;
//    }
//
//    @Override
//    public String validateToken(String theToken) {
//        return "";
//    }
//
//    @Override
//    public VerificationToken getToken(String token) {
//        return null;
//    }
//}