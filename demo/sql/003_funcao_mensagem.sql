-- ===================================
-- SQL: Function para obter a data/hora do último status change
-- ===================================
-- Salve como demo/sql/003_funcao_mensagem.sql e execute no SSMS

-- Esta função retorna a coluna `data_ultimo_status_change` da tabela `mensagem`
-- para a mensagem informada. Se a coluna estiver NULL (nenhuma mudança registrada),
-- ela retorna a `dataEnvio` como fallback.

CREATE OR ALTER FUNCTION dbo.fn_get_ultimo_status_change(@id_mensagem BIGINT)
RETURNS DATETIME2
AS
BEGIN
    DECLARE @dt DATETIME2;

    SELECT @dt = data_ultimo_status_change
    FROM mensagem
    WHERE id_mensagem = @id_mensagem;

    IF @dt IS NOT NULL
        RETURN @dt;

    -- fallback: retornar data_envio se não houver alteração registrada
    SELECT @dt = data_envio FROM mensagem WHERE id_mensagem = @id_mensagem;
    RETURN @dt;
END;
GO

-- Uso:
-- SELECT dbo.fn_get_ultimo_status_change(123);
-- Pode ser usada em consultas: SELECT m.*, dbo.fn_get_ultimo_status_change(m.id_mensagem) AS ultima_mudanca FROM mensagem m;
