-- ===========================
-- SQL: Criar VIEW de Mensagens
-- ===========================
-- Execute no SSMS após criar as tabelas

DROP VIEW IF EXISTS vw_mensagens_completo;
GO

CREATE VIEW vw_mensagens_completo AS
SELECT 
    m.id_mensagem   AS id,
    m.titulo_mensagem AS titulo,
    m.conteudo_mensagem AS conteudo,
    m.status_mensagem   AS status,
    m.data_envio        AS data_envio,
    u.id_usuario        AS usuario_id,
    u.nome_usuario      AS nome_usuario,
    u.email_usuario     AS email_usuario
FROM mensagem m
INNER JOIN usuario u ON m.usuario_id = u.id_usuario;
GO
