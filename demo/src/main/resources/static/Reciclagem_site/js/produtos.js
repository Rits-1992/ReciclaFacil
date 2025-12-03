const API_BASE = "http://localhost:8080";
const API_MATERIAIS = `${API_BASE}/materiais/public/buscar`;

const searchInput = document.getElementById("search");
const suggestionsList = document.getElementById("suggestions");
const resultadoDiv = document.getElementById("resultado");

// ============================
// BUSCA AUTOMÁTICA
// ============================
searchInput.addEventListener("input", async () => {
  const texto = searchInput.value.trim();

  if (texto.length < 2) {
    suggestionsList.innerHTML = "";
    return;
  }

  try {
    const res = await fetch(`${API_MATERIAIS}?nome=${texto}`);
    const lista = await res.json();

    suggestionsList.innerHTML = "";

    lista.forEach(item => {
      const li = document.createElement("li");
      li.textContent = item.nome;
      li.addEventListener("click", () => selecionarProduto(item));
      suggestionsList.appendChild(li);
    });

  } catch (err) {
    console.error("Erro na busca:", err);
  }
});

// ============================
// EXIBIR DETALHES DO PRODUTO
// ============================
function selecionarProduto(produto) {
  suggestionsList.innerHTML = "";
  searchInput.value = produto.nome;

  resultadoDiv.innerHTML = `
    <h3>${produto.nome}</h3>
    <p>${produto.descricao}</p>
    <p><strong>Reciclável:</strong> ${produto.reciclavel ? "Sim" : "Não"}</p>
    <p><strong>Tipo de descarte:</strong> ${produto.tipoDescarte}</p>
  `;
}