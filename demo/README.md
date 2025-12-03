# 🌱 ReciclaFacil - Documentação Técnica Completa

## 📋 Índice

1. [Visão Geral](#visão-geral)
2. [Arquitetura](#arquitetura)
3. [Tipos de Usuário](#tipos-de-usuário)
4. [Como Integrar Frontend](#como-integrar-frontend)
5. [Estrutura de Pastas](#estrutura-de-pastas)
6. [Documentação Detalhada](#documentação-detalhada)
7. [Como Rodar o Projeto](#como-rodar-o-projeto)

---

## 🎯 Visão Geral

O ReciclaFacil é uma plataforma web que conecta pessoas à locais de reciclagem.

**Backend:** Spring Boot 3.5.7 (Java 17) com:
- Spring Security para autenticação e autorização
- Spring Data JPA para persistência
- Banco de dados SQL Server
- API REST JSON para frontend integrar

**Frontend:** HTML/CSS/JavaScript (desenvolvido por outra pessoa)

---

## 🏗️ Arquitetura

### Camadas

```
┌─────────────────────────────────────────────────────┐
│               FRONTEND (HTML/CSS/JS)                │
│            Desenvolvido por outra pessoa            │
│          Faz requisições via fetch/axios            │
└──────────────────────┬──────────────────────────────┘
                       │ HTTP/REST
                       │
┌──────────────────────▼──────────────────────────────┐
│         SPRING BOOT BACKEND (REST API)              │
│                                                      │
│  ┌──────────────────────────────────────────────┐  │
│  │ Spring Security (Autenticação/Autorização)  │  │
│  └──────────────────────────────────────────────┘  │
│                       │                             │
│  ┌──────────────────────────────────────────────┐  │
│  │         Controllers (REST Endpoints)         │  │
│  │  • /materiais/api/*                          │  │
│  │  • /locais-descarte/api/*                    │  │
│  │  • /usuarios/*                               │  │
│  └──────────────────────────────────────────────┘  │
│                       │                             │
│  ┌──────────────────────────────────────────────┐  │
│  │    Services (Lógica de Negócio)              │  │
│  │  • Validações                                │  │
│  │  • Criptografia (BCrypt)                     │  │
│  │  • Regras de acesso                          │  │
│  └──────────────────────────────────────────────┘  │
│                       │                             │
│  ┌──────────────────────────────────────────────┐  │
│  │    Repositories (JPA/Hibernate)              │  │
│  │  • Queries automáticas ao banco              │  │
│  │  • CRUD operations                           │  │
│  └──────────────────────────────────────────────┘  │
└──────────────────────┬──────────────────────────────┘
                       │ JDBC/SQL
                       │
┌──────────────────────▼──────────────────────────────┐
│              SQL SERVER BANCO DE DADOS              │
│                                                      │
│  • usuario                                          │
│  • material                                         │
│  • local_descarte                                   │
│  • endereco                                         │
│  • log_pesquisa                                     │
│  • ... (tabelas de relacionamentos)                 │
└──────────────────────────────────────────────────────┘
```

---

## 👥 Tipos de Usuário

### **USER** (Usuário Padrão)
- ✅ Consultar materiais
- ✅ Consultar locais de descarte
- ❌ Criar/editar/deletar materiais
- ❌ Criar/editar/deletar locais

### **ADMIN** (Administrador)
- ✅ Todas as permissões de USER
- ✅ Criar/editar/deletar materiais
- ✅ Criar/editar/deletar locais
- ✅ Gerenciar plataforma

---

## 🔗 Como Integrar Frontend

### **1. Listar Materiais**
```javascript
fetch('/materiais/api/listar')
    .then(r => r.json())
    .then(materiais => {
        console.log(materiais);
        // Renderizar tabela/cards
    });
```

### **2. Criar Material (ADMIN)**
```javascript
fetch('/materiais/api/criar', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
        nome: 'Plástico',
        descricao: 'Garrafas, sacolas...',
        reciclavel: true
    })
})
.then(r => r.json())
.then(data => {
    if (data.sucesso) {
        alert('Material criado!');
    }
});
```

### **3. Login**
```javascript
fetch('/usuarios/autenticar', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: 'emailUsuario=user@email.com&senhaUsuario=senha123'
})
.then(r => {
    if (r.ok) window.location.href = '/dashboard';
    else alert('Erro ao fazer login');
});
```

**Para mais exemplos detalhados:** veja `INTEGRACAO_FRONTEND.md`

---

## 📁 Estrutura de Pastas

```
demo/
├── src/
│   ├── main/
│   │   ├── java/com/interdisciplinar/lp2/demo/
│   │   │   ├── Config/
│   │   │   │   └── SecurityConfig.java          ← Segurança
│   │   │   ├── Controller/
│   │   │   │   ├── ControllerUsuario.java       ← Login/Registro
│   │   │   │   ├── ControllerMaterial.java      ← CRUD Materiais
│   │   │   │   └── ControllerLocalDescarte.java ← CRUD Locais
│   │   │   ├── Services/
│   │   │   │   ├── ServiceUsuario.java
│   │   │   │   ├── ServiceMaterial.java
│   │   │   │   └── ServiceLocalDescarte.java
│   │   │   ├── Repository/
│   │   │   │   ├── RepositoryUsuario.java
│   │   │   │   ├── RepositoryMaterial.java
│   │   │   │   └── RepositoryLocalDescarte.java
│   │   │   ├── Entities/
│   │   │   │   ├── EntityUsuario.java
│   │   │   │   ├── EntityMaterial.java
│   │   │   │   ├── EntityLocalDescarte.java
│   │   │   │   ├── EntityEndereco.java
│   │   │   │   └── EntityLogPesquisa.java
│   │   │   └── DemoApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── templates/              ← Thymeleaf templates
│   │       │   ├── registro.html
│   │       │   └── login.html
│   │       └── static/                 ← CSS/JS/Images
│   └── test/
│       └── ...
├── pom.xml                             ← Maven (dependências)
├── SISTEMA_AUTENTICACAO.md             ← Documentação de segurança
├── INTEGRACAO_FRONTEND.md              ← Como integrar com frontend
├── FLUXO_CRIACAO_CONTA.md              ← Fluxo de registro
├── GUIA_RAPIDO_CADASTRO.md             ← Guia rápido
└── README.md                           ← Este arquivo
```

---

## 📚 Documentação Detalhada

Existem 4 arquivos de documentação:

| Arquivo | Conteúdo |
|---------|----------|
| **SISTEMA_AUTENTICACAO.md** | 🔐 Explicação completa de segurança, roles, fluxos |
| **INTEGRACAO_FRONTEND.md** | 🔗 Como fazer requisições AJAX aos endpoints |
| **FLUXO_CRIACAO_CONTA.md** | 📝 Fluxo passo-a-passo de registro de usuário |
| **GUIA_RAPIDO_CADASTRO.md** | ⚡ Visão rápida com exemplos |

---

## 🚀 Como Rodar o Projeto

### **1. Pré-requisitos**
- Java 17 (JDK)
- Maven (ou use `mvnw.cmd` incluído)
- SQL Server
- Visual Studio Code / IDE de sua escolha

### **2. Configurar Banco de Dados**

Edite `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=reciclafacil;encrypt=true;trustServerCertificate=true;
spring.datasource.username=sa
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServer2012Dialect

# Server
server.port=8080
```

### **3. Compilar e Rodar**

```bash
# Compilar
.\mvnw.cmd clean compile

# Rodar
.\mvnw.cmd spring-boot:run

# Ou com IDE (click direito em DemoApplication.java → Run)
```

### **4. Acessar**
- Backend: `http://localhost:8080`
- Login: `http://localhost:8080/usuarios/login`
- Cadastro: `http://localhost:8080/usuarios/registro`

---

## 📊 Endpoints Disponíveis

### **Usuários**
| Método | URL | Autenticado | Role |
|--------|-----|-------------|------|
| GET | `/usuarios/registro` | ❌ | - |
| POST | `/usuarios/registrar` | ❌ | - |
| GET | `/usuarios/login` | ❌ | - |
| POST | `/usuarios/autenticar` | ❌ | - |
| GET | `/usuarios/logout` | ✅ | USER/ADMIN |

### **Materiais**
| Método | URL | Autenticado | Role |
|--------|-----|-------------|------|
| GET | `/materiais/api/listar` | ✅ | USER/ADMIN |
| GET | `/materiais/api/{id}` | ✅ | USER/ADMIN |
| GET | `/materiais/api/reciclaveis` | ✅ | USER/ADMIN |
| POST | `/materiais/api/criar` | ✅ | **ADMIN** |
| PUT | `/materiais/api/{id}` | ✅ | **ADMIN** |
| DELETE | `/materiais/api/{id}` | ✅ | **ADMIN** |

### **Locais de Descarte**
| Método | URL | Autenticado | Role |
|--------|-----|-------------|------|
| GET | `/locais-descarte/api/listar` | ✅ | USER/ADMIN |
| GET | `/locais-descarte/api/{id}` | ✅ | USER/ADMIN |
| POST | `/locais-descarte/api/criar` | ✅ | **ADMIN** |
| PUT | `/locais-descarte/api/{id}` | ✅ | **ADMIN** |
| DELETE | `/locais-descarte/api/{id}` | ✅ | **ADMIN** |

---

## 🔐 Segurança

### **Autenticação**
- Senhas criptografadas com **BCrypt**
- Sessão do servidor (cookies)
- Email único obrigatório

### **Autorização**
- Spring Security com @PreAuthorize
- Roles: USER, ADMIN
- Proteção por endpoint

### **Validações**
- Email format
- Campos obrigatórios
- Dados únicos
- Comprimento de campos

---

## 📝 Exemplo: Criar Material como ADMIN

### **Frontend (HTML)**
```html
<form id="formMaterial">
    <input id="nome" type="text" placeholder="Nome do material">
    <textarea id="descricao" placeholder="Descrição"></textarea>
    <label>
        <input id="reciclavel" type="checkbox" checked> É reciclável?
    </label>
    <button type="submit">Criar Material</button>
</form>
```

### **Frontend (JavaScript)**
```javascript
document.getElementById('formMaterial').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const material = {
        nome: document.getElementById('nome').value,
        descricao: document.getElementById('descricao').value,
        reciclavel: document.getElementById('reciclavel').checked
    };
    
    const response = await fetch('/materiais/api/criar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(material)
    });
    
    const data = await response.json();
    
    if (data.sucesso) {
        alert('Material criado com sucesso!');
        // Recarregar lista
        carregarMateriais();
    } else {
        alert('Erro: ' + data.erro);
    }
});

function carregarMateriais() {
    fetch('/materiais/api/listar')
        .then(r => r.json())
        .then(materiais => {
            const html = materiais.map(m => `
                <div class="card">
                    <h3>${m.nome}</h3>
                    <p>${m.descricao || '-'}</p>
                    <p>Reciclável: ${m.reciclavel ? 'Sim' : 'Não'}</p>
                </div>
            `).join('');
            document.getElementById('listaMateriais').innerHTML = html;
        });
}

// Carregar ao abrir página
carregarMateriais();
```

### **Backend (Java)**
```java
@PostMapping("/api/criar")
@PreAuthorize("hasRole('ADMIN')")
@ResponseBody
public ResponseEntity<Map<String, Object>> criarMaterialAPI(
        @RequestBody EntityMaterial material) {
    Map<String, Object> resposta = new HashMap<>();
    try {
        // Spring Security já validou que é ADMIN
        EntityMaterial materialCriado = serviceMaterial.criar(material);
        resposta.put("sucesso", true);
        resposta.put("mensagem", "Material criado com sucesso");
        resposta.put("material", materialCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    } catch (IllegalArgumentException e) {
        resposta.put("sucesso", false);
        resposta.put("erro", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }
}
```

---

## 🛠️ Troubleshooting

### **"401 Unauthorized"**
→ Usuário não está autenticado (não fez login)
→ Solução: Fazer login primeiro

### **"403 Forbidden"**
→ Usuário não tem role ADMIN
→ Solução: Apenas ADMINs podem criar/editar/deletar

### **"400 Bad Request"**
→ Dados inválidos (email duplicado, nome vazio, etc)
→ Solução: Verificar validação no frontend

### **Banco de dados não conecta**
→ Verificar `application.properties`
→ Verificar se SQL Server está rodando
→ Verificar credenciais

---

## 🎓 Próximas Melhorias

- [ ] Two-Factor Authentication (2FA)
- [ ] Recuperação de senha por email
- [ ] Roles mais granulares (MODERATOR, etc)
- [ ] Rate limiting para APIs
- [ ] Cache de dados frequentes
- [ ] Integração com mapa (Google Maps)
- [ ] Busca avançada de materiais
- [ ] Estatísticas e dashboard
- [ ] Notificações por email
- [ ] Mobile app

---

## 📞 Contato/Dúvidas

Se tiver dúvidas sobre:
- **Integração Frontend:** veja `INTEGRACAO_FRONTEND.md`
- **Segurança:** veja `SISTEMA_AUTENTICACAO.md`
- **Fluxo de Usuário:** veja `FLUXO_CRIACAO_CONTA.md`

---

## 📄 Licença

Projeto acadêmico - Interdisciplinar LP2

---

## 👨‍💻 Desenvolvimento

**Backend:** Java/Spring Boot
**Frontend:** HTML/CSS/JavaScript (desenvolvido por terceiro)
**Banco:** SQL Server

**Data:** 17 de novembro de 2025
**Status:** ✅ Compilação bem-sucedida

---

## ✨ Resumo do que foi feito

1. ✅ Sistema de autenticação com Spring Security
2. ✅ Dois tipos de usuários (USER, ADMIN)
3. ✅ CRUD de materiais protegido por role
4. ✅ CRUD de locais de descarte protegido por role
5. ✅ APIs REST JSON para frontend integrar
6. ✅ Criptografia de senhas (BCrypt)
7. ✅ Validação de dados
8. ✅ 4 arquivos de documentação completa
9. ✅ Exemplos de integração
10. ✅ Projeto compilado e funcionando

Pronto para o frontend integrar! 🚀
