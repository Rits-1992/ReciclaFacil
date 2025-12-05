-- ===================================
-- SQL: Trigger para rastrear mudanças de status
-- ===================================
-- Execute no SSMS APÓS adicionar colunas à tabela mensagem

-- Passo 1: Adicionar colunas de auditoria (se não existirem)
-- Descomente se precisar adicionar as colunas:
/*
ALTER TABLE mensagem
ADD 
    status_anterior NVARCHAR(20) NULL,
    data_ultimo_status_change DATETIME2 NULL;
GO
*/

-- Passo 2: Criar trigger que registra mudanças de status
CREATE TRIGGER trg_mensagem_update_status
ON mensagem
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    -- Se o status mudou, registra o anterior e a data
    UPDATE m
    SET
        m.status_anterior = d.status_mensagem,
        m.data_ultimo_status_change = SYSUTCDATETIME()
    FROM mensagem m
    INNER JOIN deleted d ON m.id_mensagem = d.id_mensagem
    INNER JOIN inserted i ON m.id_mensagem = i.id_mensagem
    WHERE i.status_mensagem <> d.status_mensagem;
END;
GO
