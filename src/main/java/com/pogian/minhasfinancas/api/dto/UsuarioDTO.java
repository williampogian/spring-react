package com.pogian.minhasfinancas.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private String nome;
    private String email;
    private String senha;
}
