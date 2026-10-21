const mensaje = document.querySelector("#mensaje");
const filas = document.querySelector("#filas");
const reporte = document.querySelector("#reporte");

const tipos = {
  Constancia: "Constancia",
  Equivalencia: "Equivalencia",
  Revision: "Revisión"
};

async function info() {
  const datos = await pedir("/api/info");
  document.querySelector("#version").textContent = datos.version === "inicial"
    ? "Versión inicial"
    : "Versión refactorizada";
  document.title = datos.titulo;
}

function datosDe(formulario) {
  return Object.fromEntries(new FormData(formulario).entries());
}

async function pedir(ruta, cuerpo) {
  const respuesta = await fetch(ruta, cuerpo
    ? { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(cuerpo) }
    : undefined);
  return respuesta.json();
}

function pastilla(texto, clase) {
  const marca = document.createElement("span");
  marca.className = "pastilla " + clase;
  marca.textContent = texto;
  return marca;
}

function claseEstado(estado) {
  if (estado === "En curso") return "encurso";
  if (estado === "Resuelta") return "resuelta";
  if (estado === "Rechazada") return "rechazada";
  return "nueva";
}

function pintar(datos) {
  filas.innerHTML = "";
  if (!datos.length) {
    const fila = document.createElement("tr");
    fila.innerHTML = "<td colspan='9' class='vacio'>No hay solicitudes para mostrar.</td>";
    filas.append(fila);
    return;
  }
  for (const item of datos) {
    const fila = document.createElement("tr");
    const valores = [
      item.id,
      item.nombre,
      item.legajo,
      tipos[item.tipo] || item.tipo,
      item.descripcion
    ];
    for (const valor of valores) {
      const celda = document.createElement("td");
      celda.textContent = valor || "";
      fila.append(celda);
    }
    const prioridad = document.createElement("td");
    prioridad.append(pastilla(item.prioridad, (item.prioridad || "").toLowerCase()));
    fila.append(prioridad);
    const estado = document.createElement("td");
    estado.append(pastilla(item.estado, claseEstado(item.estado)));
    fila.append(estado);
    const responsable = document.createElement("td");
    responsable.textContent = item.responsable || "-";
    fila.append(responsable);
    const accion = document.createElement("td");
    const boton = document.createElement("button");
    boton.type = "button";
    boton.className = "ver";
    boton.textContent = "Usar este ID";
    boton.addEventListener("click", () => usarId(item.id));
    accion.append(boton);
    fila.append(accion);
    filas.append(fila);
  }
}

function usarId(id) {
  document.querySelector("#form-estado [name=id]").value = id;
  document.querySelector("#form-responsable [name=id]").value = id;
  document.querySelector("#estado").scrollIntoView({ behavior: "smooth", block: "start" });
}

function avisar(resultado) {
  mensaje.textContent = resultado.mensaje || (resultado.ok ? "Listo." : "No se pudo completar.");
  mensaje.className = resultado.ok ? "ok" : "error";
}

async function listaCompleta() {
  const resultado = await pedir("/api/buscar", { tipo: "", prioridad: "", estado: "" });
  pintar(resultado.datos);
  if (!mensaje.className) {
    const cantidad = resultado.datos.length;
    mensaje.textContent = cantidad === 0
      ? "Todavía no hay resultados. Empezá por Registrar nueva solicitud."
      : cantidad === 1
        ? "Hay 1 solicitud cargada."
        : "Hay " + cantidad + " solicitudes cargadas.";
  }
}

document.querySelector("#form-registrar").addEventListener("submit", async (evento) => {
  evento.preventDefault();
  const resultado = await pedir("/api/registrar", datosDe(evento.target));
  avisar(resultado);
  reporte.classList.add("oculto");
  await listaCompleta();
});

document.querySelector("#form-estado").addEventListener("submit", async (evento) => {
  evento.preventDefault();
  const resultado = await pedir("/api/estado", datosDe(evento.target));
  avisar(resultado);
  reporte.classList.add("oculto");
  await listaCompleta();
});

document.querySelector("#form-responsable").addEventListener("submit", async (evento) => {
  evento.preventDefault();
  const datos = datosDe(evento.target);
  const persona = (datos.persona || "").trim();
  const responsable = persona ? datos.area + " — " + persona : datos.area;
  const resultado = await pedir("/api/responsable", { id: datos.id, responsable });
  avisar(resultado);
  reporte.classList.add("oculto");
  await listaCompleta();
});

document.querySelector("#form-buscar").addEventListener("submit", async (evento) => {
  evento.preventDefault();
  const resultado = await pedir("/api/buscar", datosDe(evento.target));
  avisar(resultado);
  if (!resultado.mensaje) {
    mensaje.textContent = resultado.datos.length
      ? "Se encontraron " + resultado.datos.length + " solicitudes."
      : "Ninguna solicitud coincide con ese filtro.";
    mensaje.className = "ok";
  }
  reporte.classList.add("oculto");
  pintar(resultado.datos);
  document.querySelector("#listado").scrollIntoView({ behavior: "smooth", block: "start" });
});

document.querySelector("#ver-reporte").addEventListener("click", async () => {
  const resultado = await pedir("/api/reporte", {});
  avisar(resultado);
  mensaje.textContent = resultado.datos.length ? "Reporte generado." : "No hay solicitudes cargadas.";
  mensaje.className = "ok";
  pintar(resultado.datos);
  reporte.textContent = resultado.mensaje || "";
  reporte.classList.toggle("oculto", !resultado.mensaje);
  document.querySelector("#listado").scrollIntoView({ behavior: "smooth", block: "start" });
});

document.querySelectorAll(".menu a").forEach((enlace) => {
  enlace.addEventListener("click", () => {
    document.querySelectorAll(".menu a").forEach((item) => item.classList.remove("activo"));
    enlace.classList.add("activo");
  });
});

info();
listaCompleta();
