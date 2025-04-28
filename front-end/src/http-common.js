import axios from "axios";


// Obtiene las variables de entorno en tiempo de ejecución (definidas al compilar con Vite)
const server = import.meta.env.VITE_BACKEND_SERVER || "kart-app.brazilsouth.cloudapp.azure.com";
const port = import.meta.env.VITE_BACKEND_PORT || "9090";
const baseURL = `http://${server}:${port}/api/v1`;

export default axios.create({
  baseURL,
  headers: {
    "Content-type": "application/json",
  },
});
