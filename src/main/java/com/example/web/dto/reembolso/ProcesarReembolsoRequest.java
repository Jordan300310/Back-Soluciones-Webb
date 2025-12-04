package com.example.web.dto.reembolso;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcesarReembolsoRequest {
    private Boolean aprobar;
    private String comentario;
}
