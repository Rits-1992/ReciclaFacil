package com.interdisciplinar.lp2.demo.Services;
import java.util.List;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.interdisciplinar.lp2.demo.DTO.material.MaterialAdminResponseDTO;
import com.interdisciplinar.lp2.demo.DTO.material.MaterialPublicResponseDTO;
import com.interdisciplinar.lp2.demo.DTO.material.MaterialRequestDTO;
import com.interdisciplinar.lp2.demo.DTO.material.MaterialUpdateDTO;
import com.interdisciplinar.lp2.demo.DTO.material.ResultadoPesquisaDTO;
import com.interdisciplinar.lp2.demo.Entities.EntityLogPesquisa;
import com.interdisciplinar.lp2.demo.Entities.EntityMaterial;
import com.interdisciplinar.lp2.demo.Entities.EntityUsuario;
import com.interdisciplinar.lp2.demo.Mapper.MapperMaterial;
import com.interdisciplinar.lp2.demo.Repositories.RepositoryLocalDescarte;
import com.interdisciplinar.lp2.demo.Repositories.RepositoryLogPesquisa;
import com.interdisciplinar.lp2.demo.Repositories.RepositoryMaterial;
import com.interdisciplinar.lp2.demo.Repositories.RepositoryTipoDescarte;
import com.interdisciplinar.lp2.demo.Repositories.RepositoryUsuario;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ServiceMaterial {

    @Autowired
    private RepositoryMaterial repositoryMaterial;

    @Autowired
    private RepositoryLocalDescarte repositoryLocalDescarte;

    @Autowired
    private RepositoryTipoDescarte repositoryTipoDescarte;

    @Autowired
    private RepositoryLogPesquisa repositoryLogPesquisa;

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    @Autowired
    private MapperMaterial mapperMaterial;

    /* ============================================================
       LISTAGEM PÚBLICA — USUÁRIO COMUM
    ============================================================ */
    public List<MaterialPublicResponseDTO> listarPublico() {
        return repositoryMaterial.findAll()
                .stream()
                .map(mapperMaterial::toPublicDTO)
                .toList();
    }


    /* ============================================================
       BUSCAR POR NOME — USUÁRIO COMUM
    ============================================================ */
    public List<MaterialPublicResponseDTO> buscarPorNomePublic(String nome) {
        return repositoryMaterial.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapperMaterial::toPublicDTO)
                .toList();
    }

    /* ============================================================
       Gerardor de Log de Pesquisa
    ============================================================ */
    public ResultadoPesquisaDTO pesquisar(Long idMaterial, Long idUsuario) {

        EntityMaterial material = repositoryMaterial.findById(idMaterial)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"));

        EntityUsuario usuario = null;
        if (idUsuario != null) {
            usuario = repositoryUsuario.findById(idUsuario).orElse(null);
        }

        // REGISTRO DO LOG AQUI
        repositoryLogPesquisa.save(new EntityLogPesquisa(usuario, material));

        // retorna o resultado da pesquisa (DTO)
        return new ResultadoPesquisaDTO(material);
    }

    /* ============================================================
    CREATE — ADMIN
    ============================================================ */
    @Transactional
    public MaterialAdminResponseDTO cadastrar(MaterialRequestDTO dto) {

        EntityMaterial material = new EntityMaterial();
        material.setNome(dto.nome());
        material.setDescricao(dto.descricao());
        material.setReciclavel(dto.reciclavel());

        // Tipo de descarte (apenas 1)
        var tipo = repositoryTipoDescarte.findById(dto.idTipoDescarte())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de descarte não encontrado."));
        material.setTipoDescarte(tipo);

        // Locais de descarte vinculados (continua ManyToMany)
        if (dto.idLocaisDescarte() == null) {
            material.setLocaisDescarte(new HashSet<>());
        } else {
            var locais = repositoryLocalDescarte.findAllById(dto.idLocaisDescarte());
            if (locais.size() != dto.idLocaisDescarte().size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Um ou mais locais de descarte não foram encontrados.");
            }
            material.setLocaisDescarte(new HashSet<>(locais));
        }

        repositoryMaterial.save(material);

        return mapperMaterial.toAdminDTO(material);
    }

    /* ============================================================
       BUSCAR POR NOME — ADMIN
    ============================================================ */
    public List<MaterialAdminResponseDTO> buscarPorNomeAdmin(String nome) {
        return repositoryMaterial.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapperMaterial::toAdminDTO)
                .toList();
    }

    /* ============================================================
       LISTAGEM COMPLETA — ADMIN
    ============================================================ */
    public List<MaterialAdminResponseDTO> listarAdmin() {
        return repositoryMaterial.findAll()
                .stream()
                .map(mapperMaterial::toAdminDTO)
                .toList();
    }

    /* ============================================================
       UPDATE — ADMIN
    ============================================================ */
    @Transactional
    public MaterialAdminResponseDTO atualizar(Long id, MaterialUpdateDTO dto) {

        EntityMaterial material = repositoryMaterial.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"));

        material.setNome(dto.nome());
        material.setDescricao(dto.descricao());
        material.setReciclavel(dto.reciclavel());

        // Tipo de descarte (apenas 1)
        var tipo = repositoryTipoDescarte.findById(dto.idTipoDescarte())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de descarte não encontrado."));
        material.setTipoDescarte(tipo);

        // Atualiza os locais
        if (dto.idLocaisDescarte() == null) {
            material.setLocaisDescarte(new HashSet<>());
        } else {
            var locais = repositoryLocalDescarte.findAllById(dto.idLocaisDescarte());
            if (locais.size() != dto.idLocaisDescarte().size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Um ou mais locais de descarte não foram encontrados.");
            }
            material.setLocaisDescarte(new HashSet<>(locais));
        }

        repositoryMaterial.save(material);

        return mapperMaterial.toAdminDTO(material);
    }

    /* ============================================================
       DELETE — ADMIN
       ============================================================ */
    @Transactional
    public void deletar(Long id) {
        if (!repositoryMaterial.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado.");
        }
        repositoryMaterial.deleteById(id);
    }
    
    // Buscar material completo para ADMIN (usa repositoryMaterial e mapperMaterial
    // já existentes)
    public MaterialAdminResponseDTO buscarPorIdAdmin(Long id) {
        var material = repositoryMaterial.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"));
        return mapperMaterial.toAdminDTO(material);
    }

}