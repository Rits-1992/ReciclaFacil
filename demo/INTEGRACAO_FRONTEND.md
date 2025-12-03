# 🔗 Guia de Integração Frontend-Backend

## 📌 Visão Geral

O backend agora fornece dois tipos de interfaces:

1. **Endpoints HTML/Formulários** (para integração com templates Thymeleaf)
2. **API REST JSON** (para integração com JavaScript/AJAX)

Como outra pessoa está desenvolvendo o HTML/CSS/JS, você pode usar a **API REST** para fazer requisições AJAX.

---

## 🔐 Sistema de Permissões (Roles)

```
┌─────────────────┐
│     USUÁRIO     │
├─────────────────┤
│ Pode:           │
│ • Consultar     │
│   materiais     │
│ • Consultar     │
│   locais de     │
│   coleta        │
└─────────────────┘

┌─────────────────┐
│      ADMIN      │
├─────────────────┤
│ Pode:           │
│ • Criar         │
│ • Editar        │
│ • Remover       │
│ • Consultar     │
│ (materiais e    │
│  locais)        │
└─────────────────┘
```

---

## 🔑 Autenticação e Sessão

### 1. **Login do Usuário**

```javascript
// POST /usuarios/autenticar
fetch('/usuarios/autenticar', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: new URLSearchParams({
        emailUsuario: 'usuario@email.com',
        senhaUsuario: 'senha123'
    })
})
.then(response => {
    if (response.ok) {
        // Login bem-sucedido, sessão criada
        // Redirecionar para dashboard
        window.location.href = '/dashboard';
    } else {
        console.error('Email ou senha inválidos');
    }
})
.catch(error => console.error('Erro:', error));
```

### 2. **Recuperar Dados do Usuário Logado**

Uma vez autenticado, o usuário fica armazenado na sessão do servidor. Você pode acessar via Thymeleaf:

```html
<!-- No template -->
<span th:text="${session.nomeUsuario}"></span>
```

---

## 📚 APIs de MATERIAIS

### **GET /materiais/api/listar** - Listar todos os materiais

```javascript
// Requisição
fetch('/materiais/api/listar')
    .then(response => response.json())
    .then(materiais => {
        console.log(materiais);
        // Resposta:
        // [
        //   {
        //     "id": 1,
        //     "nome": "Plástico",
        //     "descricao": "Garrafas, sacolas...",
        //     "reciclavel": true
        //   },
        //   ...
        // ]
    });
```

### **GET /materiais/api/reciclaveis** - Listar apenas recicláveis

```javascript
fetch('/materiais/api/reciclaveis')
    .then(response => response.json())
    .then(materiais => console.log(materiais));
```

### **GET /materiais/api/{id}** - Buscar material por ID

```javascript
fetch('/materiais/api/1')
    .then(response => response.json())
    .then(material => {
        console.log(material);
        // Resposta:
        // {
        //   "id": 1,
        //   "nome": "Plástico",
        //   "descricao": "...",
        //   "reciclavel": true
        // }
    });
```

### **POST /materiais/api/criar** - Criar material (ADMIN apenas)

```javascript
// Requisição
const novoMaterial = {
    nome: "Vidro",
    descricao: "Garrafas, potes...",
    reciclavel: true
};

fetch('/materiais/api/criar', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(novoMaterial)
})
.then(response => response.json())
.then(data => {
    if (data.sucesso) {
        console.log('Material criado:', data.material);
        // Resposta:
        // {
        //   "sucesso": true,
        //   "mensagem": "Material criado com sucesso",
        //   "material": {...}
        // }
    } else {
        console.error('Erro:', data.erro);
    }
})
.catch(error => {
    if (error.status === 403) {
        alert('Acesso negado! Apenas ADMIN pode criar materiais.');
    }
});
```

### **PUT /materiais/api/{id}** - Atualizar material (ADMIN apenas)

```javascript
const materialAtualizado = {
    nome: "Vidro Reciclado",
    descricao: "Garrafas de vidro transparente",
    reciclavel: true
};

fetch('/materiais/api/1', {
    method: 'PUT',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(materialAtualizado)
})
.then(response => response.json())
.then(data => console.log(data));
```

### **DELETE /materiais/api/{id}** - Deletar material (ADMIN apenas)

```javascript
fetch('/materiais/api/1', {
    method: 'DELETE'
})
.then(response => response.json())
.then(data => {
    if (data.sucesso) {
        console.log('Material deletado');
    } else {
        console.error('Erro:', data.erro);
    }
});
```

---

## 📍 APIs de LOCAIS DE DESCARTE

### **GET /locais-descarte/api/listar** - Listar todos os locais

```javascript
fetch('/locais-descarte/api/listar')
    .then(response => response.json())
    .then(locais => {
        console.log(locais);
        // Resposta:
        // [
        //   {
        //     "id": 1,
        //     "nome": "Eco Ponto Centro",
        //     "horarioAbertura": "08:00:00",
        //     "horarioFechamento": "18:00:00",
        //     "contatoTelefone": "(11) 3333-3333",
        //     "contatoWhatsApp": "(11) 99999-9999",
        //     "contatoEmail": "ecocentro@email.com",
        //     "descricao": "Ponto de coleta seletiva",
        //     "endereco": {...},
        //     "materiais": [...],
        //     "tiposDescarte": [...]
        //   },
        //   ...
        // ]
    });
```

### **GET /locais-descarte/api/{id}** - Buscar local por ID

```javascript
fetch('/locais-descarte/api/1')
    .then(response => response.json())
    .then(local => console.log(local));
```

### **POST /locais-descarte/api/criar** - Criar local (ADMIN apenas)

```javascript
const novoLocal = {
    nome: "Ecocentro Norte",
    contatoEmail: "econorte@email.com",
    contatoTelefone: "(11) 2222-2222",
    contatoWhatsApp: "(11) 98888-8888",
    descricao: "Ponto de coleta seletiva na zona norte"
};

fetch('/locais-descarte/api/criar', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(novoLocal)
})
.then(response => response.json())
.then(data => console.log(data));
```

### **PUT /locais-descarte/api/{id}** - Atualizar local (ADMIN apenas)

```javascript
const localAtualizado = {
    nome: "Ecocentro Norte - Atualizado",
    contatoEmail: "econorte.novo@email.com",
    contatoTelefone: "(11) 2222-2222",
    contatoWhatsApp: "(11) 98888-8888",
    descricao: "Atualizado em 2025"
};

fetch('/locais-descarte/api/1', {
    method: 'PUT',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(localAtualizado)
})
.then(response => response.json())
.then(data => console.log(data));
```

### **DELETE /locais-descarte/api/{id}** - Deletar local (ADMIN apenas)

```javascript
fetch('/locais-descarte/api/1', {
    method: 'DELETE'
})
.then(response => response.json())
.then(data => console.log(data));
```

---

## 🚨 Tratamento de Erros

### **Erro 401 - Não Autenticado**
```javascript
fetch('/materiais/api/criar', {...})
    .then(response => {
        if (response.status === 401) {
            // Usuário não autenticado
            window.location.href = '/usuarios/login';
        }
    });
```

### **Erro 403 - Acesso Negado (sem permissão ADMIN)**
```javascript
fetch('/materiais/api/criar', {...})
    .then(response => {
        if (response.status === 403) {
            // Usuário não tem role ADMIN
            alert('Apenas administradores podem criar materiais');
        }
    });
```

### **Erro 400 - Validação Falhou**
```javascript
fetch('/materiais/api/criar', {...})
    .then(response => response.json())
    .then(data => {
        if (!data.sucesso) {
            // Mostrar erro de validação
            console.error(data.erro);
            // Exemplo: "Material com este nome já existe"
        }
    });
```

---

## 💡 Exemplo Completo: CRUD de Materiais com AJAX

### HTML
```html
<!-- Botão para criar novo material (apenas para ADMIN) -->
<button id="btnNovoMaterial">+ Novo Material</button>

<!-- Lista de materiais -->
<table id="tabelaMateriais">
    <thead>
        <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Descrição</th>
            <th>Reciclável</th>
            <th>Ações</th>
        </tr>
    </thead>
    <tbody id="corpoDados"></tbody>
</table>
```

### JavaScript
```javascript
// Listar materiais ao carregar a página
function listarMateriais() {
    fetch('/materiais/api/listar')
        .then(response => response.json())
        .then(materiais => {
            const tbody = document.getElementById('corpoDados');
            tbody.innerHTML = '';
            
            materiais.forEach(material => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${material.id}</td>
                    <td>${material.nome}</td>
                    <td>${material.descricao || '-'}</td>
                    <td>${material.reciclavel ? 'Sim' : 'Não'}</td>
                    <td>
                        <button onclick="editarMaterial(${material.id})">Editar</button>
                        <button onclick="deletarMaterial(${material.id})">Deletar</button>
                    </td>
                `;
                tbody.appendChild(tr);
            });
        });
}

// Criar novo material
function criarMaterial() {
    const nome = prompt('Nome do material:');
    if (!nome) return;
    
    const descricao = prompt('Descrição:');
    const reciclavel = confirm('É reciclável?');
    
    fetch('/materiais/api/criar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            nome: nome,
            descricao: descricao,
            reciclavel: reciclavel
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.sucesso) {
            alert('Material criado com sucesso!');
            listarMateriais(); // Recarregar lista
        } else {
            alert('Erro: ' + data.erro);
        }
    });
}

// Deletar material
function deletarMaterial(id) {
    if (!confirm('Tem certeza que deseja deletar este material?')) return;
    
    fetch(`/materiais/api/${id}`, { method: 'DELETE' })
        .then(response => response.json())
        .then(data => {
            if (data.sucesso) {
                alert('Material deletado!');
                listarMateriais();
            } else {
                alert('Erro: ' + data.erro);
            }
        });
}

// Event listener
document.getElementById('btnNovoMaterial').addEventListener('click', criarMaterial);

// Carregar materiais ao abrir página
listarMateriais();
```

---

## 🔄 Fluxo de Integração Recomendado

```
1. Frontend carrega página de materiais
   ↓
2. JavaScript faz fetch para /materiais/api/listar
   ↓
3. Backend valida autenticação (session)
   ↓
4. Backend retorna JSON com materiais
   ↓
5. Frontend processa JSON e renderiza tabela
   ↓
6. Usuário clica em "Novo Material"
   ↓
7. Frontend abre modal/formulário
   ↓
8. Usuário preenche dados
   ↓
9. Frontend faz POST para /materiais/api/criar
   ↓
10. Backend valida role (ADMIN?) e dados
   ↓
11. Backend salva no banco e retorna {sucesso: true}
   ↓
12. Frontend recarrega lista com listarMateriais()
```

---

## 📋 Checklist de Implementação Frontend

- [ ] **Página de Login**
  - [ ] Formulário com email e senha
  - [ ] POST para `/usuarios/autenticar`
  - [ ] Redirecionar para dashboard se sucesso

- [ ] **Dashboard/Home**
  - [ ] Mostrar nome do usuário (usar `session.nomeUsuario`)
  - [ ] Link para logout (`/usuarios/logout`)
  - [ ] Menu com links para Materiais e Locais

- [ ] **Página de Materiais (Consultar)**
  - [ ] Fetch para `/materiais/api/listar`
  - [ ] Tabela/card para exibir materiais
  - [ ] Filtro por "Recicláveis" (opcional)

- [ ] **Página de Criação/Edição de Materiais (ADMIN)**
  - [ ] Formulário com: Nome, Descrição, Reciclável
  - [ ] POST para `/materiais/api/criar` ou PUT para `/materiais/api/{id}`
  - [ ] Validação no frontend (nomes não vazios)

- [ ] **Página de Locais de Descarte (Consultar)**
  - [ ] Fetch para `/locais-descarte/api/listar`
  - [ ] Exibir locais em mapa (opcional com Google Maps)
  - [ ] Horários e contatos

- [ ] **Página de Criação/Edição de Locais (ADMIN)**
  - [ ] Formulário com: Nome, Email, Telefone, WhatsApp, Descrição
  - [ ] POST para `/locais-descarte/api/criar` ou PUT

---

## 🔗 URLs Resumidas

| Função | Método | URL |
|--------|--------|-----|
| Listar materiais | GET | `/materiais/api/listar` |
| Criar material | POST | `/materiais/api/criar` |
| Atualizar material | PUT | `/materiais/api/{id}` |
| Deletar material | DELETE | `/materiais/api/{id}` |
| Listar locais | GET | `/locais-descarte/api/listar` |
| Criar local | POST | `/locais-descarte/api/criar` |
| Atualizar local | PUT | `/locais-descarte/api/{id}` |
| Deletar local | DELETE | `/locais-descarte/api/{id}` |
| Login | POST | `/usuarios/autenticar` |
| Logout | GET | `/usuarios/logout` |

---

## ⚙️ Configuração de CORS (se necessário)

Se o frontend estiver em domínio diferente (ex: `localhost:3000` enquanto backend está em `localhost:8080`):

Crie um arquivo `CorsConfig.java`:

```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/materiais/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*")
                    .allowCredentials(true);
                
                registry.addMapping("/locais-descarte/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }
}
```

---

## 📞 Suporte

Se tiver dúvidas durante a integração:
- Verifique o console do navegador (F12 → Console) para erros
- Use o Network tab para inspecionar requisições/respostas
- Verifique se o usuário está autenticado (verifique sessão)
- Verifique se o usuário tem role ADMIN para operações sensíveis

---

📝 **Criado em:** 17 de novembro de 2025
