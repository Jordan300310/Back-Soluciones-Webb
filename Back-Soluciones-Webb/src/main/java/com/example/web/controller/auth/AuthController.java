package com.example.web.controller.auth;

import com.example.web.dto.auth.LoginRequest;
import com.example.web.dto.auth.LoginResponse;
import com.example.web.dto.auth.MeResponse;
import com.example.web.dto.auth.RegisterRequest;
import com.example.web.dto.auth.RegisterResponse;
import com.example.web.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout() {
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/me")
  public ResponseEntity<MeResponse> me(
      @RequestHeader(name = "Authorization", required = false) String authHeader) {
    var me = authService.me(authHeader);
    return ResponseEntity.ok(me);
  }
}
