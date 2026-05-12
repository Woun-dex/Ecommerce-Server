package com.woundex.ecom.Controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.woundex.ecom.Service.CustomUserService;
import com.woundex.ecom.dto.AuthRequest;
import com.woundex.ecom.dto.AuthResponse;
import com.woundex.ecom.utils.JwtUtils;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserService userDetailsService;
    private final JwtUtils jwtService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        request.getUsername());

        String token = jwtService.generateToken(userDetails.getUsername(), userDetails.getAuthorities().toString());

        return new AuthResponse(token);
    }
    
   
}
