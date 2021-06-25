package com.cursor.library.controller;

import com.cursor.library.services.UserService;
import com.cursor.library.util.JWTUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> createAuthenticationToken(@RequestBody AuthenticationRequest auth) throws AccessDeniedException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword()));
        UserDetails userDetails = userService.login(auth.getUsername(), auth.getPassword());
        String generateToken = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(generateToken);
    }

    @Data
    public static class AuthenticationRequest implements Serializable {
        private String username;
        private String password;
    }

}
