# 🎯 PRÓXIMOS PASSOS - Para Desenvolvedor Frontend

## 📝 Roteiro de Desenvolvimento

Você receberá um backend 100% pronto. Aqui está como proceder:

---

## **FASE 1: Configuração Inicial (30 min)**

### 1.1 Entender o Backend
- [ ] Ler **GUIA_UMA_PAGINA.md** (5 min)
- [ ] Ler **README.md** (15 min)
- [ ] Entender os 3 endpoints principais

### 1.2 Testar Endpoints com Postman
- [ ] Baixar Postman (ou usar Thunder Client no VSCode)
- [ ] Testar POST /usuarios/autenticar
- [ ] Testar GET /materiais/api/listar
- [ ] Testar POST /materiais/api/criar (como ADMIN)
- [ ] Verificar resposta JSON

### 1.3 Entender Autenticação
- [ ] Ler **SISTEMA_AUTENTICACAO.md** (20 min)
- [ ] Entender cookies/sessão
- [ ] Entender roles (USER vs ADMIN)

---

## **FASE 2: Estrutura HTML (1-2 horas)**

### 2.1 Criar Páginas Base

```html
<!-- login.html -->
<form id="formLogin">
  <input type="email" id="email" placeholder="Email">
  <input type="password" id="senha" placeholder="Senha">
  <button type="submit">Entrar</button>
</form>

<!-- dashboard.html (após login) -->
<div id="userInfo">
  <span id="nomeUser"></span>
  <button id="btnLogout">Sair</button>
</div>

<!-- materiais.html -->
<table id="tabelaMateriais">
  <thead>
    <tr>
      <th>Nome</th>
      <th>Descrição</th>
      <th>Reciclável</th>
      <th id="colAcoes" style="display:none">Ações</th>
    </tr>
  </thead>
  <tbody id="corpoDados"></tbody>
</table>

<!-- locais.html -->
<div id="listaLocais"></div>
```

---

## **FASE 3: JavaScript - Login/Logout (1-2 horas)**

### 3.1 Função de Login

```javascript
// auth.js
async function fazerLogin(email, senha) {
  const response = await fetch('/usuarios/autenticar', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: `emailUsuario=${email}&senhaUsuario=${senha}`
  });
  
  if (response.ok) {
    // Salvar informações
    localStorage.setItem('autenticado', 'true');
    window.location.href = '/dashboard.html';
  } else {
    alert('Email ou senha inválidos');
  }
}

// Usar em login.html
document.getElementById('formLogin').addEventListener('submit', (e) => {
  e.preventDefault();
  const email = document.getElementById('email').value;
  const senha = document.getElementById('senha').value;
  fazerLogin(email, senha);
});
```

### 3.2 Função de Logout

```javascript
async function fazerLogout() {
  await fetch('/usuarios/logout');
  localStorage.removeItem('autenticado');
  window.location.href = '/login.html';
}

document.getElementById('btnLogout').addEventListener('click', fazerLogout);
```

### 3.3 Verificar Autenticação

```javascript
// Adicionar no início de cada página protegida
function verificarAutenticacao() {
  if (!localStorage.getItem('autenticado')) {
    window.location.href = '/login.html';
  }
}

verificarAutenticacao();
```

---

## **FASE 4: JavaScript - Listar Materiais (1-2 horas)**

### 4.1 Função para Listar

```javascript
// materiais.js
let isAdmin = false; // determinar depois

async function carregarMateriais() {
  try {
    const response = await fetch('/materiais/api/listar');
    
    if (response.status === 401) {
      window.location.href = '/login.html';
      return;
    }
    
    const materiais = await response.json();
    exibirMateriais(materiais);
  } catch (error) {
    console.error('Erro ao carregar materiais:', error);
    alert('Erro ao carregar materiais');
  }
}

function exibirMateriais(materiais) {
  const tbody = document.getElementById('corpoDados');
  tbody.innerHTML = '';
  
  materiais.forEach(material => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${material.nome}</td>
      <td>${material.descricao || '-'}</td>
      <td>${material.reciclavel ? '✅' : '❌'}</td>
      ${isAdmin ? `
        <td>
          <button onclick="editarMaterial(${material.id})">✏️</button>
          <button onclick="deletarMaterial(${material.id})">🗑️</button>
        </td>
      ` : ''}
    `;
    tbody.appendChild(tr);
  });
  
  // Mostrar coluna de ações apenas se admin
  const colAcoes = document.getElementById('colAcoes');
  if (colAcoes) colAcoes.style.display = isAdmin ? 'table-cell' : 'none';
}

// Carregar ao abrir página
carregarMateriais();
```

---

## **FASE 5: JavaScript - CRUD (2-3 horas)**

### 5.1 Criar Material (Admin)

```javascript
async function criarMaterial() {
  const nome = prompt('Nome do material:');
  if (!nome) return;
  
  const descricao = prompt('Descrição:');
  const reciclavel = confirm('É reciclável?');
  
  const response = await fetch('/materiais/api/criar', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      nome: nome,
      descricao: descricao,
      reciclavel: reciclavel
    })
  });
  
  const data = await response.json();
  
  if (data.sucesso) {
    alert('Material criado com sucesso!');
    carregarMateriais(); // Recarregar lista
  } else {
    alert('Erro: ' + data.erro);
  }
}

// Botão para criar
document.getElementById('btnNovoMaterial')?.addEventListener('click', criarMaterial);
```

### 5.2 Editar Material (Admin)

```javascript
async function editarMaterial(id) {
  const novoNome = prompt('Novo nome:');
  if (!novoNome) return;
  
  const novaDescricao = prompt('Nova descrição:');
  const novoReciclavel = confirm('É reciclável?');
  
  const response = await fetch(`/materiais/api/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      nome: novoNome,
      descricao: novaDescricao,
      reciclavel: novoReciclavel
    })
  });
  
  const data = await response.json();
  
  if (data.sucesso) {
    alert('Material atualizado!');
    carregarMateriais();
  } else {
    alert('Erro: ' + data.erro);
  }
}
```

### 5.3 Deletar Material (Admin)

```javascript
async function deletarMaterial(id) {
  if (!confirm('Tem certeza que deseja deletar?')) return;
  
  const response = await fetch(`/materiais/api/${id}`, {
    method: 'DELETE'
  });
  
  const data = await response.json();
  
  if (data.sucesso) {
    alert('Material deletado!');
    carregarMateriais();
  } else {
    alert('Erro: ' + data.erro);
  }
}
```

---

## **FASE 6: Página de Locais de Descarte (1-2 horas)**

```javascript
// locais.js
async function carregarLocais() {
  const response = await fetch('/locais-descarte/api/listar');
  const locais = await response.json();
  
  const container = document.getElementById('listaLocais');
  container.innerHTML = '';
  
  locais.forEach(local => {
    const card = document.createElement('div');
    card.className = 'card-local';
    card.innerHTML = `
      <h3>${local.nome}</h3>
      <p><strong>Email:</strong> ${local.contatoEmail}</p>
      <p><strong>Telefone:</strong> ${local.contatoTelefone}</p>
      <p><strong>WhatsApp:</strong> ${local.contatoWhatsApp}</p>
      <p><strong>Horário:</strong> ${local.horarioAbertura} - ${local.horarioFechamento}</p>
      <p>${local.descricao}</p>
      ${isAdmin ? `
        <button onclick="editarLocal(${local.id})">Editar</button>
        <button onclick="deletarLocal(${local.id})">Deletar</button>
      ` : ''}
    `;
    container.appendChild(card);
  });
}

carregarLocais();
```

---

## **FASE 7: Integração com Mapa (Opcional - 2-4 horas)**

```html
<!-- Adicionar Google Maps -->
<script src="https://maps.googleapis.com/maps/api/js?key=SUA_API_KEY"></script>

<div id="mapa" style="width: 100%; height: 500px;"></div>
```

```javascript
let mapa;

function inicializarMapa() {
  mapa = new google.maps.Map(document.getElementById('mapa'), {
    zoom: 12,
    center: { lat: -23.5505, lng: -46.6333 } // São Paulo
  });
  
  carregarLocaisNoMapa();
}

async function carregarLocaisNoMapa() {
  const response = await fetch('/locais-descarte/api/listar');
  const locais = await response.json();
  
  locais.forEach(local => {
    if (local.endereco) {
      new google.maps.Marker({
        position: { lat: local.endereco.latitude, lng: local.endereco.longitude },
        map: mapa,
        title: local.nome
      });
    }
  });
}

inicializarMapa();
```

---

## **FASE 8: Determinação de Permissões (1 hora)**

```javascript
// Verificar se é ADMIN ao fazer login
async function verificarPermissoes() {
  // Opção 1: Armazenar na sessão do servidor
  // O backend já armazena em session.setAttribute("tipoUsuario")
  
  // Opção 2: Fazer uma requisição ao backend
  // POST /api/verificar-tipo (novo endpoint, opcional)
  
  // Opção 3: Verificar se consegue criar material
  const testResponse = await fetch('/materiais/api/criar', {
    method: 'OPTIONS'
  });
  
  if (testResponse.status !== 403) {
    isAdmin = true;
    document.getElementById('menuAdmin').style.display = 'block';
  }
}

verificarPermissoes();
```

---

## **FASE 9: Estilos CSS (1-2 horas)**

```css
/* estilos.css */

/* Tabela de Materiais */
#tabelaMateriais {
  width: 100%;
  border-collapse: collapse;
}

#tabelaMateriais th, #tabelaMateriais td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

#tabelaMateriais th {
  background-color: #667eea;
  color: white;
}

#tabelaMateriais tr:hover {
  background-color: #f5f5f5;
}

/* Cards de Locais */
.card-local {
  background: white;
  padding: 20px;
  margin: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

/* Botões */
button {
  padding: 10px 20px;
  background-color: #667eea;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #764ba2;
}

/* Formulário */
form {
  max-width: 400px;
  margin: 20px auto;
}

input, textarea {
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ddd;
  border-radius: 5px;
}
```

---

## **FASE 10: Testes (1-2 horas)**

### 10.1 Testes Manuais

- [ ] Login com USER válido
- [ ] Logout funciona
- [ ] Listar materiais funciona
- [ ] USER não consegue criar/editar/deletar
- [ ] ADMIN consegue criar/editar/deletar
- [ ] Erros aparecem corretamente
- [ ] Página não quebra se backend cair

### 10.2 Testes Automatizados (Opcional)

```javascript
// testes.js com Jest ou Vitest
describe('Autenticação', () => {
  test('Login deve redirecionar para dashboard', () => {
    // teste...
  });
});
```

---

## **CHECKLIST DE CONCLUSÃO**

- [ ] Phase 1: Configuração ✅
- [ ] Phase 2: HTML ✅
- [ ] Phase 3: Login/Logout ✅
- [ ] Phase 4: Listar ✅
- [ ] Phase 5: CRUD ✅
- [ ] Phase 6: Locais ✅
- [ ] Phase 7: Mapa (opcional) ✅
- [ ] Phase 8: Permissões ✅
- [ ] Phase 9: Estilos ✅
- [ ] Phase 10: Testes ✅
- [ ] Deploy para produção ✅

---

## **TEMPO TOTAL ESTIMADO**

- Sem Mapa: **10-15 horas**
- Com Mapa: **14-20 horas**

---

## **DOCUMENTAÇÃO PARA CONSULTAR**

| Fase | Consulte |
|------|----------|
| 1 | GUIA_UMA_PAGINA.md |
| 2-10 | INTEGRACAO_FRONTEND.md |
| Erros | EXEMPLOS_REQUISICOES.md |
| Segurança | SISTEMA_AUTENTICACAO.md |

---

## **DICAS FINAIS**

1. **Comece pequeno:** Não tente fazer tudo de uma vez
2. **Teste frequentemente:** Use Postman para testar cada endpoint
3. **Use localStorage:** Para manter autenticação entre páginas
4. **Trate erros:** Sempre verifique status 401/403
5. **Organize código:** Use módulos (auth.js, materiais.js, etc)

---

**Boa sorte com o desenvolvimento!** 🚀

Qualquer dúvida, consulte a documentação ou peça ao time de backend.
