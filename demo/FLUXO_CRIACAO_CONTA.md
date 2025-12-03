# 🔐 Fluxo de Criação de Conta - ReciclaFacil

## 📋 Resumo Geral

O sistema de criação de conta segue uma arquitetura em **camadas** (3-tier architecture):
1. **Frontend (HTML/Templates Thymeleaf)**
2. **Controller (Spring MVC)**
3. **Service (Lógica de Negócio)**
4. **Repository (Acesso ao Banco de Dados)**
5. **Entity (Modelo de Dados)**

---

## 🌐 1. Frontend - Formulário HTML

### Arquivo: `registro.html`
- **Localização**: `src/main/resources/templates/registro.html`
- **Função**: Exibir formulário de cadastro com campos:
  - Nome Completo (obrigatório)
  - Email (obrigatório e único)
  - Senha (mínimo 6 caracteres)
  - Tipo de Usuário (opcional, padrão: "USER")

### Como Funciona:
1. Usuário acessa: `http://localhost:8080/usuarios/registro`
2. O formulário faz POST para `/usuarios/registrar`
3. Dados são enviados como `application/x-www-form-urlencoded`

```html
<form action="/usuarios/registrar" method="POST">
    <input name="nomeUsuario" type="text" required>
    <input name="emailUsuario" type="email" required>
    <input name="senhaUsuario" type="password" required>
    <button type="submit">Criar Conta</button>
</form>
```

---

## 🎛️ 2. Controller - Gerenciar Requisições HTTP

### Arquivo: `ControllerUsuario.java`
- **Localização**: `src/main/java/.../Controller/ControllerUsuario.java`
- **Responsabilidades**: 
  - Receber requisições HTTP
  - Validar dados do formulário
  - Chamar a Service
  - Redirecionar ou retornar resposta

### Endpoints Principais:

#### **GET /usuarios/registro**
```java
@GetMapping("/usuarios/registro")
public String exibirFormularioRegistro() {
    return "registro"; // Retorna template registro.html
}
```
- Exibe o formulário de cadastro

#### **POST /usuarios/registrar**
```java
@PostMapping("/usuarios/registrar")
public String cadastrarUsuario(
        @RequestParam String nomeUsuario,
        @RequestParam String emailUsuario,
        @RequestParam String senhaUsuario,
        Model model) {
    try {
        EntityUsuario usuario = new EntityUsuario();
        usuario.setNome(nomeUsuario);
        usuario.setEmail(emailUsuario);
        usuario.setSenha(senhaUsuario);
        
        serviceUsuario.cadastrarUsuario(usuario); // Delega para a Service
        
        return "redirect:/usuarios/login"; // Redireciona para login
    } catch (IllegalArgumentException e) {
        model.addAttribute("erro", e.getMessage());
        return "registro"; // Volta ao formulário com erro
    }
}
```
- Processa o cadastro
- Trata exceções de validação
- Redireciona ou exibe erro

#### **POST /api/registrar** (Versão API JSON)
```java
@PostMapping("/api/registrar")
@ResponseBody
public ResponseEntity<Map<String, String>> cadastrarUsuarioAPI(...) {
    // Retorna JSON em vez de HTML
    // Útil para aplicações AJAX/JavaScript
}
```

---

## 🔧 3. Service - Lógica de Negócio

### Arquivo: `ServiceUsuario.java`
- **Localização**: `src/main/java/.../Services/ServiceUsuario.java`
- **Responsabilidades**:
  - Validar regras de negócio
  - Criptografar senhas (BCrypt)
  - Verificar duplicações (email único)
  - Persistir dados no banco

### Método Principal: `cadastrarUsuario()`

```java
public EntityUsuario cadastrarUsuario(EntityUsuario usuario) {
    
    // 1️⃣ Validação: Email já existe?
    if (repositoryUsuario.existsByEmail(usuario.getEmail())) {
        throw new IllegalArgumentException("Email já cadastrado no sistema");
    }
    
    // 2️⃣ Validação: Campos obrigatórios?
    if (usuario.getNome() == null || usuario.getNome().isBlank()) {
        throw new IllegalArgumentException("Nome do usuário é obrigatório");
    }
    
    // 3️⃣ Criptografar senha com BCrypt
    usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
    
    // 4️⃣ Definir tipo padrão e situação
    usuario.setTipoUsuario("USER");
    usuario.setSituacao(true); // Ativo
    
    // 5️⃣ Salvar no banco de dados
    return repositoryUsuario.save(usuario);
}
```

### Dependências:
- **BCryptPasswordEncoder**: Criptografia de senhas (Spring Security)
- **RepositoryUsuario**: Acesso ao banco de dados

---

## 💾 4. Repository - Camada de Persistência

### Arquivo: `RepositoryUsuario.java`
- **Localização**: `src/main/java/.../Repository/RepositoryUsuario.java`
- **Função**: Interface JPA que executa queries SQL automaticamente

```java
@Repository
public interface RepositoryUsuario extends JpaRepository<EntityUsuario, Long> {
    
    // Buscar por email
    Optional<EntityUsuario> findByEmail(String email);
    
    // Verificar se email existe
    boolean existsByEmail(String email);
    
    // Buscar por ID (herdado de JpaRepository)
    // save(), delete(), findAll(), etc. (também herdados)
}
```

**Métodos Disponíveis (JPA automático):**
- `save(user)` - Inserir novo usuário
- `findById(id)` - Buscar por ID
- `findByEmail(email)` - Buscar por email
- `existsByEmail(email)` - Verificar existência
- `delete(user)` - Deletar usuário
- `findAll()` - Listar todos

---

## 📊 5. Entity - Modelo de Dados

### Arquivo: `EntityUsuario.java`
- **Localização**: `src/main/java/.../Entities/EntityUsuario.java`
- **Mapeamento**: Representa a tabela `usuario` no banco de dados

```java
@Entity
@Table(name = "usuario")
public class EntityUsuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Long id;
    
    @Column(name = "nomeUsuario", length = 100, nullable = false)
    private String nome;
    
    @Column(name = "email_usuario", length = 100, nullable = false, unique = true)
    private String email;
    
    @Column(name = "senhaUsuario", length = 100, nullable = false)
    private String senha;
    
    @Column(name = "tipoUsuario", length = 50, nullable = false)
    private String tipoUsuario; // ADMIN, USER, etc
    
    @Column(name = "situacaoUsuario", nullable = false)
    private boolean situacao; // true = ativo, false = inativo
}
```

**Estrutura no Banco de Dados:**
```sql
CREATE TABLE usuario (
    idUsuario INT PRIMARY KEY IDENTITY(1,1),
    nomeUsuario VARCHAR(100) NOT NULL,
    email_usuario VARCHAR(100) NOT NULL UNIQUE,
    senhaUsuario VARCHAR(100) NOT NULL,
    tipoUsuario VARCHAR(50) NOT NULL,
    situacaoUsuario BIT NOT NULL
);
```

---

## 🔄 Fluxo Completo: Passo a Passo

```
┌─────────────────────────────────────────────────────────────────┐
│ 1. USUÁRIO ACESSA SITE                                          │
│    GET /usuarios/registro                                       │
└────────────────────┬────────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────────┐
│ 2. CONTROLLER RETORNA FORMULÁRIO                                │
│    ControllerUsuario.exibirFormularioRegistro()                │
│    ↓ Retorna: registro.html                                     │
└────────────────────┬────────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────────┐
│ 3. USUÁRIO PREENCHE E ENVIA FORMULÁRIO                          │
│    POST /usuarios/registrar                                     │
│    ├─ nomeUsuario: "João Silva"                                │
│    ├─ emailUsuario: "joao@email.com"                           │
│    └─ senhaUsuario: "senha123"                                 │
└────────────────────┬────────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────────┐
│ 4. CONTROLLER RECEBE DADOS                                      │
│    ControllerUsuario.cadastrarUsuario()                         │
│    └─ Cria EntityUsuario com os parâmetros                    │
└────────────────────┬────────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────────┐
│ 5. CONTROLLER CHAMA SERVICE                                     │
│    serviceUsuario.cadastrarUsuario(usuario)                    │
└────────────────────┬────────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────────┐
│ 6. SERVICE VALIDA DADOS                                         │
│    ServiceUsuario.cadastrarUsuario()                            │
│    ├─ Email já existe? (repositoryUsuario.existsByEmail)      │
│    ├─ Nome em branco?                                          │
│    ├─ Email em branco?                                         │
│    ├─ Senha em branco?                                         │
│    └─ Se tudo OK, continua...                                 │
└────────────────────┬────────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────────┐
│ 7. SERVICE CRIPTOGRAFA SENHA                                    │
│    usuario.setSenha(                                            │
│        passwordEncoder.encode("senha123")                       │
│    )                                                             │
│    ↓ Resultado: $2a$10$abc123...xyz                            │
└────────────────────┬────────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────────┐
│ 8. SERVICE DEFINE VALORES PADRÃO                                │
│    usuario.setTipoUsuario("USER")                              │
│    usuario.setSituacao(true)                                    │
└────────────────────┬────────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────────┐
│ 9. SERVICE SALVA NO BANCO                                       │
│    repositoryUsuario.save(usuario)                              │
└────────────────────┬────────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────────┐
│ 10. REPOSITORY EXECUTA SQL INSERT                               │
│     INSERT INTO usuario                                         │
│     (nomeUsuario, email_usuario, senhaUsuario, ...)            │
│     VALUES (...)                                                │
└────────────────────┬────────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────────┐
│ 11. BANCO DE DADOS INSERE REGISTRO                              │
│     ✅ Sucesso!                                                 │
└────────────────────┬────────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────────┐
│ 12. CONTROLLER REDIRECIONA                                      │
│     return "redirect:/usuarios/login"                           │
│     ↓ Usuário é levado para página de login                   │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🛡️ Segurança

### 1. **Criptografia de Senha (BCrypt)**
- Senhas são criptografadas com o algoritmo BCrypt antes de serem salvas
- Impossível recuperar a senha original
- Hash é regenerado a cada criptografia (segurança contra rainbow tables)

### 2. **Validação de Dados**
- Campos obrigatórios são validados
- Email deve ser único
- Formato de email é validado pelo HTML5

### 3. **SQL Injection Prevention**
- Uso de JPA/Hibernate (queries parametrizadas automaticamente)
- Sem concatenação de strings em SQL

### 4. **Sessão de Usuário**
- Após login bem-sucedido, usuário é armazenado na sessão:
  ```java
  session.setAttribute("usuarioLogado", usuario);
  session.setAttribute("idUsuario", usuario.getId());
  ```

---

## 🚀 Como Testar

### 1. **Iniciar a Aplicação**
```bash
cd Interdisciplinar LP/demo
mvn spring-boot:run
```

### 2. **Acessar o Formulário**
```
http://localhost:8080/usuarios/registro
```

### 3. **Preencher e Enviar**
- Nome: "João Silva"
- Email: "joao@email.com"
- Senha: "senha123"

### 4. **Verificar no Banco de Dados**
```sql
SELECT * FROM usuario WHERE email_usuario = 'joao@email.com';
```

---

## 📁 Arquivos Criados/Modificados

| Arquivo | Tipo | Descrição |
|---------|------|-----------|
| `ControllerUsuario.java` | ✏️ Modificado | Endpoints HTTP para registro/login |
| `ServiceUsuario.java` | ✏️ Modificado | Lógica de negócio (validação, criptografia) |
| `RepositoryUsuario.java` | ✏️ Modificado | Métodos de acesso ao banco |
| `registro.html` | 🆕 Novo | Formulário de cadastro |
| `login.html` | 🆕 Novo | Formulário de login |
| `pom.xml` | ✏️ Modificado | Adicionada dependência Spring Security |

---

## 🔗 Relacionamentos com Outras Entidades

A `EntityUsuario` tem relacionamento **1:N** com `EntityLogPesquisa`:
- Um usuário pode realizar **múltiplas pesquisas**
- Cada pesquisa é registrada em `log_pesquisa` com referência ao usuário

---

## 💡 Próximos Passos

1. **Adicionar Validação de Email** (enviar link de confirmação)
2. **Implementar "Esqueci a Senha"** (reset password)
3. **Adicionar Roles/Permissões** (ADMIN, USER, MODERATOR)
4. **Integrar com Spring Security** (proteção de endpoints)
5. **Adicionar CAPTCHA** (prevenir bots)
6. **Two-Factor Authentication (2FA)**

---

📝 Documentação criada em: **17 de novembro de 2025**
