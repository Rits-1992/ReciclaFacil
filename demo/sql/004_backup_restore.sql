-- ===================================
-- SQL: BACKUP, Backup Diferencial e Restauração
-- Banco de dados: BdReciclaFacil
-- ===================================
-- Execute estes comandos no SSMS para realizar backups e restaurações

-- ============================================
-- 1. BACKUP COMPLETO (Full Backup)
-- ============================================
-- Realiza um backup completo do banco de dados
-- Executar regularmente (ex: diariamente) para ter cópia segura de todos os dados

BACKUP DATABASE [BdReciclaFacil]
TO DISK = 'C:\SQL_Backups\BdReciclaFacil_FULL_20251205.bak'
WITH 
    DESCRIPTION = 'Full backup do BdReciclaFacil - 2025-12-05',
    COMPRESSION,
    STATS = 10;
GO

-- Nota: Mude 'C:\SQL_Backups\' para o diretório onde deseja salvar o backup
-- Certifique-se de que a pasta existe e você tem permissão de escrita

-- ============================================
-- 2. BACKUP DIFERENCIAL (Differential Backup)
-- ============================================
-- Faz backup apenas das mudanças desde o último backup completo
-- Mais rápido e com menos espaço que o backup completo
-- Execute regularmente após o backup completo (ex: a cada 4 horas)

BACKUP DATABASE [BdReciclaFacil]
TO DISK = 'C:\SQL_Backups\BdReciclaFacil_DIFF_20251205.bak'
WITH 
    DIFFERENTIAL,
    DESCRIPTION = 'Differential backup do BdReciclaFacil - 2025-12-05',
    COMPRESSION,
    STATS = 10;
GO

-- ============================================
-- 3. RESTAURAÇÃO DO BACKUP COMPLETO (Restore Full Backup)
-- ============================================
-- Restaura o banco de dados a partir de um backup completo
-- CUIDADO: Sobrescreve todos os dados atuais
-- IMPORTANTE: Feche a aplicação ANTES de executar isso

-- Passo 1: Desconectar todas as sessões ativas (inclusive a aplicação)
USE master;
GO

ALTER DATABASE [BdReciclaFacil] SET OFFLINE WITH ROLLBACK IMMEDIATE;
GO

-- Passo 2: Restaurar o backup completo
RESTORE DATABASE [BdReciclaFacil]
FROM DISK = 'C:\SQL_Backups\BdReciclaFacil_FULL_20251205.bak'
WITH 
    REPLACE,
    RECOVERY,
    STATS = 10;
GO

-- Passo 3: Colocar banco online novamente
ALTER DATABASE [BdReciclaFacil] SET ONLINE;
GO

-- Passo 4: Confirmar que está pronto
USE [BdReciclaFacil];
SELECT name, state_desc FROM sys.databases WHERE name = 'BdReciclaFacil';
GO

-- ============================================
-- 4. RESTAURAÇÃO COM BACKUP DIFERENCIAL
-- ============================================
-- Restaura o backup completo + aplicar mudanças do backup diferencial
-- Resultado: banco restaurado até a data/hora do último backup diferencial
-- IMPORTANTE: Feche a aplicação ANTES de executar isso

-- Passo 1: Desconectar todas as sessões
USE master;
GO

ALTER DATABASE [BdReciclaFacil] SET OFFLINE WITH ROLLBACK IMMEDIATE;
GO

-- Passo 2: Restaurar backup completo (sem RECOVERY para aceitar diff depois)
RESTORE DATABASE [BdReciclaFacil]
FROM DISK = 'C:\SQL_Backups\BdReciclaFacil_FULL_20251205.bak'
WITH 
    REPLACE,
    NORECOVERY,
    STATS = 10;
GO

-- Passo 3: Aplicar o backup diferencial
RESTORE DATABASE [BdReciclaFacil]
FROM DISK = 'C:\SQL_Backups\BdReciclaFacil_DIFF_20251205.bak'
WITH 
    RECOVERY,
    STATS = 10;
GO

-- Passo 4: Colocar banco online
ALTER DATABASE [BdReciclaFacil] SET ONLINE;
GO

-- Passo 5: Confirmar que está pronto
USE [BdReciclaFacil];
SELECT name, state_desc FROM sys.databases WHERE name = 'BdReciclaFacil';
GO

-- ============================================
-- 5. VERIFICAR HISTÓRICO DE BACKUPS
-- ============================================
-- Lista os backups realizados no servidor

SELECT 
    database_name,
    backup_start_date,
    backup_finish_date,
    type,
    physical_device_name,
    backup_size,
    compressed_backup_size
FROM msdb.dbo.backupset bs
INNER JOIN msdb.dbo.backupmediafamily bmf 
    ON bs.media_set_id = bmf.media_set_id
WHERE database_name = 'BdReciclaFacil'
ORDER BY backup_start_date DESC;
GO

-- ============================================
-- 6. DICAS E BOAS PRÁTICAS
-- ============================================
-- 1. Backup completo: executar 1-2x por semana (ou conforme política)
-- 2. Backup diferencial: executar diariamente ou a cada 4-6 horas
-- 3. Logs de transações: opcional, para recuperação point-in-time
-- 4. Armazenar backups em local seguro (externa/nuvem)
-- 5. Testar restauração periodicamente para garantir integridade
-- 6. Documentar as datas/locais dos backups criados

-- ============================================
-- 7. SCRIPT DE BACKUP AUTOMÁTICO (Job no SQL Agent)
-- ============================================
-- Se quiser agendar backups automáticos, use SQL Agent (SQL Server Enterprise/Standard)
-- ou crie um PowerShell script que rode via Task Scheduler

-- Exemplo de PowerShell (salvar como backup_script.ps1):
/*
$sqlserver = "localhost"
$database = "BdReciclaFacil"
$backupPath = "C:\SQL_Backups"
$timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$backupFile = "$backupPath\${database}_FULL_$timestamp.bak"

# Executar backup via sqlcmd
sqlcmd -S $sqlserver -U sa -P "75395126842" -Q "BACKUP DATABASE [$database] TO DISK = '$backupFile' WITH COMPRESSION, STATS = 10, VERIFY;"

Write-Host "Backup salvo em: $backupFile"
*/
