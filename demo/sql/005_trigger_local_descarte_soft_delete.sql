-- ========================================================================
-- TRIGGER: SOFT-DELETE para tabela local_descarte
-- ========================================================================
-- OBJETIVO:
--   Quando alguém tenta deletar um ponto de coleta (local_descarte),
--   a trigger IMPEDE a exclusão física e apenas marca o registro como
--   inativo, alterando a coluna 'situacao_local_descarte' para 0 (false).
--   Assim, o dado fica preservado no banco para histórico/auditoria,
--   mas não aparece mais no front-end (que filtra por situacao_local_descarte = 1).
--
-- TABELA: local_descarte
-- COLUNA-CHAVE: id_local_descarte (chave primária)
-- COLUNA-STATUS: situacao_local_descarte (0 = inativo/deletado, 1 = ativo)
--
-- EVENTO ACIONADOR: INSTEAD OF DELETE
--   A trigger substitui a ação DELETE por um UPDATE que desativa o registro.
--
-- COMO APLICAR:
--   1. Conecte-se ao banco BdReciclaFacil no SSMS
--   2. Copie e execute este arquivo (ou F5 para selecionar todo e executar)
--   3. Confirme que a trigger foi criada sem erros
--
-- COMO REVERTER (se necessário):
--   DROP TRIGGER dbo.trg_local_descarte_soft_delete;
--
-- ========================================================================

USE BdReciclaFacil;
GO

-- Remover trigger antiga se existir (permite recriar sem conflito)
IF OBJECT_ID('dbo.trg_local_descarte_soft_delete', 'TR') IS NOT NULL
    DROP TRIGGER dbo.trg_local_descarte_soft_delete;
GO

-- Criar a trigger
CREATE TRIGGER dbo.trg_local_descarte_soft_delete
ON dbo.local_descarte
INSTEAD OF DELETE
AS
BEGIN
    SET NOCOUNT ON;  -- Evita mensagem "N rows affected"

    -- ================================================================
    -- LÓGICA: Ao invés de deletar, marcamos como inativo (situacao = 0)
    -- ================================================================
    -- Tabela 'deleted' contém os registros que seriam removidos
    -- Executamos um UPDATE em local_descarte para os IDs em 'deleted'
    -- Definindo situacao_local_descarte = 0
    
    UPDATE ld
    SET ld.situacao_local_descarte = 0
    FROM dbo.local_descarte ld
    INNER JOIN deleted d ON ld.id_local_descarte = d.id_local_descarte;

    -- Log/notificação (opcional - descomente se quiser registrar em histórico)
    -- INSERT INTO auditoria_local_descarte (id_local, acao, data_acao)
    -- SELECT d.id_local_descarte, 'SOFT_DELETE', GETDATE()
    -- FROM deleted d;

END;
GO

-- ========================================================================
-- VERIFICAÇÃO: Confirmar que a trigger foi criada com sucesso
-- ========================================================================
-- Execute os comandos abaixo para validar:

-- 1. Verificar que a trigger existe no banco
SELECT 'Trigger criada com sucesso!' AS status
WHERE OBJECT_ID('dbo.trg_local_descarte_soft_delete', 'TR') IS NOT NULL;

-- 2. Ver a definição da trigger
SELECT OBJECT_DEFINITION(OBJECT_ID('dbo.trg_local_descarte_soft_delete')) AS DefinicaoTrigger;

-- 3. Listar todas as triggers da tabela local_descarte
SELECT 
    name AS NomeTrigger,
    object_id,
    create_date AS DataCriacao,
    modify_date AS DataUltimaAlteracao
FROM sys.triggers
WHERE parent_id = OBJECT_ID('dbo.local_descarte')
ORDER BY name;

-- ========================================================================
-- TESTE: Como testar o comportamento da trigger
-- ========================================================================
-- Execute o bloco abaixo para testar SEM salvar alterações permanentes:
-- (Usar ROLLBACK ao final desfaz tudo)

/*
USE BdReciclaFacil;
GO

BEGIN TRAN;  -- Inicia transação

-- Encontre um ID válido na tabela (substitua 1 por um ID real)
DELETE FROM dbo.local_descarte 
WHERE id_local_descarte = 1;

-- Verificar: o registro deve estar com situacao_local_descarte = 0
SELECT 
    id_local_descarte,
    nome_local_descarte,
    situacao_local_descarte,
    CASE 
        WHEN situacao_local_descarte = 1 THEN 'Ativo'
        WHEN situacao_local_descarte = 0 THEN 'Inativo (Soft-Delete)'
        ELSE 'Desconhecido'
    END AS Status
FROM dbo.local_descarte
WHERE id_local_descarte = 1;

-- Se tudo estiver correto, o registro deve aparecer com situacao_local_descarte = 0
-- Se quiser desfazer e testar novamente:
ROLLBACK TRAN;

-- Se quiser confirmar permanentemente:
-- COMMIT TRAN;
*/

-- ========================================================================
-- NOTAS IMPORTANTES:
-- ========================================================================
-- 1. A trigger NÃO deleta fisicamente, apenas marca como inativo.
-- 2. O front-end já filtra por 'situacao_local_descarte = 1', então pontos
--    inativos não aparecerão automaticamente.
-- 3. Se quiser restaurar um ponto (reativar), execute:
--    UPDATE dbo.local_descarte SET situacao_local_descarte = 1
--    WHERE id_local_descarte = X;
-- 4. Se precisar deletar FISICAMENTE (remover do banco), você terá que
--    desabilitar a trigger temporariamente:
--    DISABLE TRIGGER dbo.trg_local_descarte_soft_delete ON dbo.local_descarte;
--    DELETE FROM dbo.local_descarte WHERE ...;
--    ENABLE TRIGGER dbo.trg_local_descarte_soft_delete ON dbo.local_descarte;
