package com.example.web.models.auth;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class SessionUser implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  private Long idUsuario;
  private String username;
  private List<String> roles; // ["CLIENTE"], ["EMPLEADO"] o ambos
  private boolean enabled;
}