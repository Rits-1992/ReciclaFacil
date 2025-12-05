const API_BASE = "http://localhost:8080";
const API_MENSAGENS = `${API_BASE}/mensagens`;

async function enviarMensagemProcedure(event) {
    event.preventDefault();

    // Aqui você adapta o envio para o DTO novo:
    const body = {
        titulo: "Mensagem enviada pelo formulário de contato",
        conteudo: document.getElementById("mensagem").value,
        usuarioId: 1 // TODO → Trocar pelo ID real do usuário logado
    };

    try {
        const resp = await fetch(`${API_MENSAGENS}/enviarProcedure`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });

        if (!resp.ok) {
            const erro = await resp.json();
            alert("Erro ao enviar: " + (erro.message || "Tente novamente"));
            return;
        }

        // Sucesso
        document.getElementById("form-contato").reset();
        document.getElementById("mensagem-sucesso").style.display = "block";

        setTimeout(() => {
            document.getElementById("mensagem-sucesso").style.display = "none";
        }, 4000);

    } catch (error) {
        alert("Erro inesperado ao enviar a mensagem.");
        console.error(error);
    }
}