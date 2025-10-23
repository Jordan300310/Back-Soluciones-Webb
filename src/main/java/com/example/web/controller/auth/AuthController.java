package com.example.web.controller.auth;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.web.dto.auth.LoginRequest;
import com.example.web.dto.auth.LoginResponse;
import com.example.web.dto.auth.RegisterRequest;
import com.example.web.dto.auth.RegisterResponse;
import com.example.web.models.auth.SessionUser;
import com.example.web.service.auth.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
    var resp = authService.registerCliente(request);
    return ResponseEntity.status(201).body(resp);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpSession session) {
    return ResponseEntity.ok(authService.login(request, session));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpSession session) {
    authService.logout(session);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/me")
  public ResponseEntity<SessionUser> me(HttpSession session) {
    return ResponseEntity.ok(authService.me(session));
  }
}
