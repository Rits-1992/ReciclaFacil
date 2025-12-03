# 🔐 Sistema de Autenticação e Autorização - ReciclaFacil

## 📊 Arquitetura de Segurança

```
┌─────────────────────────────────────────────────────────────────┐
│                        NAVEGADOR/CLIENTE                        │
│                                                                   │
│  • HTML/CSS/JS (desenvolvido por outra pessoa)                  │
│  • Faz requisições ao backend via fetch/axios                   │
└────────────────────┬────────────────────────────────────────────┘
                     │
                     │ HTTP Request
                     │
┌────────────────────▼────────────────────────────────────────────┐
│              Spring Security (SecurityConfig)                    │
│                                                                   │
│  • Intercepta todas as requisições                              │
│  • Verifica autenticação (sessão/token)                         │
│  • Verifica autorização (Role ADMIN/USER)                       │
└────────────────────┬────────────────────────────────────────────┘
                     │
         ┌───────────┴───────────┐
         │                       │
    Autenticado?            Não autenticado?
         │                       │
    Verificar Role          Redirecionar
         │                  para /login
    ┌────┴────┐
    │          │
 ADMIN?      USER?
    │          │
  ✅          ✅
   (acesso a   (acesso apenas
   endpoints   a consultas)
   de CRUD)
    │          │
    └────┬─────┘
         │
    ✅ Request permitido
         │
┌────────▼─────────────────────────────────────────────────────────┐
│                  SPRING BOOT BACKEND                             │
│                                                                   │
│  • ControllerMaterial (@PreAuthorize)                            │
│  • ControllerLocalDescarte (@PreAuthorize)                       │
│  • Services (lógica de negócio)                                 │
│  • Repository (acesso BD)                                        │
└────────────────────┬────────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────────┐
│                   BANCO DE DADOS                                 │
│                                                                   │
│  • tabela usuario (idUsuario, email, senha, tipoUsuario)        │
│  • tabela material (idMaterial, nome, ...)                      │
│  • tabela local_descarte (id_local_descarte, nome, ...)         │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔑 Tipos de Usuários (Roles)

### **USER (Usuário Padrão)**
```
Permissões:
├─ ✅ Consultar materiais (GET /materiais/api/listar)
├─ ✅ Consultar locais de descarte (GET /locais-descarte/api/listar)
├─ ✅ Buscar por ID (GET /materiais/api/{id})
├─ ✅ Buscar por ID (GET /locais-descarte/api/{id})
└─ ❌ Criar/Editar/Deletar materiais
└─ ❌ Criar/Editar/Deletar locais
```

**Casos de uso:**
- Pessoa comum querendo buscar onde reciclar
- Consultar quais materiais são aceitos
- Ver horários e contatos dos locais

### **ADMIN (Administrador)**
```
Permissões:
├─ ✅ Todas as permissões de USER
├─ ✅ Criar material (POST /materiais/api/criar)
├─ ✅ Editar material (PUT /materiais/api/{id})
├─ ✅ Deletar material (DELETE /materiais/api/{id})
├─ ✅ Criar local descarte (POST /locais-descarte/api/criar)
├─ ✅ Editar local descarte (PUT /locais-descarte/api/{id})
└─ ✅ Deletar local descarte (DELETE /locais-descarte/api/{id})
```

**Casos de uso:**
- Gerenciar catálogo de materiais
- Atualizar informações de locais de coleta
- Manter banco de dados consistente

---

## 🔄 Fluxo de Autenticação

### **1. Usuário faz Registro**

```
┌──────────────────────┐
│ Acessa /registro     │
└──────────┬───────────┘
           │
┌──────────▼────────────────────────────────┐
│ Preenche formulário                        │
│ • Nome: "João"                             │
│ • Email: "joao@email.com"                 │
│ • Senha: "senha123"                       │
│ • Tipo: USER (padrão)                     │
└──────────┬────────────────────────────────┘
           │
┌──────────▼────────────────────────────────┐
│ POST /usuarios/registrar                  │
│                                           │
│ ServiceUsuario:                           │
│ • Valida email único                      │
│ • Criptografa senha (BCrypt)              │
│ • Define tipoUsuario = "USER"             │
│ • Define situacao = true (ativo)          │
│ • Salva no banco                          │
└──────────┬────────────────────────────────┘
           │
┌──────────▼────────────────────────────────┐
│ ✅ Sucesso!                                │
│ Redireciona para /usuarios/login          │
└──────────────────────────────────────────┘
```

### **2. Usuário faz Login**

```
┌──────────────────────┐
│ Acessa /login        │
└──────────┬───────────┘
           │
┌──────────▼────────────────────────────────┐
│ Preenche formulário                        │
│ • Email: "joao@email.com"                 │
│ • Senha: "senha123"                       │
└──────────┬────────────────────────────────┘
           │
┌──────────▼────────────────────────────────┐
│ POST /usuarios/autenticar                 │
│                                           │
│ ServiceUsuario.autenticar():              │
│ • Busca usuario por email                 │
│ • Compara senha (BCrypt.matches)          │
│ • Retorna true/false                      │
└──────────┬────────────────────────────────┘
           │
    ┌──────┴──────┐
    │             │
Válido?       Inválido?
    │             │
   ✅            ❌
    │             │
┌───▼─────┐  ┌───▼────────────────┐
│ Create  │  │ Volta para /login  │
│ Session │  │ com erro           │
│ com:    │  └────────────────────┘
│ • idU.. │
│ • nome  │
│ • type  │
└────┬────┘
     │
┌────▼───────────────────┐
│ Redireciona para       │
│ /dashboard             │
│ (usuário autenticado)  │
└────────────────────────┘
```

### **3. Usuário Acessa Recurso Protegido**

**Requisição: GET /materiais/api/listar (USER)**
```
┌──────────────────────────────────────────┐
│ Cliente envia requisição com cookies     │
│ (sessão está nos cookies automaticamente)│
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│ Spring Security Valida:                  │
│                                          │
│ 1. Sessão existe?                       │
│    Sim ✅ → continue                    │
│    Não ❌ → 401 Unauthorized            │
│                                          │
│ 2. Usuario tem role necessário?         │
│    /materiais/api/* = @Authenticated   │
│    Qualquer USER/ADMIN ✅ → continue   │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│ ✅ Request permitido                    │
│                                          │
│ ControllerMaterial.listarMateriaisAPI() │
│ ServiceMaterial.listarTodos()           │
│ RepositoryMaterial.findAll()            │
│                                          │
│ Retorna JSON com todos os materiais     │
└──────────────────────────────────────────┘
```

**Requisição: POST /materiais/api/criar (ADMIN)**
```
┌──────────────────────────────────────────┐
│ Cliente envia requisição com cookies     │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│ Spring Security Valida:                  │
│                                          │
│ 1. Sessão existe?                       │
│    Sim ✅ → continue                    │
│    Não ❌ → 401 Unauthorized            │
│                                          │
│ 2. Usuario tem role ADMIN?              │
│    /materiais/api/criar = @hasRole      │
│    'ADMIN'                              │
│    User tem tipo="USER"?                 │
│    Não ❌ → 403 Forbidden               │
│    User tem tipo="ADMIN"?               │
│    Sim ✅ → continue                    │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│ ✅ Request permitido                    │
│                                          │
│ ControllerMaterial.criarMaterialAPI()   │
│ (apenas ADMIN pode executar)            │
│                                          │
│ Retorna 201 Created com novo material   │
└──────────────────────────────────────────┘
```

**Requisição: DELETE /materiais/api/{id} (USER - acesso negado)**
```
┌──────────────────────────────────────────┐
│ Cliente (USER) tenta deletar             │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│ Spring Security Valida:                  │
│                                          │
│ 1. Sessão existe?                       │
│    Sim ✅ → continue                    │
│                                          │
│ 2. Usuario tem role ADMIN?              │
│    /materiais/api/{id} = @hasRole       │
│    'ADMIN'                              │
│    User tem tipo="USER" ❌              │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│ ❌ 403 FORBIDDEN                        │
│                                          │
│ Acesso negado: usuário não tem          │
│ permissão para deletar materiais        │
└──────────────────────────────────────────┘
```

---

## 🛡️ Spring Security Config Explicado

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // ========== ENDPOINTS PÚBLICOS (sem autenticação) ==========
    .requestMatchers(
        "/usuarios/registro",       // Formulário de cadastro
        "/usuarios/login",          // Formulário de login
        "/usuarios/api/registrar",  // API de cadastro
        "/usuarios/api/autenticar", // API de login
        "/css/**",                  // Arquivos CSS
        "/js/**"                    // Arquivos JavaScript
    ).permitAll()

    // ========== ENDPOINTS AUTENTICADOS (qualquer usuário logado) ==========
    .requestMatchers(
        "/materiais/consultar",      // Listar materiais (HTML)
        "/materiais/api/listar",     // Listar materiais (JSON)
        "/materiais/api/**",         // Qualquer GET de materiais
        "/locais-descarte/consultar",
        "/locais-descarte/api/listar"
    ).authenticated()  // Requer estar logado (USER ou ADMIN)

    // ========== ENDPOINTS ADMIN ONLY (apenas ADMIN) ==========
    .requestMatchers(
        "/materiais/criar",          // Formulário criar (HTML)
        "/materiais/editar/**",      // Formulário editar (HTML)
        "/materiais/remover/**",     // Deletar (HTML)
        "/materiais/api/criar",      // Criar (JSON)
        "/materiais/api/editar/**",  // Editar (JSON)
        "/materiais/api/deletar/**", // Deletar (JSON)
        "/locais-descarte/criar",
        "/locais-descarte/editar/**",
        "/locais-descarte/remover/**",
        "/locais-descarte/api/criar",
        "/locais-descarte/api/editar/**",
        "/locais-descarte/api/deletar/**"
    ).hasRole("ADMIN")  // Requer role = "ADMIN"
}
```

---

## 📋 @PreAuthorize Anotações

Usadas em métodos específicos do controller para autorização granular:

```java
// Qualquer usuário autenticado (USER ou ADMIN)
@GetMapping("/consultar")
public String listarMateriais(Model model) {...}

// Apenas ADMIN
@PostMapping("/criar")
@PreAuthorize("hasRole('ADMIN')")
public String criarMaterial(...) {...}

// Apenas ADMIN
@DeleteMapping("/api/{id}")
@PreAuthorize("hasRole('ADMIN')")
@ResponseBody
public ResponseEntity<?> deletarMaterialAPI(@PathVariable Long id) {...}

// Usuário proprietário do recurso
@GetMapping("/{id}")
@PreAuthorize("@userService.isOwner(#id, principal.username)")
public String verUsuario(@PathVariable Long id) {...}
```

---

## 🔍 Onde está o tipoUsuario?

### **EntityUsuario.java**
```java
@Column(name = "tipoUsuario", length = 50, nullable = false)
private String tipoUsuario; // "ADMIN" ou "USER"
```

### **ServiceUsuario.java - Cadastro**
```java
public EntityUsuario cadastrarUsuario(EntityUsuario usuario) {
    // ... validações ...
    
    // Definir tipo padrão se não especificado
    if (usuario.getTipoUsuario() == null || usuario.getTipoUsuario().isBlank()) {
        usuario.setTipoUsuario("USER"); // Novo usuário = USER por padrão
    }
    
    // ... criptografar, salvar ...
}
```

### **Como criar um ADMIN?**

**Opção 1: Diretamente no banco (SQL)**
```sql
UPDATE usuario 
SET tipoUsuario = 'ADMIN' 
WHERE email_usuario = 'admin@email.com';
```

**Opção 2: Criar endpoint especial (seguro)**
```java
@PostMapping("/api/promover-admin")
@PreAuthorize("hasRole('ADMIN')") // Apenas ADMINs existentes podem promover
@ResponseBody
public ResponseEntity<?> promoverAdmin(@RequestParam Long userId) {
    // ... validações ...
    EntityUsuario usuario = usuarioRepository.findById(userId).orElseThrow();
    usuario.setTipoUsuario("ADMIN");
    usuarioRepository.save(usuario);
    return ResponseEntity.ok("Usuário promovido a ADMIN");
}
```

---

## 🚀 Fluxo Completo: User vs Admin

### **Usuário: Consultar Materiais**

```
1. Acesso: GET /materiais/api/listar
   └─ SecurityConfig permite (autenticado)
   
2. Spring Security valida:
   └─ Sessão existe? ✅
   └─ Qual role? USER ou ADMIN? (ambos OK)
   
3. ControllerMaterial.listarMateriaisAPI()
   └─ @ResponseBody → retorna JSON
   
4. Resposta:
   [
     {"id": 1, "nome": "Plástico", ...},
     {"id": 2, "nome": "Vidro", ...}
   ]
```

### **Admin: Criar Material**

```
1. Acesso: POST /materiais/api/criar
   └─ SecurityConfig requer hasRole("ADMIN")
   
2. Spring Security valida:
   └─ Sessão existe? ✅
   └─ tipoUsuario = "ADMIN"? ✅
   
3. @PreAuthorize("hasRole('ADMIN')")
   └─ Confirma novamente (defesa em camadas)
   
4. ControllerMaterial.criarMaterialAPI()
   └─ Executa lógica de criação
   └─ ServiceMaterial.criar()
   └─ RepositoryMaterial.save()
   
5. Resposta:
   {
     "sucesso": true,
     "mensagem": "Material criado com sucesso",
     "material": {"id": 3, "nome": "Alumínio", ...}
   }
```

### **User: Tenta Criar Material (acesso negado)**

```
1. Acesso: POST /materiais/api/criar
   └─ SecurityConfig requer hasRole("ADMIN")
   
2. Spring Security valida:
   └─ Sessão existe? ✅
   └─ tipoUsuario = "ADMIN"? ❌ (é "USER")
   
3. Spring Security nega:
   └─ 403 FORBIDDEN
   └─ RedirectTo: /erro/acesso-negado
   
4. Resposta:
   HTTP 403 Forbidden
   "Você não tem permissão para acessar este recurso"
```

---

## 📝 Resumo das Mudanças

| Componente | Mudança |
|-----------|---------|
| `SecurityConfig.java` | ✅ Novo - Configuração de segurança |
| `ControllerMaterial.java` | ✏️ Endpoints com @PreAuthorize |
| `ControllerLocalDescarte.java` | ✏️ Endpoints com @PreAuthorize |
| `ServiceMaterial.java` | ✏️ CRUD completo |
| `ServiceLocalDescarte.java` | ✏️ Tipos atualizados (Long) |
| `RepositoryMaterial.java` | ✏️ Métodos customizados |
| `RepositoryLocalDescarte.java` | ✏️ Tipos atualizados (Long) |
| `pom.xml` | ✏️ spring-boot-starter-security adicionado |
| `EntityUsuario.java` | ✅ tipoUsuario já existe |
| `INTEGRACAO_FRONTEND.md` | ✅ Novo - Guia para desenvolver frontend |

---

## 🎯 Checklist Final

- ✅ Sistema de roles (ADMIN/USER) implementado
- ✅ Spring Security configurado com autorização granular
- ✅ Endpoints protegidos por @PreAuthorize
- ✅ CRUD de materiais (apenas ADMIN)
- ✅ CRUD de locais (apenas ADMIN)
- ✅ Consultas de materiais (USER e ADMIN)
- ✅ Consultas de locais (USER e ADMIN)
- ✅ APIs REST JSON para frontend integrar
- ✅ Tratamento de erro 403 (acesso negado)
- ✅ Documentação de integração frontend

---

📝 **Criado em:** 17 de novembro de 2025
🔐 **Segurança:** Spring Security 6.x + BCrypt + @PreAuthorize
