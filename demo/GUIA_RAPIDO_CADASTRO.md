# 🚀 Guia Rápido: Sistema de Criação de Conta

## 📍 Onde está o código?

### 1. **Frontend (Formulários HTML)**
```
src/main/resources/templates/
├── registro.html      ← Formulário de cadastro
└── login.html         ← Formulário de login
```

**Acesso no navegador:**
- Cadastro: `http://localhost:8080/usuarios/registro`
- Login: `http://localhost:8080/usuarios/login`

---

### 2. **Backend - Estrutura de Camadas**

```
src/main/java/com/interdisciplinar/lp2/demo/

├── Controller/
│   └── ControllerUsuario.java
│       ├── GET  /usuarios/registro      → Exibe formulário
│       ├── POST /usuarios/registrar     → Processa cadastro
│       ├── GET  /usuarios/login         → Exibe login
│       ├── POST /usuarios/autenticar    → Processa login
│       ├── GET  /usuarios/logout        → Faz logout
│       ├── POST /api/registrar          → API JSON (AJAX)
│       └── POST /api/autenticar         → API JSON (AJAX)

├── Services/
│   └── ServiceUsuario.java
│       ├── cadastrarUsuario()           → Valida + criptografa + salva
│       ├── autenticar()                 → Verifica email/senha
│       ├── buscarPorEmail()             → Query por email
│       ├── buscarPorId()                → Query por ID
│       ├── listarTodos()                → Lista todos os usuários
│       ├── atualizar()                  → Atualiza usuário
│       └── deletar()                    → Deleta usuário

├── Repository/
│   └── RepositoryUsuario.java
│       ├── findByEmail()                → JPA query automática
│       ├── existsByEmail()              → Verifica se existe
│       ├── save()                       → Herdado de JpaRepository
│       ├── findById()                   → Herdado
│       ├── findAll()                    → Herdado
│       └── deleteById()                 → Herdado

└── Entities/
    └── EntityUsuario.java
        @Entity @Table(name = "usuario")
        ├── idUsuario (PK)
        ├── nomeUsuario
        ├── email_usuario (UNIQUE)
        ├── senhaUsuario (criptografada com BCrypt)
        ├── tipoUsuario (ADMIN, USER, etc)
        └── situacaoUsuario (ativo/inativo)
```

---

## 🔄 Fluxo Simplificado

```
USUÁRIO PREENCHENDO FORMULÁRIO
         ↓
    NAVEGADOR
         ↓
  CONTROLLER (recebe POST)
         ↓
    SERVICE (valida + criptografa)
         ↓
  REPOSITORY (salva no banco)
         ↓
 BANCO DE DADOS (insere registro)
         ↓
 SUCESSO: usuário criado ✅
```

---

## 🔐 Segurança Implementada

| Segurança | Como Funciona |
|-----------|---------------|
| **Criptografia de Senha** | BCryptPasswordEncoder (Spring Security) |
| **Email Único** | `unique = true` + validação na Service |
| **SQL Injection** | JPA Hibernate (queries parametrizadas) |
| **Validação de Entrada** | Jakarta Validation (@NotBlank, @Email) |
| **Sessão de Usuário** | HttpSession após login bem-sucedido |

---

## 📦 Dependências Adicionadas

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

---

## 🧪 Testando o Sistema

### 1. **Iniciar aplicação**
```bash
cd demo
.\mvnw.cmd spring-boot:run
```

### 2. **Acessar cadastro**
```
http://localhost:8080/usuarios/registro
```

### 3. **Preencher dados**
- Nome: "Maria Silva"
- Email: "maria@email.com"
- Senha: "senha@123"

### 4. **Enviar formulário**
- Sistema criptografa a senha
- Valida email único
- Salva no banco de dados

### 5. **Fazer login**
```
http://localhost:8080/usuarios/login
```

### 6. **Verificar no banco**
```sql
SELECT idUsuario, nomeUsuario, email_usuario, tipoUsuario, situacaoUsuario
FROM usuario 
WHERE email_usuario = 'maria@email.com';
```

---

## 🎨 Interfaces de Usuário

### Página de Cadastro (`registro.html`)
```
┌──────────────────────────────────┐
│     ReciclaFacil                 │
│  Crie sua conta e comece         │
│  a reciclar                      │
├──────────────────────────────────┤
│ Nome Completo:                   │
│ [________________________]        │
│                                  │
│ Email:                           │
│ [________________________]        │
│                                  │
│ Senha:                           │
│ [________________________]        │
│                                  │
│     [  Criar Conta  ]            │
│                                  │
│ Já tem uma conta?                │
│ Faça login aqui                  │
└──────────────────────────────────┘
```

### Página de Login (`login.html`)
```
┌──────────────────────────────────┐
│     ReciclaFacil                 │
│  Bem-vindo de volta!             │
├──────────────────────────────────┤
│ Email:                           │
│ [________________________]        │
│                                  │
│ Senha:                           │
│ [________________________]        │
│                                  │
│     [  Entrar  ]                 │
│                                  │
│ Esqueceu a senha?                │
│ Não tem uma conta?               │
│ Registre-se aqui                 │
└──────────────────────────────────┘
```

---

## 📊 Banco de Dados

### Tabela: `usuario`
```sql
idUsuario (INT, PK, AUTO_INCREMENT)
nomeUsuario (VARCHAR(100), NOT NULL)
email_usuario (VARCHAR(100), NOT NULL, UNIQUE)
senhaUsuario (VARCHAR(100), NOT NULL)  ← Criptografada (BCrypt)
tipoUsuario (VARCHAR(50), NOT NULL)     ← "USER", "ADMIN", etc
situacaoUsuario (BIT, NOT NULL)         ← 1 = ativo, 0 = inativo
```

### Exemplo de registro salvo:
```
idUsuario: 1
nomeUsuario: Maria Silva
email_usuario: maria@email.com
senhaUsuario: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4/8O
tipoUsuario: USER
situacaoUsuario: 1
```

⚠️ **Nota:** A senha não pode ser revertida (hash criptografado é irreversível)

---

## 🔗 Endpoints Disponíveis

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/usuarios/registro` | Formulário de cadastro |
| POST | `/usuarios/registrar` | Processa cadastro (HTML form) |
| GET | `/usuarios/login` | Formulário de login |
| POST | `/usuarios/autenticar` | Processa login (HTML form) |
| GET | `/usuarios/logout` | Desconecta usuário |
| POST | `/api/registrar` | API JSON para cadastro (AJAX) |
| POST | `/api/autenticar` | API JSON para login (AJAX) |

---

## 🚀 Próximas Implementações Sugeridas

- [ ] Validação de email com link de confirmação
- [ ] Recuperação de senha (reset password)
- [ ] Perfis de usuário (ADMIN, USER, MODERADOR)
- [ ] Proteção de endpoints com Spring Security
- [ ] CAPTCHA para prevenir bots
- [ ] Two-Factor Authentication (2FA)
- [ ] Auditoria de login/logout
- [ ] Integração com redes sociais (Google, Facebook)

---

## 📚 Documentação Completa

Veja **`FLUXO_CRIACAO_CONTA.md`** para documentação detalhada com:
- ✅ Explicação de cada camada (Controller, Service, Repository, Entity)
- ✅ Fluxo passo-a-passo com diagramas
- ✅ Exemplos de código
- ✅ Estrutura do banco de dados
- ✅ Medidas de segurança

---

## 🎯 Resumo

Você agora tem um **sistema completo de criação de contas** que inclui:

1. ✅ Formulários HTML responsivos (registro.html, login.html)
2. ✅ Controller com 7 endpoints diferentes
3. ✅ Service com validação e criptografia BCrypt
4. ✅ Repository com queries automáticas
5. ✅ Entidade mapeada ao banco de dados
6. ✅ Dependência Spring Security adicionada ao pom.xml
7. ✅ Proteção contra SQL injection, emails duplicados, senhas fracas
8. ✅ Autenticação baseada em sessão

Tudo pronto para usar! 🎉

---

📝 **Criado em:** 17 de novembro de 2025
🔧 **Stack:** Spring Boot 3.5.7 + Spring Security + Thymeleaf + JPA/Hibernate
