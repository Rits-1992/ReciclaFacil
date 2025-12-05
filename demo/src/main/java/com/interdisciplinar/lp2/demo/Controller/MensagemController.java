package com.interdisciplinar.lp2.demo.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.interdisciplinar.lp2.demo.DTO.mensagem.MensagemRequestDTO;
import com.interdisciplinar.lp2.demo.DTO.mensagem.MensagemResponseDTO;
import com.interdisciplinar.lp2.demo.Entities.StatusMensagem;
import com.interdisciplinar.lp2.demo.Entities.MensagensCompletoView;
import com.interdisciplinar.lp2.demo.Services.MensagemService;
import com.interdisciplinar.lp2.demo.Repositories.MensagemRepository;
import java.time.LocalDateTime;
import com.interdisciplinar.lp2.demo.Repositories.MensagensCompletoRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mensagens")
@RequiredArgsConstructor
public class MensagemController {

    private final MensagemService service;
    private final MensagensCompletoRepository mensagensCompletoRepository;
    private final MensagemRepository mensagemRepository;

    @PostMapping("/procedure")
    public ResponseEntity<MensagemResponseDTO> enviarProcedure(@RequestBody MensagemRequestDTO dto) {
        return ResponseEntity.ok(service.enviarMensagemViaProcedure(dto));
    }

    @PostMapping("/enviarProcedure")
    public ResponseEntity<MensagemResponseDTO> enviarViaProcedure(
            @RequestBody MensagemRequestDTO dto) {
        return ResponseEntity.ok(service.enviarMensagemViaProcedure(dto));
    }

    @GetMapping
    public ResponseEntity<List<MensagemResponseDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<MensagemResponseDTO>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(service.listarPorUsuario(idUsuario));
    }

    @PatchMapping("/{idMensagem}/status/{status}")
    public ResponseEntity<MensagemResponseDTO> alterarStatus(
            @PathVariable Long idMensagem,
            @PathVariable StatusMensagem status) {

        return ResponseEntity.ok(service.alterarStatus(idMensagem, status));
    }

    @GetMapping("/view")
    public ResponseEntity<List<MensagensCompletoView>> listarMensagensView() {
        return ResponseEntity.ok(mensagensCompletoRepository.findAll());
    }

    @GetMapping("/{id}/ultimo-status-change")
    public ResponseEntity<String> getUltimoStatusChange(@PathVariable("id") Long id) {
        LocalDateTime dt = mensagemRepository.getUltimoStatusChange(id);
        if (dt == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dt.toString());
    }
}