document.querySelectorAll('a[href^="#"]').forEach(link => {
  link.addEventListener("click", function (e) {
    e.preventDefault();

    if (this.getAttribute("href") === "#") {
      // Scroll suave para o topo da página
      window.scrollTo({ top: 0, behavior: "smooth" });
    } else {
      const destino = document.querySelector(this.getAttribute("href"));
      if (destino) {
        destino.scrollIntoView({ behavior: "smooth" });
      }
    }
  });
});