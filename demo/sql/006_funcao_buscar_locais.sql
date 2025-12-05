-- =============================================================
-- FUNÇÃO: buscar locais de descarte por cidade (inline TVF)
-- Objetivo: retornar os locais ativos que correspondem a uma string
-- de cidade (case/acentuação-insensitive) e limitar resultados.
-- Benefício: centraliza lógica de busca no banco, melhora reuso
-- e permite consultas otimizadas (inline TVF é SARGable).
-- =============================================================

USE BdReciclaFacil;
GO

IF OBJECT_ID('dbo.fn_buscar_locais_por_cidade', 'TF') IS NOT NULL
    DROP FUNCTION dbo.fn_buscar_locais_por_cidade;
GO

CREATE FUNCTION dbo.fn_buscar_locais_por_cidade
(
    @cidade NVARCHAR(100),
    @limit INT = 100
)
RETURNS TABLE
AS
RETURN
(
    SELECT TOP(@limit)
        ld.id_local_descarte,
        ld.nome_local_descarte,
        ld.whatsapp_local_descarte AS contato_whatsapp,
        ld.descricao_local_descarte AS descricao,
        ld.contato_email AS email,
        ld.horario_abertura,
        ld.horario_fechamento,
        e.logradouro AS rua,
        e.numero_endereco AS numero,
        e.bairro,
        e.cidade,
        e.estado,
        e.cep
    FROM dbo.local_descarte ld
    INNER JOIN dbo.endereco e
        ON ld.endereco_id = e.id_endereco
    WHERE ld.situacao_local_descarte = 1
      AND (
          -- Busca acentuação- e case-insensitive usando COLLATE
          e.cidade COLLATE Latin1_General_CI_AI LIKE '%' + @cidade + '%'
      )
    ORDER BY ld.nome_local_descarte
);
GO

-- Uso:
-- SELECT * FROM dbo.fn_buscar_locais_por_cidade('Campinas', 50);
-- Ou via JPA/native query: SELECT * FROM dbo.fn_buscar_locais_por_cidade(:cidade, :limit)

-- Recomendações de indexação (executar se fizer sentido no seu ambiente):
-- 1) Índice na coluna 'cidade' da tabela 'endereco' para acelerar LIKE (pode ajudar mesmo com COLLATE):
--    CREATE INDEX IX_endereco_cidade ON dbo.endereco(cidade);
-- 2) Índice na coluna 'situacao_local_descarte' para filtrar ativos rapidamente:
--    CREATE INDEX IX_local_descarte_situacao ON dbo.local_descarte(situacao_local_descarte);
-- Observação: ajuste nomes/colunas caso seu esquema use outras convenções.

-- Notas:
-- - Esta é uma função INLINE (TVF). Evite funções com múltiplos statements retornando tabelas, pois
--   elas não são tão performáticas quanto as inline.
-- - Se precisar de filtros adicionais (tipo de descarte, distância geográfica, paginação com offset),
--   podemos estender a função ou criar outra que aceite mais parâmetros.
-- - Para busca por proximidade (distância), é necessário guardar coordenadas (latitude/longitude)
--   no banco e usar cálculos/índices espaciais.
