# Scripts SQL - ReciclaFácil

Pasta contendo scripts SQL para criar views, triggers e tabelas complementares do banco de dados.

## Arquivos

### `001_criar_view.sql`
- **Propósito**: Criar a VIEW `vw_mensagens_completo`
- **O que faz**: Une dados de `mensagem` + `usuario` para o admin visualizar mensagens com nome do usuário
- **Quando executar**: Após criar tabelas `mensagem` e `usuario`
- **Como executar**: Abra no SSMS e execute (Ctrl+E)

### `002_trigger_mensagem.sql`
- **Propósito**: Rastrear mudanças de status de mensagens
- **O que faz**: 
  - Quando status muda (ex: ENVIADA → LIDA), registra:
    - `status_anterior` = status antigo
    - `data_ultimo_status_change` = data/hora da mudança
- **Quando executar**: 
  1. Primeiro: Execute o ALTER TABLE (descomente a primeira seção)
  2. Depois: Execute o CREATE TRIGGER
- **Como executar**: Abra no SSMS e execute

## Ordem de execução recomendada

```
1. Criar tabelas (usuario, mensagem, etc.) - feito automaticamente pelo Hibernate
2. Executar: 001_criar_view.sql
3. Executar: 002_trigger_mensagem.sql (primeiro ALTER, depois CREATE TRIGGER)
```

## Conexão no SSMS

```
Server: localhost
Database: BdReciclaFacil
Username: sa
Password: 75395126842
```

## Notas

- Triggers rodam automaticamente quando UPDATE ocorre na tabela `mensagem`
- A VIEW é somente leitura (read-only)
- Não reexecute scripts que já foram executados (vai dar erro de "já existe")
  - Para limpar: use `DROP VIEW` ou `DROP TRIGGER` antes de reexecutar
