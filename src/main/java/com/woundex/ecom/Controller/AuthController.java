package com.woundex.ecom.Controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.woundex.ecom.Service.CustomUserService;
import com.woundex.ecom.dto.AuthRequest;
import com.woundex.ecom.dto.AuthResponse;
import com.woundex.ecom.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.woundex.ecom.Entity.User;
import com.woundex.ecom.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Optional;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserService userDetailsService;
    private final JwtUtils jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        
        // Create a new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        
        // Save the user
        userRepository.save(user);
        
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<Optional<User>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userRepository.findByUsername(userDetails.getUsername()));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allusers")
    public ResponseEntity<java.util.List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
   
}
