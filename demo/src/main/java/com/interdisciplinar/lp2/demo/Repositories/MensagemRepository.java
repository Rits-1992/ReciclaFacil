package com.interdisciplinar.lp2.demo.Repositories;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.interdisciplinar.lp2.demo.Entities.EntityMensagem;
import com.interdisciplinar.lp2.demo.Entities.StatusMensagem;

public interface MensagemRepository extends JpaRepository<EntityMensagem, Long> {
    List<EntityMensagem> findByUsuarioId(Long usuarioId);
    List<EntityMensagem> findByStatusOrderByDataEnvioDesc(StatusMensagem status);

    // Chama a function SQL Server que retorna DATETIME2
    @Query(value = "SELECT dbo.fn_get_ultimo_status_change(:id)", nativeQuery = true)
    Timestamp getUltimoStatusChangeTs(@Param("id") Long id);

    // Conveniência: converte para LocalDateTime (pode retornar null)
    default LocalDateTime getUltimoStatusChange(Long id) {
        Timestamp ts = getUltimoStatusChangeTs(id);
        return ts == null ? null : ts.toLocalDateTime();
    }
}
