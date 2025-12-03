const input = document.getElementById('search');
const sug = document.getElementById('suggestions');
const resultado = document.getElementById('resultado');

// Dispara busca conforme usuário digita
input.addEventListener('input', async () => {
  const q = input.value.trim();

  sug.innerHTML = '';
  resultado.innerHTML = '<p style="text-align:center; color:#666;">Pesquise uma cidade acima para ver os pontos de coleta.</p>';

  if (q.length < 2) return;

  try {
    const response = await fetch(`http://localhost:8080/localDescarte/public/sugestoes?cidade=${q}`);

    const cidades = await response.json();

    cidades.forEach(nomeCidade => {
      const li = document.createElement('li');
      li.textContent = nomeCidade;
      li.addEventListener('click', () => buscarLocais(nomeCidade));
      sug.appendChild(li);
    });
  } catch (error) {
    console.error("Erro ao buscar sugestões:", error);
  }
});

// Buscar locais de descarte por cidade
async function buscarLocais(cidadeNome) {
  sug.innerHTML = '';
  input.value = cidadeNome;

  try {
    const response = await fetch(`http://localhost:8080/localDescarte/public/buscar?cidade=${cidadeNome}`);
    const locais = await response.json();

    mostrarPontos(cidadeNome, locais);
  } catch (error) {
    resultado.innerHTML = `<p style="color:red;">Erro ao carregar locais.</p>`;
  }
}

// Renderiza os pontos no HTML
function mostrarPontos(cidadeNome, locais) {
  if (!locais || locais.length === 0) {
    resultado.innerHTML = `
      <h2>${cidadeNome}</h2>
      <p>Nenhum ponto de coleta encontrado 😕</p>`;
    return;
  }

  const html = `
    <h2>${cidadeNome}</h2>
    <h3>Pontos de Coleta Disponíveis</h3>
    <ul>
      ${locais.map(l => `
        <li>
          <strong>${l.nome}</strong><br>
          ${l.endereco.rua}, ${l.endereco.numero} - ${l.endereco.bairro}<br>
          Tel: ${l.contato}<br>
          Horário: ${l.horarioAbertura} às ${l.horarioFechamento}
        </li>
      `).join('')}
    </ul>
  `;

  resultado.innerHTML = html;
}