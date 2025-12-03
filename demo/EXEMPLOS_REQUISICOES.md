# 📡 Exemplos de Requisições e Respostas da API

## 🔐 Autenticação

### **Login (POST /usuarios/autenticar)**

**Requisição:**
```http
POST /usuarios/autenticar HTTP/1.1
Content-Type: application/x-www-form-urlencoded

emailUsuario=joao@email.com&senhaUsuario=senha123
```

**Resposta OK (302 Redirect):**
```http
HTTP/1.1 302 Found
Location: /dashboard
Set-Cookie: JSESSIONID=ABC123...; Path=/; HttpOnly
```

**Resposta Erro:**
```http
HTTP/1.1 302 Found
Location: /usuarios/login?erro=true
```

---

### **Logout (GET /usuarios/logout)**

**Requisição:**
```http
GET /usuarios/logout HTTP/1.1
```

**Resposta:**
```http
HTTP/1.1 302 Found
Location: /usuarios/login
Set-Cookie: JSESSIONID=; Path=/; Max-Age=0
```

---

## 📦 Materiais

### **Listar Todos (GET /materiais/api/listar)**

**Requisição:**
```javascript
fetch('/materiais/api/listar')
    .then(r => r.json())
    .then(data => console.log(data))
```

**Resposta 200 OK:**
```json
[
  {
    "id": 1,
    "nome": "Plástico",
    "descricao": "Garrafas, sacolas, potes...",
    "reciclavel": true
  },
  {
    "id": 2,
    "nome": "Vidro",
    "descricao": "Garrafas, potes de vidro",
    "reciclavel": true
  },
  {
    "id": 3,
    "nome": "Metal",
    "descricao": "Latas, potes metálicos",
    "reciclavel": true
  }
]
```

---

### **Buscar por ID (GET /materiais/api/{id})**

**Requisição:**
```javascript
fetch('/materiais/api/1')
    .then(r => r.json())
    .then(data => console.log(data))
```

**Resposta 200 OK:**
```json
{
  "id": 1,
  "nome": "Plástico",
  "descricao": "Garrafas, sacolas, potes...",
  "reciclavel": true
}
```

**Resposta 404 Not Found:**
```json
```
(corpo vazio com HTTP 404)

---

### **Listar Recicláveis (GET /materiais/api/reciclaveis)**

**Requisição:**
```javascript
fetch('/materiais/api/reciclaveis')
    .then(r => r.json())
    .then(data => console.log(data))
```

**Resposta 200 OK:**
```json
[
  {
    "id": 1,
    "nome": "Plástico",
    "descricao": "...",
    "reciclavel": true
  },
  {
    "id": 2,
    "nome": "Vidro",
    "descricao": "...",
    "reciclavel": true
  }
]
```

---

### **Criar Material (POST /materiais/api/criar) - ADMIN**

**Requisição:**
```javascript
fetch('/materiais/api/criar', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
        nome: 'Papel',
        descricao: 'Jornais, revistas, papelão',
        reciclavel: true
    })
})
.then(r => r.json())
.then(data => console.log(data))
```

**Resposta 201 Created (Sucesso):**
```json
{
  "sucesso": true,
  "mensagem": "Material criado com sucesso",
  "material": {
    "id": 4,
    "nome": "Papel",
    "descricao": "Jornais, revistas, papelão",
    "reciclavel": true
  }
}
```

**Resposta 400 Bad Request (Email duplicado):**
```json
{
  "sucesso": false,
  "erro": "Material com este nome já existe"
}
```

**Resposta 403 Forbidden (Não é ADMIN):**
```json
```
(corpo vazio com HTTP 403)

**Resposta 401 Unauthorized (Não autenticado):**
```json
```
(corpo vazio com HTTP 401)

---

### **Atualizar Material (PUT /materiais/api/{id}) - ADMIN**

**Requisição:**
```javascript
fetch('/materiais/api/1', {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
        nome: 'Plástico Reciclado',
        descricao: 'Garrafas e sacolas de plástico reciclado',
        reciclavel: true
    })
})
.then(r => r.json())
.then(data => console.log(data))
```

**Resposta 200 OK (Sucesso):**
```json
{
  "sucesso": true,
  "mensagem": "Material atualizado com sucesso",
  "material": {
    "id": 1,
    "nome": "Plástico Reciclado",
    "descricao": "Garrafas e sacolas de plástico reciclado",
    "reciclavel": true
  }
}
```

**Resposta 400 Bad Request (Material não encontrado):**
```json
{
  "sucesso": false,
  "erro": "Material não encontrado com ID: 999"
}
```

---

### **Deletar Material (DELETE /materiais/api/{id}) - ADMIN**

**Requisição:**
```javascript
fetch('/materiais/api/1', { method: 'DELETE' })
    .then(r => r.json())
    .then(data => console.log(data))
```

**Resposta 200 OK (Sucesso):**
```json
{
  "sucesso": true,
  "mensagem": "Material deletado com sucesso"
}
```

**Resposta 400 Bad Request (Não encontrado):**
```json
{
  "sucesso": false,
  "erro": "Material não encontrado com ID: 999"
}
```

---

## 📍 Locais de Descarte

### **Listar Todos (GET /locais-descarte/api/listar)**

**Requisição:**
```javascript
fetch('/locais-descarte/api/listar')
    .then(r => r.json())
    .then(data => console.log(data))
```

**Resposta 200 OK:**
```json
[
  {
    "id": 1,
    "nome": "Ecocentro Centro",
    "horarioAbertura": "08:00:00",
    "horarioFechamento": "18:00:00",
    "contatoTelefone": "(11) 3333-3333",
    "contatoWhatsApp": "(11) 99999-9999",
    "contatoEmail": "ecocentro@email.com",
    "descricao": "Ponto de coleta seletiva no centro",
    "endereco": {
      "id": 1,
      "logradouro": "Rua das Flores",
      "numero": 100,
      "complemento": "Próximo ao metrô",
      "bairro": "Centro",
      "cidade": "São Paulo",
      "estado": "SP",
      "cep": "01311-100"
    },
    "materiais": [
      {"id": 1, "nome": "Plástico"},
      {"id": 2, "nome": "Vidro"}
    ],
    "tiposDescarte": []
  },
  {
    "id": 2,
    "nome": "Ecocentro Norte",
    "horarioAbertura": "09:00:00",
    "horarioFechamento": "17:00:00",
    "contatoTelefone": "(11) 2222-2222",
    "contatoWhatsApp": "(11) 98888-8888",
    "contatoEmail": "econorte@email.com",
    "descricao": "Ponto de coleta seletiva na zona norte",
    "endereco": {...},
    "materiais": [...],
    "tiposDescarte": []
  }
]
```

---

### **Buscar por ID (GET /locais-descarte/api/{id})**

**Requisição:**
```javascript
fetch('/locais-descarte/api/1')
    .then(r => r.json())
    .then(data => console.log(data))
```

**Resposta 200 OK:**
```json
{
  "id": 1,
  "nome": "Ecocentro Centro",
  "horarioAbertura": "08:00:00",
  "horarioFechamento": "18:00:00",
  "contatoTelefone": "(11) 3333-3333",
  "contatoWhatsApp": "(11) 99999-9999",
  "contatoEmail": "ecocentro@email.com",
  "descricao": "Ponto de coleta seletiva no centro",
  "endereco": {
    "id": 1,
    "logradouro": "Rua das Flores",
    "numero": 100,
    "complemento": "Próximo ao metrô",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01311-100"
  },
  "materiais": [
    {"id": 1, "nome": "Plástico"},
    {"id": 2, "nome": "Vidro"}
  ],
  "tiposDescarte": []
}
```

---

### **Criar Local (POST /locais-descarte/api/criar) - ADMIN**

**Requisição:**
```javascript
fetch('/locais-descarte/api/criar', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
        nome: 'Ecocentro Sul',
        contatoTelefone: '(11) 1111-1111',
        contatoWhatsApp: '(11) 91111-1111',
        contatoEmail: 'ecosul@email.com',
        descricao: 'Ponto de coleta na zona sul',
        horarioAbertura: '08:00:00',
        horarioFechamento: '18:00:00'
    })
})
.then(r => r.json())
.then(data => console.log(data))
```

**Resposta 201 Created (Sucesso):**
```json
{
  "sucesso": true,
  "mensagem": "Local de descarte criado com sucesso",
  "local": {
    "id": 3,
    "nome": "Ecocentro Sul",
    "contatoTelefone": "(11) 1111-1111",
    "contatoWhatsApp": "(11) 91111-1111",
    "contatoEmail": "ecosul@email.com",
    "descricao": "Ponto de coleta na zona sul",
    "horarioAbertura": "08:00:00",
    "horarioFechamento": "18:00:00",
    "endereco": null,
    "materiais": [],
    "tiposDescarte": []
  }
}
```

**Resposta 400 Bad Request (Email duplicado):**
```json
{
  "sucesso": false,
  "erro": "E-mail já cadastrado no sistema"
}
```

---

### **Atualizar Local (PUT /locais-descarte/api/{id}) - ADMIN**

**Requisição:**
```javascript
fetch('/locais-descarte/api/1', {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
        nome: 'Ecocentro Centro - Novo Endereço',
        contatoTelefone: '(11) 3333-3333',
        contatoWhatsApp: '(11) 99999-9999',
        contatoEmail: 'ecocentro@email.com',
        descricao: 'Novo local de coleta no centro',
        horarioAbertura: '07:00:00',
        horarioFechamento: '19:00:00'
    })
})
.then(r => r.json())
.then(data => console.log(data))
```

**Resposta 200 OK (Sucesso):**
```json
{
  "sucesso": true,
  "mensagem": "Local de descarte atualizado com sucesso",
  "local": {
    "id": 1,
    "nome": "Ecocentro Centro - Novo Endereço",
    "horarioAbertura": "07:00:00",
    "horarioFechamento": "19:00:00",
    "contatoTelefone": "(11) 3333-3333",
    "contatoWhatsApp": "(11) 99999-9999",
    "contatoEmail": "ecocentro@email.com",
    "descricao": "Novo local de coleta no centro"
  }
}
```

---

### **Deletar Local (DELETE /locais-descarte/api/{id}) - ADMIN**

**Requisição:**
```javascript
fetch('/locais-descarte/api/1', { method: 'DELETE' })
    .then(r => r.json())
    .then(data => console.log(data))
```

**Resposta 200 OK (Sucesso):**
```json
{
  "sucesso": true,
  "mensagem": "Local de descarte deletado com sucesso"
}
```

---

## ⚠️ Códigos de Erro

| Código | Significado | Exemplo |
|--------|-----------|---------|
| 200 | OK | GET bem-sucedido |
| 201 | Created | POST bem-sucedido |
| 302 | Redirect | Login com sucesso → /dashboard |
| 400 | Bad Request | Dados inválidos, validação falhou |
| 401 | Unauthorized | Não autenticado (não fez login) |
| 403 | Forbidden | Autenticado mas sem permissão (não é ADMIN) |
| 404 | Not Found | Recurso não encontrado |
| 500 | Internal Server Error | Erro no servidor |

---

## 🔄 Fluxo: Criar e Listar Materiais

### **Step-by-Step**

```
1. USER acessa http://localhost:8080/materiais/api/listar
   └─ Recebe lista de materiais em JSON

2. ADMIN acessa http://localhost:8080/materiais/api/criar
   └─ POST com JSON do novo material
   └─ Recebe HTTP 201 com material criado

3. USER tenta acessar http://localhost:8080/materiais/api/criar
   └─ Recebe HTTP 403 Forbidden
   └─ Não pode criar (não é ADMIN)

4. USER não autenticado tenta acessar /materiais/api/listar
   └─ Recebe HTTP 401 Unauthorized
   └─ Redireciona para /usuarios/login
```

---

## 🧪 Testar com cURL

### **Listar Materiais**
```bash
curl -X GET http://localhost:8080/materiais/api/listar \
  -H "Content-Type: application/json" \
  -b "JSESSIONID=seu_session_id"
```

### **Criar Material**
```bash
curl -X POST http://localhost:8080/materiais/api/criar \
  -H "Content-Type: application/json" \
  -b "JSESSIONID=seu_session_id" \
  -d '{
    "nome": "Papel",
    "descricao": "Jornais e revistas",
    "reciclavel": true
  }'
```

### **Deletar Material**
```bash
curl -X DELETE http://localhost:8080/materiais/api/1 \
  -H "Content-Type: application/json" \
  -b "JSESSIONID=seu_session_id"
```

---

## 📊 Resposta Padrão para Erros

### **Validação (400)**
```json
{
  "sucesso": false,
  "erro": "Material com este nome já existe"
}
```

### **Autorização (403)**
```
HTTP 403 Forbidden
(corpo vazio)
```

### **Não Autenticado (401)**
```
HTTP 401 Unauthorized
(corpo vazio)
```

---

## ✅ Dicas de Teste

1. **Use ferramentas de teste:** Postman, Insomnia, Thunder Client
2. **Mantenha cookies:** Use `set-cookie` para manter sessão
3. **Teste erros:** Tente acessar como USER, depois como ADMIN
4. **Valide JSON:** Use `JSON.stringify()` no JavaScript
5. **Veja console:** F12 → Network tab para inspecionar requisições

---

📝 Criado em: 17 de novembro de 2025
