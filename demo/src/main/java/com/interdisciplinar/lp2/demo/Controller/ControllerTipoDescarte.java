package com.interdisciplinar.lp2.demo.Controller;


import com.interdisciplinar.lp2.demo.DTO.tipodescarte.TipoDescarteAdminResponseDTO;
import com.interdisciplinar.lp2.demo.DTO.tipodescarte.TipoDescarteDTO;
import com.interdisciplinar.lp2.demo.DTO.tipodescarte.TipoDescarteResumoDTO;
import com.interdisciplinar.lp2.demo.Services.ServiceTipoDescarte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipoDescarte")
public class ControllerTipoDescarte {

    @Autowired
    private ServiceTipoDescarte service;

    // ============================================================
    // LISTAR TODOS (ADMIN)
    // ============================================================
    @GetMapping("/admin/listar")
    public ResponseEntity<List<TipoDescarteAdminResponseDTO>> listarAdmin() {
        return ResponseEntity.ok(service.listarTodosAdmin());
    }

    // ============================================================
    // LISTAGEM RESUMIDA (PUBLIC)
    // ============================================================
    @GetMapping("/public/listar")
    public ResponseEntity<List<TipoDescarteResumoDTO>> listarPublic() {
        return ResponseEntity.ok(service.listarResumo());
    }

    // ============================================================
    // BUSCAR POR ID (ADMIN/PUBLIC)
    // ============================================================
    @GetMapping("/{id}")
    public ResponseEntity<TipoDescarteDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

}