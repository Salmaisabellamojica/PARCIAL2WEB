const token = document.querySelector('meta[name="_csrf"]').content;
const header = document.querySelector('meta[name="_csrf_header"]').content;
const mensaje = document.getElementById("mensaje");

function mostrar(texto, ok = true) {
    mensaje.textContent = texto;
    mensaje.className = ok ? "mb-4 text-sm font-semibold text-green-700" : "mb-4 text-sm font-semibold text-red-700";
}

document.querySelectorAll(".actualizarHorario").forEach((boton) => {
    boton.addEventListener("click", async () => {
        const fila = boton.closest("tr");
        const datos = {
            fecha: fila.querySelector(".fecha").value,
            horaInicio: fila.querySelector(".inicio").value,
            horaFin: fila.querySelector(".fin").value
        };
        try {
            const respuesta = await fetch(`/api/consultas/${fila.dataset.id}/horario`, {
                method: "PATCH",
                headers: {"Content-Type": "application/json", [header]: token},
                body: JSON.stringify(datos)
            });
            if (!respuesta.ok) {
                const error = await respuesta.json().catch(() => ({error: "Horario ocupado o invalido"}));
                throw new Error(error.error);
            }
            mostrar("Horario actualizado correctamente.");
        } catch (error) {
            mostrar(error.message, false);
        }
    });
});

