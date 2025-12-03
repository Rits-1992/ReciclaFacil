# 🚀 GUIA RÁPIDO - Uma Página

## 🔐 Autenticação

```
LOGIN
URL: POST /usuarios/autenticar
Body: emailUsuario=email&senhaUsuario=senha
Response: Redirect ou Erro

LOGOUT
URL: GET /usuarios/logout
Response: Redirect para /login
```

---

## 📊 Permissões

```
┌─────────────┬──────────────────┐
│ USER        │ ADMIN            │
├─────────────┼──────────────────┤
│ ✅ Listar   │ ✅ Listar        │
│ ❌ Criar    │ ✅ Criar         │
│ ❌ Editar   │ ✅ Editar        │
│ ❌ Deletar  │ ✅ Deletar       │
└─────────────┴──────────────────┘
```

---

## 📚 Materiais - Endpoints

| Ação | Método | URL | Role |
|------|--------|-----|------|
| Listar | GET | `/materiais/api/listar` | USER/ADMIN |
| Por ID | GET | `/materiais/api/{id}` | USER/ADMIN |
| Recicláveis | GET | `/materiais/api/reciclaveis` | USER/ADMIN |
| Criar | POST | `/materiais/api/criar` | **ADMIN** |
| Editar | PUT | `/materiais/api/{id}` | **ADMIN** |
| Deletar | DELETE | `/materiais/api/{id}` | **ADMIN** |

---

## 📍 Locais - Endpoints

| Ação | Método | URL | Role |
|------|--------|-----|------|
| Listar | GET | `/locais-descarte/api/listar` | USER/ADMIN |
| Por ID | GET | `/locais-descarte/api/{id}` | USER/ADMIN |
| Criar | POST | `/locais-descarte/api/criar` | **ADMIN** |
| Editar | PUT | `/locais-descarte/api/{id}` | **ADMIN** |
| Deletar | DELETE | `/locais-descarte/api/{id}` | **ADMIN** |

---

## 💻 JavaScript - Exemplos

### Listar
```javascript
fetch('/materiais/api/listar')
  .then(r => r.json())
  .then(data => console.log(data))
```

### Criar
```javascript
fetch('/materiais/api/criar', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    nome: 'Papel',
    descricao: 'Jornais',
    reciclavel: true
  })
})
.then(r => r.json())
.then(data => console.log(data))
```

### Deletar
```javascript
fetch('/materiais/api/1', { method: 'DELETE' })
  .then(r => r.json())
  .then(data => console.log(data))
```

---

## ⚠️ Erros

| Código | Significado |
|--------|------------|
| 200 | OK |
| 201 | Criado |
| 400 | Dados inválidos |
| 401 | Não autenticado |
| 403 | Sem permissão |
| 404 | Não encontrado |

---

## 📝 JSON - Exemplo Material

```json
{
  "id": 1,
  "nome": "Plástico",
  "descricao": "Garrafas, sacolas",
  "reciclavel": true
}
```

---

## 📍 JSON - Exemplo Local

```json
{
  "id": 1,
  "nome": "Ecocentro Centro",
  "horarioAbertura": "08:00:00",
  "horarioFechamento": "18:00:00",
  "contatoTelefone": "(11) 3333-3333",
  "contatoWhatsApp": "(11) 99999-9999",
  "contatoEmail": "eco@email.com",
  "descricao": "Coleta seletiva"
}
```

---

## 🔄 Fluxo Rápido

```
1. USER → /usuarios/login (formulário)
2. POST /usuarios/autenticar → sessão criada
3. GET /materiais/api/listar → JSON com materiais
4. ADMIN → POST /materiais/api/criar → novo material

Se USER tentar criar:
→ 403 FORBIDDEN (sem permissão)

Se não autenticado tentar acessar:
→ 401 UNAUTHORIZED
```

---

## ✅ Checklist Integração

- [ ] Login funciona
- [ ] Listar materiais funciona
- [ ] Criar material (ADMIN) funciona
- [ ] Editar material (ADMIN) funciona
- [ ] Deletar material (ADMIN) funciona
- [ ] USER não consegue criar/editar/deletar
- [ ] Erros aparecem corretamente

---

## 📂 Arquivos Importantes

| Arquivo | O que faz |
|---------|----------|
| `INTEGRACAO_FRONTEND.md` | Como integrar com AJAX |
| `SISTEMA_AUTENTICACAO.md` | Segurança e roles |
| `EXEMPLOS_REQUISICOES.md` | Exemplos de respostas |
| `README.md` | Overview geral |

---

**Compilação:** ✅ OK
**Deploy:** Ready
**Frontend:** Pronto para integrar 🚀
