package com.interdisciplinar.lp2.demo.Controller;

import com.interdisciplinar.lp2.demo.DTO.localdescarte.LocalDescarteAdminResponseDTO;
import com.interdisciplinar.lp2.demo.DTO.localdescarte.LocalDescarteByMaterialDTO;
import com.interdisciplinar.lp2.demo.DTO.localdescarte.LocalDescarteCreateDTO;
import com.interdisciplinar.lp2.demo.DTO.localdescarte.LocalDescarteUpdateDTO;
import com.interdisciplinar.lp2.demo.DTO.localdescarte.LocalDescartePublicResponseDTO;
import com.interdisciplinar.lp2.demo.Services.ServiceLocalDescarte;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/localDescarte")
public class ControllerLocalDescarte {

    @Autowired
    private ServiceLocalDescarte serviceLocalDescarte;

    /* ============================================================
       CREATE (ADMIN)
    ============================================================ */
    @PostMapping("/admin")
    public ResponseEntity<LocalDescarteAdminResponseDTO> create(
            @Valid @RequestBody LocalDescarteCreateDTO dto) {

        LocalDescarteAdminResponseDTO response = serviceLocalDescarte.create(dto);
        return ResponseEntity.ok(response);
    }

    /* ============================================================
       UPDATE (ADMIN)
       ============================================================ */
    @PutMapping("/admin/{id}")
    public ResponseEntity<LocalDescarteAdminResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody LocalDescarteUpdateDTO dto) {

        return ResponseEntity.ok(serviceLocalDescarte.update(id, dto));
    }

    /* ============================================================
       DELETE (ADMIN)
       ============================================================ */
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        serviceLocalDescarte.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /* ============================================================
       LISTAR TODOS (ADMIN)
       ============================================================ */
    @GetMapping("/admin")
    public ResponseEntity<List<LocalDescarteAdminResponseDTO>> listarAdmin() {
        return ResponseEntity.ok(serviceLocalDescarte.listarAdmin());
    }

    /* ============================================================
       BUSCAR POR ID (ADMIN)
       ============================================================ */
    @GetMapping("/admin/{id}")
    public ResponseEntity<LocalDescarteAdminResponseDTO> buscarPorIdAdmin(
            @PathVariable Long id) {
        return ResponseEntity.ok(serviceLocalDescarte.buscarPorIdAdmin(id)); 
    }

    /* ============================================================
       LISTAR PARA USUÁRIOS COMUNS (apenas ativos)
       ============================================================ */
    @GetMapping("/public")
    public ResponseEntity<List<LocalDescartePublicResponseDTO>> listarPublic() {
        return ResponseEntity.ok(serviceLocalDescarte.listarAtivos());
    }

    /* ============================================================
       LISTAR POR MATERIAL (USUÁRIO COMUM)
       ============================================================ */
    @GetMapping("/public/material/{materialId}")
    public ResponseEntity<List<LocalDescarteByMaterialDTO>> listarPorMaterial(
            @PathVariable Long materialId) {
        return ResponseEntity.ok(serviceLocalDescarte.listarPorMaterial(materialId));
}
    @GetMapping("/public/sugestoes")
public ResponseEntity<List<String>> sugestoes(@RequestParam String cidade) {
    return ResponseEntity.ok(serviceLocalDescarte.sugestoesCidade(cidade));
}

@GetMapping("/public/buscar")
public ResponseEntity<List<LocalDescartePublicResponseDTO>> buscarPorCidade(@RequestParam String cidade) {
    return ResponseEntity.ok(serviceLocalDescarte.buscarPorCidadeFuncao(cidade));
}

}
