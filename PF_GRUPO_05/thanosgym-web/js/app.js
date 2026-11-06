
const API_URL = "http://localhost:8080/api/v1/auth/login";

// Identificamos los elementos que existen en cada página.
const botonAcceso = document.getElementById("btnAcceso");
const botonCerrarSesion = document.getElementById("btnCerrarSesion");

// =====================================================
// FUNCIONES COMPARTIDAS
// =====================================================

function obtenerToken() {
  return sessionStorage.getItem("thanosgymToken");
}

function limpiarSesion() {
  sessionStorage.removeItem("thanosgymToken");
  sessionStorage.removeItem("thanosgymUsuario");
  sessionStorage.removeItem("thanosgymRol");
}

// =====================================================
// PÁGINA 1: INDEX.HTML - INICIO DE SESIÓN
// =====================================================

function configurarInicioSesion() {
  // Si ya existe una sesión, entramos directamente al panel.
  if (obtenerToken()) {
    window.location.replace("panel.html");
    return;
  }

  if (!botonAcceso) return;

  // Creamos el formulario de inicio de sesión.
  const modal = document.createElement("dialog");
  modal.id = "modalAcceso";

  modal.innerHTML = `
    <form id="formAcceso">
      <h2>Iniciar sesión</h2>
      <p>Ingresa tus credenciales de TNTGym.</p>

      <label for="usuario">Usuario</label>
      <input
        type="text"
        id="usuario"
        name="usuario"
        placeholder="Tu usuario"
        autocomplete="username"
        required
      />

      <label for="password">Contraseña</label>
      <input
        type="password"
        id="password"
        name="password"
        placeholder="Tu contraseña"
        autocomplete="current-password"
        required
      />

      <p id="mensajeAcceso" role="status" aria-live="polite"></p>

      <div class="acciones-acceso">
        <button type="submit" id="btnEnviarAcceso">
          Entrar
        </button>
        <button type="button" id="btnCancelarAcceso">
          Cancelar
        </button>
      </div>
    </form>
  `;

  document.body.appendChild(modal);

// Estilos del formulario.
const estilos = document.createElement("style");

estilos.textContent = `
    #modalAcceso {
        width: min(90%, 410px);
        max-width: 410px;
        box-sizing: border-box;
        border: 1px solid rgba(184, 243, 74, 0.45);
        border-radius: 18px;
        padding: 36px;
        background: linear-gradient(
            145deg,
            #1c211c,
            #111412 75%
        );
        color: #f5f5f5;
        box-shadow: 0 20px 65px rgba(0, 0, 0, 0.65);
    }

    #modalAcceso::backdrop {
        background: rgba(0, 0, 0, 0.82);
        backdrop-filter: blur(6px);
    }

    #formAcceso {
        display: flex;
        flex-direction: column;
        gap: 12px;
    }

    #formAcceso h2 {
        margin: 0 0 4px;
        color: #b8f34a;
        font-size: 28px;
        text-align: center;
    }

    #formAcceso > p:not(#mensajeAcceso) {
        margin: 0 0 18px;
        color: #b7b8c0;
        text-align: center;
        font-size: 14px;
    }

    #formAcceso label {
        margin-top: 7px;
        color: #e8e8e8;
        font-size: 14px;
        font-weight: 600;
    }

    #formAcceso input {
        box-sizing: border-box;
        width: 100%;
        padding: 14px;
        border: 1px solid #424a40;
        border-radius: 8px;
        outline: none;
        background: #202520;
        color: white;
        font: inherit;
        transition: border-color 0.25s, box-shadow 0.25s;
    }

    #formAcceso input::placeholder {
        color: #858b83;
    }

    #formAcceso input:focus {
        border-color: #b8f34a;
        box-shadow: 0 0 0 3px rgba(184, 243, 74, 0.12);
    }

    .acciones-acceso {
        display: flex;
        gap: 12px;
        margin-top: 18px;
    }

    .acciones-acceso button {
        flex: 1;
        padding: 13px 14px;
        border: 0;
        border-radius: 8px;
        font-size: 14px;
        font-weight: bold;
        cursor: pointer;
        transition: transform 0.2s, background 0.2s;
    }

    #btnEnviarAcceso {
        background: #b8f34a;
        color: #151710;
    }

    #btnEnviarAcceso:hover {
        background: #c9ff60;
        transform: translateY(-2px);
    }

    #btnCancelarAcceso {
        border: 1px solid #50564d;
        background: #292e29;
        color: #eeeeee;
    }

    #btnCancelarAcceso:hover {
        background: #383e37;
    }

    .acciones-acceso button:disabled {
        opacity: 0.6;
        cursor: not-allowed;
        transform: none;
    }

    #mensajeAcceso {
        min-height: 18px;
        margin: 4px 0;
        color: #b8f34a;
        font-size: 13px;
        overflow-wrap: anywhere;
    }

    @media (max-width: 480px) {
        #modalAcceso {
            padding: 26px 22px;
        }
    }
`;

document.head.appendChild(estilos);

  const formulario = document.getElementById("formAcceso");
  const mensaje = document.getElementById("mensajeAcceso");
  const botonEnviar = document.getElementById("btnEnviarAcceso");
  const botonCancelar = document.getElementById("btnCancelarAcceso");

  // Abrir el formulario.
  botonAcceso.addEventListener("click", () => {
    formulario.reset();
    mensaje.textContent = "";
    botonEnviar.disabled = false;
    botonEnviar.textContent = "Entrar";

    modal.showModal();
  });

  // Cerrar el formulario sin iniciar sesión.
  botonCancelar.addEventListener("click", () => {
    modal.close();
  });

  // Enviar las credenciales al servicio de autenticación.
  formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault();

    const usuario = document.getElementById("usuario").value.trim();
    const password = document.getElementById("password").value;

    mensaje.textContent = "Verificando credenciales...";
    botonEnviar.disabled = true;
    botonEnviar.textContent = "Ingresando...";

    try {
      const respuesta = await fetch(API_URL, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          usuario,
          password
        })
      });

      const datos = await respuesta.json().catch(() => ({}));

      if (!respuesta.ok) {
        throw new Error(
          datos.message ||
          datos.mensaje ||
          `El servidor respondió con el código ${respuesta.status}.`
        );
      }

      if (!datos.token) {
        throw new Error(
          "El servicio respondió, pero no encontramos el token de acceso."
        );
      }

      // Guardar los datos de la sesión.
      sessionStorage.setItem("thanosgymToken", datos.token);
      sessionStorage.setItem(
        "thanosgymUsuario",
        datos.nombre || usuario
      );
      sessionStorage.setItem("thanosgymRol", datos.rol || "");

      // Entrar al panel después de iniciar sesión correctamente.
      window.location.href = "panel.html";

    } catch (error) {
      console.error("Error de inicio de sesión:", error);

      if (error instanceof TypeError) {
        mensaje.textContent =
          "No se pudo conectar con el servicio. Verifica que los servicios estén ejecutándose y revisa CORS en el API Gateway.";
      } else {
        mensaje.textContent = error.message;
      }

    } finally {
      botonEnviar.disabled = false;
      botonEnviar.textContent = "Entrar";
    }
  });
}
