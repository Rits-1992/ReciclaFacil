document.querySelector("#formCadastro").addEventListener("submit", async (e) => {
  e.preventDefault();

  const dados = {
    nome: document.querySelector("#nome").value,
    email: document.querySelector("#email").value,
    senha: document.querySelector("#senha").value
  };

  try {
    const response = await fetch("http://localhost:8080/usuarios/cadastrar", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(dados)
    });

    const resultado = await response.json();

    if (response.ok) {
      alert("Cadastro realizado com sucesso!");
      window.location.href = "login.html";
    } else {
      alert("Erro: " + (resultado.mensagem || JSON.stringify(resultado)));
    }

  } catch (error) {
    alert("Erro ao conectar com o servidor.");
    console.error(error);
  }
});