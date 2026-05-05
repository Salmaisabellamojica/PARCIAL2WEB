const token = document.querySelector('meta[name="_csrf"]').content;
const header = document.querySelector('meta[name="_csrf_header"]').content;
const mensaje = document.getElementById("mensaje");

function mostrar(texto, ok = true) {
    mensaje.textContent = texto;
    mensaje.className = ok ? "mb-4 text-sm font-semibold text-green-700" : "mb-4 text-sm font-semibold text-red-700";
}

async function enviar(url, metodo, datos) {
    const respuesta = await fetch(url, {
        method: metodo,
        headers: {"Content-Type": "application/json", [header]: token},
        body: JSON.stringify(datos)
    });
    if (!respuesta.ok) {
        const error = await respuesta.json().catch(() => ({error: "No fue posible completar la accion"}));
        throw new Error(error.error || "No fue posible completar la accion");
    }
    return respuesta;
}

document.getElementById("formConsulta").addEventListener("submit", async (event) => {
    event.preventDefault();
    const id = document.getElementById("consultaId").value;
    const datos = {
        nombrePaciente: document.getElementById("nombrePaciente").value,
        motivo: document.getElementById("motivo").value,
        numeroConsultorio: document.getElementById("numeroConsultorio").value,
        fecha: document.getElementById("fecha").value,
        horaInicio: document.getElementById("horaInicio").value,
        horaFin: document.getElementById("horaFin").value,
        medicoId: Number(document.getElementById("medicoId").value)
    };
    try {
        await enviar(id ? `/api/consultas/${id}` : "/api/consultas", id ? "PUT" : "POST", datos);
        mostrar("Consulta guardada correctamente.");
        location.reload();
    } catch (error) {
        mostrar(error.message, false);
    }
});

document.getElementById("formMedico").addEventListener("submit", async (event) => {
    event.preventDefault();
    const id = document.getElementById("medicoEditId").value;
    const datos = {
        nombre: document.getElementById("medicoNombre").value,
        especialidad: document.getElementById("especialidad").value,
        documento: document.getElementById("documento").value,
        username: document.getElementById("username").value,
        password: document.getElementById("password").value
    };
    try {
        await enviar(id ? `/api/medicos/${id}` : "/api/medicos", id ? "PUT" : "POST", datos);
        mostrar("Medico guardado correctamente.");
        location.reload();
    } catch (error) {
        mostrar(error.message, false);
    }
});

document.querySelectorAll(".editarConsulta").forEach((boton) => {
    boton.addEventListener("click", () => {
        const fila = boton.closest("tr").dataset;
        document.getElementById("consultaId").value = fila.id;
        document.getElementById("nombrePaciente").value = fila.paciente;
        document.getElementById("motivo").value = fila.motivo;
        document.getElementById("numeroConsultorio").value = fila.consultorio;
        document.getElementById("fecha").value = fila.fecha;
        document.getElementById("horaInicio").value = fila.inicio.substring(0, 5);
        document.getElementById("horaFin").value = fila.fin.substring(0, 5);
        document.getElementById("medicoId").value = fila.medico;
        window.scrollTo({top: 0, behavior: "smooth"});
    });
});

document.querySelectorAll(".eliminarConsulta").forEach((boton) => {
    boton.addEventListener("click", async () => {
        const id = boton.closest("tr").dataset.id;
        try {
            const respuesta = await fetch(`/api/consultas/${id}`, {method: "DELETE", headers: {[header]: token}});
            if (!respuesta.ok) {
                throw new Error("No fue posible eliminar la consulta");
            }
            location.reload();
        } catch (error) {
            mostrar(error.message, false);
        }
    });
});

document.getElementById("limpiarConsulta").addEventListener("click", () => {
    document.getElementById("formConsulta").reset();
    document.getElementById("consultaId").value = "";
});

document.querySelectorAll(".editarMedico").forEach((boton) => {
    boton.addEventListener("click", () => {
        const fila = boton.closest("tr").dataset;
        document.getElementById("medicoEditId").value = fila.id;
        document.getElementById("medicoNombre").value = fila.nombre;
        document.getElementById("especialidad").value = fila.especialidad;
        document.getElementById("documento").value = fila.documento;
        document.getElementById("username").value = fila.username;
        document.getElementById("password").value = "";
        window.scrollTo({top: 0, behavior: "smooth"});
    });
});

document.querySelectorAll(".eliminarMedico").forEach((boton) => {
    boton.addEventListener("click", async () => {
        const id = boton.closest("tr").dataset.id;
        try {
            const respuesta = await fetch(`/api/medicos/${id}`, {method: "DELETE", headers: {[header]: token}});
            if (!respuesta.ok) {
                throw new Error("No fue posible eliminar el medico. Revise que no tenga consultas asignadas.");
            }
            location.reload();
        } catch (error) {
            mostrar(error.message, false);
        }
    });
});

document.getElementById("limpiarMedico").addEventListener("click", () => {
    document.getElementById("formMedico").reset();
    document.getElementById("medicoEditId").value = "";
});
