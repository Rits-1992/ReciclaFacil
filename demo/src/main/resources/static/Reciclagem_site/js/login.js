document.querySelector("#formLogin").addEventListener("submit", async (e) => {
  e.preventDefault();

  const dados = {
    email: document.querySelector("#email").value.trim(),
    senha: document.querySelector("#senha").value.trim()
  };

  try {
    const response = await fetch("http://localhost:8080/usuarios/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(dados)
    });

    const usuario = await response.json();

    if (!response.ok) {
      alert(usuario.message || "Email ou senha incorretos!");
      return;
    }

    // ⭐ SALVAR TOKEN AQUI ⭐
    if (usuario.token) {
      localStorage.setItem("token", usuario.token);
    }

    if (usuario.tipoUsuario === "ADMIN") {
      window.location.href = "admin.html";
    } else {
      window.location.href = "home.html";
    }

  } catch (error) {
    alert("Erro ao conectar ao servidor! (CORS / Backend offline)");
    console.error(error);
  }
});