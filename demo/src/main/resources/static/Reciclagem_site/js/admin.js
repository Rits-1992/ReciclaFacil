// admin.js - Versão completa para CRUD de Materiais e Pontos de Coleta
const API_BASE = "http://localhost:8080";
const API_MATERIAL = `${API_BASE}/materiais`;
const API_LOCAIS = `${API_BASE}/localDescarte`;
const API_TIPOS = `${API_BASE}/tipoDescarte`;

let editandoProduto = null;
let editandoColeta = null;

// caches
let tiposDescarte = [];
let locaisDescarte = [];

document.addEventListener("DOMContentLoaded", () => {
  esconderModais();
  carregarTiposDescarte();    // carrega tipos (usado em ambos os modais)
  carregarLocaisDescarte();   // carrega locais (usado no modal produto e listagem)
  atualizarProdutos();
  atualizarColeta();
  atualizarMensagens();
});

/* ========================= UI Helpers ========================= */
function mostrarAba(id, event) {
  document.querySelectorAll(".conteudo-aba").forEach(div => div.classList.remove("ativo"));
  document.querySelectorAll(".aba").forEach(btn => btn.classList.remove("ativa"));
  document.getElementById(id).classList.add("ativo");
  if (event) event.target.classList.add("ativa");
}

function esconderModais() {
  document.getElementById("modalProduto").style.display = "none";
  document.getElementById("modalColeta").style.display = "none";
}

// Helpers para mostrar erros no modal de coleta
function clearColetaErrors() {
  const existing = document.getElementById('coletaErrors');
  if (existing) existing.remove();
}

function showColetaErrors(messages) {
  clearColetaErrors();
  const modal = document.querySelector('#modalColeta .modal-content');
  if (!modal) {
    alert(messages.join('\n'));
    return;
  }
  const div = document.createElement('div');
  div.id = 'coletaErrors';
  div.style.background = '#fff3f3';
  div.style.border = '1px solid #ffb3b3';
  div.style.color = '#8a1f1f';
  div.style.padding = '8px 12px';
  div.style.marginBottom = '8px';
  div.style.borderRadius = '4px';
  div.innerHTML = '<strong>Corrija os seguintes erros:</strong><ul style="margin:6px 0 0 18px"></ul>';
  const ul = div.querySelector('ul');
  messages.forEach(m => {
    const li = document.createElement('li');
    li.textContent = m;
    ul.appendChild(li);
  });
  modal.prepend(div);
}

function abrirModalProduto() {
  editandoProduto = null;
  document.getElementById("tituloModalProduto").textContent = "Novo Produto";
  document.getElementById("nomeProduto").value = "";
  document.getElementById("descricaoProduto").value = "";
  document.getElementById("reciclavelProduto").checked = false;
  // garante que selects/checkboxes estão atualizados
  renderTipoDescarteSelect();
  renderLocaisDescarteCheckList();
  document.getElementById("modalProduto").style.display = "flex";
}

function fecharModalProduto() {
  document.getElementById("modalProduto").style.display = "none";
}

function abrirModalColeta() {
  editandoColeta = null;
  document.getElementById("tituloModalColeta").textContent = "Novo Ponto de Coleta";
  document.getElementById("localColeta").value = "";
  document.getElementById("contatoColeta").value = "";
  document.getElementById("emailColeta").value = "";
  document.getElementById("horarioAberturaColeta").value = "";
  document.getElementById("horarioFechamentoColeta").value = "";
  document.getElementById("descricaoColeta").value = "";
  document.getElementById("ruaColeta").value = "";
  document.getElementById("numeroColeta").value = "";
  document.getElementById("bairroColeta").value = "";
  document.getElementById("cidadeColeta").value = "";
  document.getElementById("estadoColeta").value = "";
  document.getElementById("cepColeta").value = "";
  renderTiposDescarteCheckList(); // mostra os tipos no modal coleta
  clearColetaErrors();
  document.getElementById("modalColeta").style.display = "flex";
}

function fecharModalColeta() {
  document.getElementById("modalColeta").style.display = "none";
}

/* ========================= Carregamento de listas do backend ========================= */
async function carregarTiposDescarte() {
  try {
    const res = await fetch(`${API_TIPOS}/admin/listar`)
    if (!res.ok) throw new Error(`Erro ao carregar tipos: ${res.status}`);
    tiposDescarte = await res.json();
    // atualiza listas se os modais estiverem abertos
    renderTipoDescarteSelect();
    renderTiposDescarteCheckList();
  } catch (err) {
    console.error(err);
    tiposDescarte = [];
  }
}

async function carregarLocaisDescarte() {
  try {
    const res = await fetch(`${API_LOCAIS}/admin`);
    if (!res.ok) throw new Error(`Erro ao carregar locais: ${res.status}`);
    locaisDescarte = await res.json();
    renderLocaisDescarteCheckList();
    atualizarColeta(); // atualiza tabela de coleta também
  } catch (err) {
    console.error(err);
    locaisDescarte = [];
  }
}

/* ========================= Renderização de selects/checkboxes ========================= */
function renderTipoDescarteSelect() {
  const select = document.getElementById("tipoDescarteProduto");
  if (!select) return;
  select.innerHTML = "";
  select.innerHTML = `<option value="">-- Escolha um tipo --</option>`;
  tiposDescarte.forEach(t => {
    select.innerHTML += `<option value="${t.id}">${t.nome}</option>`;
  });
}

function renderLocaisDescarteCheckList() {
  const div = document.getElementById("listaLocaisDescarteProduto");
  if (!div) return;
  div.innerHTML = "";
  locaisDescarte.forEach(l => {
    div.innerHTML += `
      <label class="checkbox-item">
        <input type="checkbox" data-id="${l.id}" /> ${l.nome}
      </label>
    `;
  });
}

function renderTiposDescarteCheckList() {
  const div = document.getElementById("listaTiposDescarteColeta");
  if (!div) return;
  div.innerHTML = "";
  tiposDescarte.forEach(t => {
    div.innerHTML += `
      <label class="checkbox-item">
        <input type="checkbox" data-id="${t.id}" /> ${t.nome}
      </label>
    `;
  });
}

/* ========================= PRODUTOS (MATERIAIS) ========================= */
async function atualizarProdutos() {
  try {
    const tbody = document.getElementById("listaProdutos");
    const req = await fetch(`${API_MATERIAL}/admin/listar`);
    if (!req.ok) throw new Error(`Erro ${req.status}`);
    const lista = await req.json();

    tbody.innerHTML = "";
    lista.forEach(p => {
      const locaisNomes = (p.locaisDescarte || []).map(l => l.nome).join(", ");
      const tipoNome = p.tipoDescarte?.nome ?? "";
      tbody.innerHTML += `
        <tr>
          <td>${p.id}</td>
          <td>${p.nome}</td>
          <td>${p.descricao || ""}</td>
          <td>${p.reciclavel ? "Sim" : "Não"}</td>
          <td>${tipoNome}</td>
          <td>${locaisNomes}</td>
          <td>
            <button onclick="editarProduto(${p.id})">Editar</button>
            <button onclick="excluirProduto(${p.id})">Excluir</button>
          </td>
        </tr>
      `;
    });
  } catch (err) {
    console.error("Erro ao carregar produtos:", err);
  }
}

function buscarProduto() {
  const termo = document.getElementById("buscaProduto").value.toLowerCase();
  document.querySelectorAll("#listaProdutos tr").forEach(linha => {
    const nome = linha.children[1]?.textContent.toLowerCase() || "";
    linha.style.display = nome.includes(termo) ? "" : "none";
  });
}

async function editarProduto(id) {
  try {
    const req = await fetch(`${API_MATERIAL}/admin/${id}`);
    if (!req.ok) throw new Error(`Erro ${req.status}`);
    const p = await req.json();
    editandoProduto = id;

    document.getElementById("tituloModalProduto").textContent = "Editar Produto";
    document.getElementById("nomeProduto").value = p.nome || "";
    document.getElementById("descricaoProduto").value = p.descricao || "";
    document.getElementById("reciclavelProduto").checked = !!p.reciclavel;

    // carregar selects/checkboxes atualizados
    await carregarTiposDescarte();
    await carregarLocaisDescarte();

    if (p.tipoDescarte && p.tipoDescarte.id) {
      document.getElementById("tipoDescarteProduto").value = p.tipoDescarte.id;
    }

    // marcar locais
    const checkboxes = document.querySelectorAll("#listaLocaisDescarteProduto input[type='checkbox']");
    checkboxes.forEach(cb => {
      const id = Number(cb.getAttribute("data-id"));
      cb.checked = (p.locaisDescarte || []).some(l => l.id === id);
    });

    document.getElementById("modalProduto").style.display = "flex";
  } catch (err) {
    console.error("Erro ao buscar material:", err);
    alert("Erro ao buscar material. Veja o console.");
  }
}


async function excluirProduto(id) {
  if (!confirm("Deseja excluir este material?")) return;
  try {
    const res = await fetch(`${API_MATERIAL}/admin/${id}`, { method: "DELETE" });
    if (!res.ok) {
      const txt = await res.text();
      alert(`Erro ao excluir: ${res.status}\n${txt}`);
      return;
    }
    atualizarProdutos();
  } catch (err) {
    console.error(err);
    alert("Erro ao excluir material.");
  }
}

/* ========================= PONTOS DE COLETA ========================= */
async function atualizarColeta() {
  try {
    const tbody = document.getElementById("listaColeta");
    // tenta rota /admin/listar e /admin
    let req = await fetch(`${API_LOCAIS}/admin`);
    if (!req.ok) throw new Error(`Erro ${req.status}`);
    const lista = await req.json();

    tbody.innerHTML = "";
    lista.forEach(c => {
      const endereco = c.endereco ? `${c.endereco.rua || ""} ${c.endereco.numero || ""} - ${c.endereco.bairro || ""}` : "";
      const tipos = (c.tiposDescarte || []).map(t => t.nome).join(", ");
      tbody.innerHTML += `
        <tr>
          <td>${c.id}</td>
          <td>${c.nome}</td>
          <td>${endereco}</td>
          <td>${c.contato || ""}<br>${c.email || ""}</td>
          <td>${tipos}</td>
          <td>
            <button onclick="editarColeta(${c.id})">Editar</button>
            <button onclick="excluirColeta(${c.id})">Excluir</button>
          </td>
        </tr>
      `;
    });
  } catch (err) {
    console.error("Erro carregar pontos:", err);
  }
}

function buscarColeta() {
  const termo = document.getElementById("buscaColeta").value.toLowerCase();
  document.querySelectorAll("#listaColeta tr").forEach(linha => {
    const nome = linha.children[1]?.textContent.toLowerCase() || "";
    linha.style.display = nome.includes(termo) ? "" : "none";
  });
}

async function editarColeta(id) {
  try {
    const req = await fetch(`${API_LOCAIS}/admin/${id}`);
    if (!req.ok) throw new Error(`Erro ${req.status}`);
    const c = await req.json();
    editandoColeta = id;

    document.getElementById("tituloModalColeta").textContent = "Editar Ponto de Coleta";
    document.getElementById("localColeta").value = c.nome || "";
    document.getElementById("contatoColeta").value = c.contato || "";
    document.getElementById("emailColeta").value = c.email || "";
    document.getElementById("horarioAberturaColeta").value = c.horarioAbertura || "";
    document.getElementById("horarioFechamentoColeta").value = c.horarioFechamento || "";
    document.getElementById("descricaoColeta").value = c.descricao || "";
    document.getElementById("ruaColeta").value = c.endereco?.rua || "";
    document.getElementById("numeroColeta").value = c.endereco?.numero || "";
    document.getElementById("bairroColeta").value = c.endereco?.bairro || "";
    document.getElementById("cidadeColeta").value = c.endereco?.cidade || "";
    document.getElementById("estadoColeta").value = c.endereco?.estado || "";
    document.getElementById("cepColeta").value = c.endereco?.cep || "";

    // garante tipos atualizados
    await carregarTiposDescarte();
    renderTiposDescarteCheckList();
    // marcar os tipos que já existem
    const checkboxes = document.querySelectorAll("#listaTiposDescarteColeta input[type='checkbox']");
    checkboxes.forEach(cb => {
      const idTipo = Number(cb.getAttribute("data-id"));
      cb.checked = (c.tiposDescarte || []).some(t => t.id === idTipo);
    });

    document.getElementById("modalColeta").style.display = "flex";
  } catch (err) {
    console.error("Erro ao buscar coleta:", err);
    alert("Erro ao buscar ponto de coleta. Veja console.");
  }
}

async function salvarColeta() {
  const nome = document.getElementById("localColeta").value.trim();
  const contato = document.getElementById("contatoColeta").value.trim();
  const email = document.getElementById("emailColeta").value.trim();
  const horarioAbertura = document.getElementById("horarioAberturaColeta").value;
  const horarioFechamento = document.getElementById("horarioFechamentoColeta").value;
  const descricao = document.getElementById("descricaoColeta").value.trim();

  const rua = document.getElementById("ruaColeta").value.trim();
  const numeroStr = document.getElementById("numeroColeta").value.trim();
  const numero = numeroStr === "" ? null : Number(numeroStr);
  const complemento = document.getElementById("complementoColeta")?.value.trim() || "";
  const bairro = document.getElementById("bairroColeta").value.trim();
  const cidade = document.getElementById("cidadeColeta").value.trim();
  const estado = document.getElementById("estadoColeta").value.trim();
  const cep = document.getElementById("cepColeta").value.trim();

  const tipos = [...document.querySelectorAll("#listaTiposDescarteColeta input[type='checkbox']:checked")]
    .map(cb => Number(cb.getAttribute("data-id")))
    .filter(id => !Number.isNaN(id));

  // Validações básicas
  if (!nome || !rua || !bairro || !cidade || !estado || tipos.length === 0) {
    alert("Preencha os campos obrigatórios: nome, rua, bairro, cidade, estado e selecione ao menos um tipo.");
    return;
  }

  if (!email) {
    alert("O e-mail é obrigatório.");
    return;
  }
  const emailRe = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRe.test(email)) {
    alert("Informe um e-mail válido.");
    return;
  }

  if (numero === null || Number.isNaN(numero)) {
    alert("Informe o número do endereço (somente números).");
    return;
  }

  const bodyObj = {
    nome,
    horarioAbertura: horarioAbertura || "08:00",
    horarioFechamento: horarioFechamento || "18:00",
    contato,
    email,
    descricao,
    endereco: {
      rua,
      numero,
      complemento,
      bairro,
      cidade,
      estado,
      cep
    },
    // O backend espera um array de ids: "tiposDescarteIds"
    tiposDescarteIds: tipos
  };

  console.log("Tentando payload salvarColeta:", JSON.stringify(bodyObj, null, 2));

  try {
    let res;
    if (editandoColeta) {
      res = await fetch(`${API_LOCAIS}/admin/${editandoColeta}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(bodyObj)
      });
    } else {
      res = await fetch(`${API_LOCAIS}/admin`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(bodyObj)
      });
    }

    if (!res.ok) {
      const txt = await res.text();
      console.error("Erro salvar coleta:", res.status, txt);
      // tentar extrair JSON de erro
      let parsed = null;
      try { parsed = JSON.parse(txt); } catch(e) { parsed = null; }

      // mensagens amigáveis conforme validação conhecida
      const errors = [];
      if (parsed && parsed.errors) {
        // backend pode retornar { errors: [ ... ] }
        parsed.errors.forEach(e => errors.push(e));
      }

      // detectar email duplicado por texto/constraint
      const lower = (txt || '').toLowerCase();
      if (lower.includes('unique') || lower.includes('duplicate') || lower.includes('já') && lower.includes('email')) {
        errors.push('O email informado já está em uso.');
      }

      // se backend retornou mensagens de validação individuais, use-as
      if (errors.length > 0) {
        showColetaErrors(errors);
        return;
      }

      // fallback: exibir a mensagem do servidor no modal
      showColetaErrors([`Erro ao salvar ponto de coleta: ${res.status}`, txt || '']);
      return;
    }

    clearColetaErrors();
    fecharModalColeta();
    await atualizarColeta();
  } catch (err) {
    console.error("Erro ao salvar coleta:", err);
    showColetaErrors(['Erro ao salvar ponto de coleta. Veja o console para detalhes.']);
  }
}

async function excluirColeta(id) {
  if (!confirm("Deseja excluir este ponto?")) return;
  try {
    const res = await fetch(`${API_LOCAIS}/admin/${id}`, { method: "DELETE" });
    if (!res.ok) {
      const txt = await res.text();
      alert(`Erro ao excluir: ${res.status}\n${txt}`);
      return;
    }
    atualizarColeta();
  } catch (err) {
    console.error(err);
    alert("Erro ao excluir ponto de coleta.");
  }
}
async function salvarProduto() {
  const nome = document.getElementById("nomeProduto").value.trim();
  const descricao = document.getElementById("descricaoProduto").value.trim();
  const reciclavel = document.getElementById("reciclavelProduto").checked;
  const tipoDescarteId = Number(document.getElementById("tipoDescarteProduto").value);

  const locais = [...document.querySelectorAll("#listaLocaisDescarteProduto input[type='checkbox']:checked")]
    .map(cb => Number(cb.getAttribute("data-id")));

  if (!nome) {
    alert("O nome do produto é obrigatório.");
    return;
  }

  if (!tipoDescarteId) {
    alert("Selecione um tipo de descarte.");
    return;
  }

const bodyObj = {
  nome,
  descricao,
  reciclavel,
  idTipoDescarte: tipoDescarteId,
  idLocaisDescarte: locais.length > 0 ? locais : null
};
console.log("JSON enviado para o backend:", JSON.stringify(bodyObj, null, 2));
  try {
    let res;
    if (editandoProduto) {
      res = await fetch(`${API_MATERIAL}/admin/${editandoProduto}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(bodyObj)
      });
    } else {
      res = await fetch(`${API_MATERIAL}/admin/cadastrar`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(bodyObj)
      });
    }

    if (!res.ok) {
      const txt = await res.text();
      console.error("Erro salvar produto:", res.status, txt);
      alert(`Erro ao salvar produto: ${res.status}\n${txt}`);
      return;
    }

    fecharModalProduto();
    atualizarProdutos();

  } catch (err) {
    console.error("Erro salvarProduto:", err);
    alert("Erro inesperado. Veja o console.");
  }
}

/* ========================= MENSAGENS (placeholder) ========================= */
async function atualizarMensagens() {
  const tbody = document.getElementById("listaMensagens");
  tbody.innerHTML = `
    <tr>
      <td colspan="5" style="text-align:center; color:#666;">
        Nenhuma mensagem nova.
      </td>
    </tr>
  `;
}

// Tenta mapear o texto/JSON de erro do servidor para mensagens por campo
function mapServerErrors(text, parsed) {
  const msgs = [];
  const lower = (text || '').toLowerCase();

  // se já veio um JSON estruturado com violações
  if (parsed) {
    // common: { violations: [{ propertyPath, message }] }
    if (Array.isArray(parsed.violations)) {
      parsed.violations.forEach(v => {
        const field = (v.propertyPath || v.field || '').toLowerCase();
        const message = v.message || JSON.stringify(v);
        if (field.includes('email')) msgs.push(`Email: ${message}`);
        else if (field.includes('cep')) msgs.push(`CEP: ${message}`);
        else if (field.includes('numero') || field.includes('number')) msgs.push(`Número: ${message}`);
        else if (field.includes('tipo')) msgs.push(`Tipo de descarte: ${message}`);
        else msgs.push(`${field || 'Campo'}: ${message}`);
      });
      return msgs;
    }

    // common: { errors: [{ field, message }] }
    if (Array.isArray(parsed.errors) && parsed.errors.length > 0 && typeof parsed.errors[0] === 'object') {
      parsed.errors.forEach(e => {
        const field = (e.field || '').toLowerCase();
        const message = e.message || e.defaultMessage || JSON.stringify(e);
        if (field.includes('email')) msgs.push(`Email: ${message}`);
        else if (field.includes('cep')) msgs.push(`CEP: ${message}`);
        else if (field.includes('numero') || field.includes('number')) msgs.push(`Número: ${message}`);
        else if (field.includes('tipo')) msgs.push(`Tipo de descarte: ${message}`);
        else msgs.push(`${field || 'Campo'}: ${message}`);
      });
      return msgs;
    }

    // common: { message: '...' }
    if (parsed.message && typeof parsed.message === 'string') {
      text = parsed.message;
    }
  }

  // heurísticas sobre o texto cru
  if (lower.includes('email')) {
    if (lower.includes('unique') || lower.includes('duplicate') || lower.includes('já') || lower.includes('already')) {
      msgs.push('O email informado já está em uso.');
    } else if (lower.includes('format') || lower.includes('@') || lower.includes('invál') || lower.includes('valid')) {
      msgs.push('Informe um email válido.');
    } else {
      msgs.push('Erro no campo email.');
    }
  }

  if (lower.includes('cep')) {
    if (lower.includes('obrig')) msgs.push('O cep é obrigatório.');
    else msgs.push('CEP inválido — formato esperado 00000-000.');
  }

  if (lower.includes('numero') || lower.includes('number')) {
    if (lower.includes('obrig')) msgs.push('O número é obrigatório.');
    else msgs.push('Número do endereço inválido (somente números).');
  }

  if (lower.includes('tipo') || lower.includes('descarte')) {
    msgs.push('É necessário informar ao menos um tipo de descarte válido.');
  }

  if (lower.includes('contato') || lower.includes('telefone') || lower.includes('whatsapp')) {
    msgs.push('Contato inválido — use o formato (XX) XXXXX-XXXX ou (XX) XXXX-XXXX.');
  }

  if (lower.includes('nome')) {
    if (lower.includes('obrig')) msgs.push('O nome é obrigatório.');
    else msgs.push('Nome inválido (máx 150 caracteres).');
  }

  if (lower.includes('rua')) {
    msgs.push('A rua é obrigatória.');
  }
  if (lower.includes('bairro')) msgs.push('O bairro é obrigatório.');
  if (lower.includes('cidade')) msgs.push('A cidade é obrigatória.');
  if (lower.includes('estado')) msgs.push('O estado é obrigatório.');
  if (lower.includes('descricao')) msgs.push('Descrição inválida (máx 200 caracteres).');

  // se nada mapeado, retornar texto bruto
  if (msgs.length === 0) {
    if (text && text.length > 0) msgs.push(text);
    else msgs.push('Erro desconhecido no servidor.');
  }

  // deduplicar
  return [...new Set(msgs)];
}