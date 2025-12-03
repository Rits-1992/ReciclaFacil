package com.interdisciplinar.lp2.demo.Services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.interdisciplinar.lp2.demo.DTO.usuario.UsuarioLoginDTO;
import com.interdisciplinar.lp2.demo.DTO.usuario.UsuarioRequestDTO;
import com.interdisciplinar.lp2.demo.DTO.usuario.UsuarioResponseDTO;
import com.interdisciplinar.lp2.demo.Entities.EntityUsuario;
import com.interdisciplinar.lp2.demo.Entities.TipoUsuario;
import com.interdisciplinar.lp2.demo.Mapper.MapperUsuario;
import com.interdisciplinar.lp2.demo.Repositories.RepositoryUsuario;

@Service
public class ServiceUsuario {

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    @Autowired
    private MapperUsuario mapperUsuario;


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /*
     * ============================================================
     *                  Cadastrar Usuário
     * ============================================================
    */
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {

        if (repositoryUsuario.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email já cadastrado no sistema");
        }

        EntityUsuario usuario = new EntityUsuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setSituacao(true);
        usuario.setDataCadastro(LocalDateTime.now());
        usuario.setTipoUsuario(TipoUsuario.USER);

        EntityUsuario salvo = repositoryUsuario.save(usuario);

        return mapperUsuario.toDTO(salvo);
    }

    /*
     * ============================================================
     *                          Login
     * ============================================================
    */
    public UsuarioResponseDTO login(UsuarioLoginDTO dto) {

        EntityUsuario usuario = repositoryUsuario.findByEmail(dto.email())
            .orElseThrow(() -> new IllegalArgumentException("Email ou senha incorretos"));

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new IllegalArgumentException("Email ou senha incorretos");
        }

        return mapperUsuario.toDTO(usuario);
    }

}