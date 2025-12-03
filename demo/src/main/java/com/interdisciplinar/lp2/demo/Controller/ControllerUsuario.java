package com.interdisciplinar.lp2.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interdisciplinar.lp2.demo.DTO.usuario.UsuarioLoginDTO;
import com.interdisciplinar.lp2.demo.DTO.usuario.UsuarioRequestDTO;
import com.interdisciplinar.lp2.demo.DTO.usuario.UsuarioResponseDTO;
import com.interdisciplinar.lp2.demo.Services.ServiceUsuario;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@Validated
public class ControllerUsuario {

    @Autowired
    private ServiceUsuario serviceUsuario;

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponseDTO> cadastrar(
            @Valid @RequestBody UsuarioRequestDTO dto) {

        UsuarioResponseDTO response = serviceUsuario.cadastrar(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UsuarioLoginDTO dto) {

        try {
            UsuarioResponseDTO usuario = serviceUsuario.login(dto);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
