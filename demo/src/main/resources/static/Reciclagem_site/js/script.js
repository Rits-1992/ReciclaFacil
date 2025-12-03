document.querySelector("#formLogin").addEventListener("submit", async (e) => {
  e.preventDefault();

  const email = document.querySelector("#email").value;
  const senha = document.querySelector("#senha").value;

  // Teste local (sem backend)
  if (email === "usu" && senha === "1234") {
    alert("Login bem-sucedido!");
    window.location.href = "home.html";
  } else {
    alert("Email ou senha incorretos!");
  }
});