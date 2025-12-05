package com.interdisciplinar.lp2.demo.Services;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interdisciplinar.lp2.demo.DTO.mensagem.MensagemRequestDTO;
import com.interdisciplinar.lp2.demo.DTO.mensagem.MensagemResponseDTO;
import com.interdisciplinar.lp2.demo.Entities.*;
import com.interdisciplinar.lp2.demo.Mapper.MensagemMapper;
import com.interdisciplinar.lp2.demo.Repositories.MensagemRepository;
import com.interdisciplinar.lp2.demo.Repositories.RepositoryUsuario;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MensagemService {

    private final MensagemRepository repository;
    private final RepositoryUsuario usuarioRepository;
    private final MensagemMapper mapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public MensagemResponseDTO enviarMensagemViaProcedure(MensagemRequestDTO dto) {

        EntityUsuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Chamar a procedure via EntityManager
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_enviarMensagem");
        query.registerStoredProcedureParameter("p_usuario_origem", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_titulo", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_conteudo", String.class, ParameterMode.IN);

        query.setParameter("p_usuario_origem", dto.getUsuarioId());
        query.setParameter("p_titulo", dto.getTitulo());
        query.setParameter("p_conteudo", dto.getConteudo());

        query.execute();

        // A procedure não retorna id diretamente; precisamos buscar a última mensagem do usuário
        EntityMensagem msg = repository.findByUsuarioId(dto.getUsuarioId())
                .stream()
                .max((a, b) -> a.getDataEnvio().compareTo(b.getDataEnvio()))
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar mensagem criada"));

        return mapper.toResponse(msg);
    }

    @Transactional
    public MensagemResponseDTO enviarMensagem(MensagemRequestDTO dto) {

        EntityUsuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        EntityMensagem msg = mapper.toEntity(dto);
        msg.setUsuario(usuario);

        repository.save(msg);

        return mapper.toResponse(msg);
    }

    public List<MensagemResponseDTO> listarTodas() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<MensagemResponseDTO> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public MensagemResponseDTO alterarStatus(Long idMensagem, StatusMensagem status) {
        EntityMensagem msg = repository.findById(idMensagem)
                .orElseThrow(() -> new IllegalArgumentException("Mensagem não encontrada"));

        msg.setStatus(status);
        return mapper.toResponse(repository.save(msg));
    }
}