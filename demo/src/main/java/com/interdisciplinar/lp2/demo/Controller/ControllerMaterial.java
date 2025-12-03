package com.interdisciplinar.lp2.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.interdisciplinar.lp2.demo.DTO.material.MaterialAdminResponseDTO;
import com.interdisciplinar.lp2.demo.DTO.material.MaterialPublicResponseDTO;
import com.interdisciplinar.lp2.demo.DTO.material.MaterialRequestDTO;
import com.interdisciplinar.lp2.demo.DTO.material.MaterialUpdateDTO;
import com.interdisciplinar.lp2.demo.DTO.material.ResultadoPesquisaDTO;
import com.interdisciplinar.lp2.demo.Services.ServiceMaterial;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/materiais")
public class ControllerMaterial {

    @Autowired
    private ServiceMaterial serviceMaterial;

    // =========================================================
    // PUBLIC — APENAS MATERIAIS ATIVOS
    // =========================================================

    @GetMapping("/public/listar")
    public ResponseEntity<List<MaterialPublicResponseDTO>> listarPublico() {
        return ResponseEntity.ok(serviceMaterial.listarPublico());
    }

    @GetMapping("/public/buscar")
    public ResponseEntity<List<MaterialPublicResponseDTO>> buscarPorNomePublic(
            @RequestParam String nome) {
        return ResponseEntity.ok(serviceMaterial.buscarPorNomePublic(nome));
    }

    // 🔥 ANTIGA ROTA COM CONFLITO: "/{idMaterial}"
    // AGORA FICA ISOLADA DENTRO DE /public
    @GetMapping("/public/detalhar/{idMaterial}")
    public ResultadoPesquisaDTO detalhar(
            @PathVariable Long idMaterial,
            @RequestParam(required = false) Long usuarioId) {

        return serviceMaterial.pesquisar(idMaterial, usuarioId);
    }


    // =========================================================
    // ADMIN — CRUD COMPLETO SEM TOKEN
    // =========================================================

    // CREATE
    @PostMapping("/admin/cadastrar")
    public ResponseEntity<MaterialAdminResponseDTO> cadastrar(
            @Valid @RequestBody MaterialRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceMaterial.cadastrar(dto));
    }

    // UPDATE
    @PutMapping("/admin/{id}")
    public ResponseEntity<MaterialAdminResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MaterialUpdateDTO dto) {
        return ResponseEntity.ok(serviceMaterial.atualizar(id, dto));
    }

    // LISTAR TODOS
    @GetMapping("/admin/listar")
    public ResponseEntity<List<MaterialAdminResponseDTO>> listarAdmin() {
        return ResponseEntity.ok(serviceMaterial.listarAdmin());
    }

    // BUSCAR POR NOME ADMIN
    @GetMapping("/admin/buscar")
    public ResponseEntity<List<MaterialAdminResponseDTO>> buscarPorNomeAdmin(
            @RequestParam String nome) {
        return ResponseEntity.ok(serviceMaterial.buscarPorNomeAdmin(nome));
    }

    // DELETE
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        serviceMaterial.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // BUSCAR POR ID ADMIN
    @GetMapping("/admin/{id}")
    public ResponseEntity<MaterialAdminResponseDTO> buscarPorIdAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(serviceMaterial.buscarPorIdAdmin(id));
    }

}