package com.interdisciplinar.lp2.demo.Services;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.interdisciplinar.lp2.demo.DTO.localdescarte.LocalDescarteAdminResponseDTO;
import com.interdisciplinar.lp2.demo.DTO.localdescarte.LocalDescarteByMaterialDTO;
import com.interdisciplinar.lp2.demo.DTO.localdescarte.LocalDescarteCreateDTO;
import com.interdisciplinar.lp2.demo.DTO.localdescarte.LocalDescartePublicResponseDTO;
import com.interdisciplinar.lp2.demo.DTO.localdescarte.LocalDescarteUpdateDTO;
import com.interdisciplinar.lp2.demo.Entities.EntityEndereco;
import com.interdisciplinar.lp2.demo.Entities.EntityLocalDescarte;
import com.interdisciplinar.lp2.demo.Entities.EntityMaterial;
import com.interdisciplinar.lp2.demo.Entities.EntityTipoDescarte;
import com.interdisciplinar.lp2.demo.Mapper.MapperLocalDescarte;
import com.interdisciplinar.lp2.demo.Repositories.RepositoryLocalDescarte;
import com.interdisciplinar.lp2.demo.Repositories.RepositoryMaterial;
import com.interdisciplinar.lp2.demo.Repositories.RepositoryTipoDescarte;

@Service
public class ServiceLocalDescarte {

        @Autowired
        private RepositoryLocalDescarte localRepo;
        @Autowired
        private RepositoryTipoDescarte tipoRepo;
        @Autowired
        private MapperLocalDescarte mapperLocalDescarte;
        @Autowired
        private RepositoryMaterial materialRepository;
        @Autowired
        private JdbcTemplate jdbcTemplate;


        /*============================================================
         * CREATE — ADMIN
         * =========================================================*/
        @Transactional
        public LocalDescarteAdminResponseDTO create(LocalDescarteCreateDTO dto) {

        // Buscar tipos
        Set<EntityTipoDescarte> tipos = tipoRepo.findAllById(dto.tiposDescarteIds())
                .stream().collect(Collectors.toSet());

                // Criar Local Descarte
                EntityLocalDescarte local = new EntityLocalDescarte();
                local.setNome(dto.nome());
                local.setHorarioAbertura(dto.horarioAbertura());
                local.setHorarioFechamento(dto.horarioFechamento());
                local.setContato(dto.contato());
                local.setEmail(dto.email());
                local.setDescricao(dto.descricao());
                local.setSituacao(true);
                local.setTiposDescarte(tipos);

                // Criar Endereço
                EntityEndereco endereco = new EntityEndereco();
                endereco.setRua(dto.endereco().rua());
                endereco.setNumero(dto.endereco().numero());
                endereco.setComplemento(dto.endereco().complemento());
                endereco.setBairro(dto.endereco().bairro());
                endereco.setCidade(dto.endereco().cidade());
                endereco.setEstado(dto.endereco().estado());
                endereco.setCep(dto.endereco().cep());

                // associar bidirecionalmente
                endereco.setLocalDescarte(local);
                local.setEndereco(endereco);

                // salvar — cascade garante que endereço é salvo junto
                try {
                    localRepo.save(local);
                } catch (Exception e) {
                    String errorMsg = e.getMessage();
                    if (e.getCause() != null) {
                        errorMsg = e.getCause().getMessage();
                    }
                    throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, 
                        "Erro ao salvar local de descarte: " + errorMsg, 
                        e
                    );
                }

                return mapperLocalDescarte.toAdminDTO(local);
        }

        /*============================================================
         * UPDATE — ADMIN
         * =========================================================*/
        @Transactional
        public LocalDescarteAdminResponseDTO update(Long id, LocalDescarteUpdateDTO dto) {

                EntityLocalDescarte local = localRepo.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Local não encontrado"));

                // Atualiza dados básicos (somente se não forem nulos no DTO)
                if (dto.nome() != null) local.setNome(dto.nome());
                if (dto.horarioAbertura() != null) local.setHorarioAbertura(dto.horarioAbertura());
                if (dto.horarioFechamento() != null) local.setHorarioFechamento(dto.horarioFechamento());
                if (dto.contato() != null) local.setContato(dto.contato());
                if (dto.email() != null) local.setEmail(dto.email());
                if (dto.descricao() != null) local.setDescricao(dto.descricao());
                if (dto.situacao() != null) local.setSituacao(dto.situacao());

                /*============================
                * ATUALIZA ENDEREÇO
                * ============================
                * Trata caso em que o DTO não contenha endereço (não atualiza)
                */
                LocalDescarteUpdateDTO.EnderecoDTO e = dto.endereco();
                if (e != null) {
                        EntityEndereco end = local.getEndereco();
                        if (end == null) {
                                end = new EntityEndereco();
                                end.setLocalDescarte(local);
                                local.setEndereco(end);
                        }
                        if (e.rua() != null) end.setRua(e.rua());
                        if (e.numero() != null) end.setNumero(e.numero());
                        if (e.complemento() != null) end.setComplemento(e.complemento());
                        if (e.bairro() != null) end.setBairro(e.bairro());
                        if (e.cidade() != null) end.setCidade(e.cidade());
                        if (e.estado() != null) end.setEstado(e.estado());
                        if (e.cep() != null) end.setCep(e.cep());
                }

                /*============================
                * ATUALIZA TIPOS DE DESCARTE
                *===========================*/
                if (dto.tiposDescarte() != null) {
                        Set<EntityTipoDescarte> tipos = dto.tiposDescarte().stream()
                                .map(t -> tipoRepo.findById(t.id())
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo não encontrado: " + t.id())))
                                .collect(Collectors.toSet());

                        local.setTiposDescarte(tipos);
                }

                // Salva o LocalDescarte (isso já salva o endereço por cascade)
                localRepo.save(local);

                return mapperLocalDescarte.toAdminDTO(local);
        }



        /*============================================================
         * LISTAR — USUÁRIOS (somente ativos)
         * =========================================================*/
        public List<LocalDescartePublicResponseDTO> listarAtivos() {
                return localRepo.findBySituacaoTrue()
                                .stream()
                                .map(local -> new LocalDescartePublicResponseDTO(
                                                local.getNome(),
                                                local.getHorarioAbertura(),
                                                local.getHorarioFechamento(),
                                                local.getContato(),
                                                local.getDescricao(),
                                                local.getEndereco().toString(), null))
                                .collect(Collectors.toList());
        }

        /*============================================================
         * LISTAR — ADMIN (apenas pontos ativos)
         * =========================================================*/
        public List<LocalDescarteAdminResponseDTO> listarAdmin() {
                return localRepo.findBySituacaoTrue()
                                .stream()
                                .map(local -> new LocalDescarteAdminResponseDTO(
                                                local.getId(),
                                                local.getNome(),
                                                local.getHorarioAbertura(),
                                                local.getHorarioFechamento(),
                                                local.getContato(),
                                                local.getEmail(),
                                                local.getDescricao(),
                                                local.isSituacao(),
                                                // montar endereço
                                                new LocalDescarteAdminResponseDTO.EnderecoDTO(
                                                                local.getEndereco().getRua(),
                                                                local.getEndereco().getNumero(),
                                                                local.getEndereco().getComplemento(),
                                                                local.getEndereco().getBairro(),
                                                                local.getEndereco().getCidade(),
                                                                local.getEndereco().getEstado(),
                                                                local.getEndereco().getCep()),
                                                // montar tipos de descarte
                                                local.getTiposDescarte()
                                                                .stream()
                                                                .map(tipo -> new LocalDescarteAdminResponseDTO.TipoDescarteDTO(
                                                                                tipo.getId(), tipo.getNome()))
                                                                .collect(Collectors.toSet())))
                                .collect(Collectors.toList());
        }

        /*============================================================
         * LISTAR — por Material (usuário comum)
         *==========================================================*/
        public List<LocalDescarteByMaterialDTO> listarPorMaterial(Long materialId) {

                // 1. Buscar o Material → pegamos o tipo dele
                EntityMaterial material = materialRepository.findById(materialId)
                        .orElseThrow(() -> new IllegalArgumentException("Material não encontrado"));

                Long tipoId = material.getTipoDescarte().getId();

                // 2. Buscar locais que aceitam esse tipo
                return localRepo.findByTiposDescarte_Id(tipoId)
                        .stream()
                        .filter(EntityLocalDescarte::isSituacao)
                        .map(local -> new LocalDescarteByMaterialDTO(
                                local.getNome(),
                                local.getHorarioAbertura(),
                                local.getHorarioFechamento(),
                                local.getContato(),
                                local.getDescricao(),
                                local.getEndereco().toString()
                        ))
                        .collect(Collectors.toList());
        }

        /*============================================================
         * BUSCAR POR ID — ADMIN
         * =========================================================*/
        public LocalDescarteAdminResponseDTO buscarPorIdAdmin(Long id) {
                EntityLocalDescarte local = localRepo.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Local de descarte não encontrado"));

                return mapperLocalDescarte.toAdminDTO(local);
        }

        /*============================================================
         * DELETE — ADMIN (Soft-Delete)
         * Ao invés de deletar fisicamente, marca o registro como inativo.
         * O front-end retorna apenas pontos ativos, então o ponto
         * desaparecerá da tela, mas os dados ficarão preservados no banco.
         * =========================================================*/
        @Transactional
        public void deletar(Long id) {
                // Verifica se o local existe
                if (!localRepo.existsById(id)) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Local não encontrado.");
                }
                // Soft-delete via query nativa (evita validações JPA)
                localRepo.softDeleteById(id);
        }

        /*============================================================
         * Buscar sugestões de cidades — USUÁRIO COMUM
         * =========================================================*/

        public List<String> sugestoesCidade(String cidade) {
                return localRepo.findBySituacaoTrue().stream()
                                .map(l -> l.getEndereco().getCidade())
                                .filter(c -> c.toLowerCase().contains(cidade.toLowerCase()))
                                .distinct()
                                .toList();
        }
        /**
         * Busca locais usando a função SQL dbo.fn_buscar_locais_por_cidade
         * @param cidade cidade (trecho) a buscar
         * @return lista de DTOs públicos
         */
        public List<LocalDescartePublicResponseDTO> buscarPorCidadeFuncao(String cidade) {
                return buscarPorCidadeFuncao(cidade, 100);
        }

        public List<LocalDescartePublicResponseDTO> buscarPorCidadeFuncao(String cidade, int limit) {
                final String sql = "SELECT * FROM dbo.fn_buscar_locais_por_cidade(?, ?)";
                return jdbcTemplate.query(sql, new Object[]{cidade, limit}, (rs, rowNum) -> {
                        String nome = rs.getString("nome_local_descarte");
                        Time t1 = rs.getTime("horario_abertura");
                        Time t2 = rs.getTime("horario_fechamento");
                        LocalTime horarioAbertura = t1 == null ? null : t1.toLocalTime();
                        LocalTime horarioFechamento = t2 == null ? null : t2.toLocalTime();
                        String contato = rs.getString("contato_whatsapp");
                        String email = rs.getString("email");
                        String descricao = rs.getString("descricao");
                        String rua = rs.getString("rua");
                        String numero = rs.getString("numero") != null ? rs.getString("numero") : "";
                        String bairro = rs.getString("bairro");
                        String cidadeRes = rs.getString("cidade");
                        String estado = rs.getString("estado");
                        String cep = rs.getString("cep");

                        return new LocalDescartePublicResponseDTO(
                                        nome,
                                        horarioAbertura,
                                        horarioFechamento,
                                        contato,
                                        email,
                                        descricao,
                                        new LocalDescartePublicResponseDTO.EnderecoDTO(rua, numero, bairro, cidadeRes, estado, cep)
                        );
                });
        }

}