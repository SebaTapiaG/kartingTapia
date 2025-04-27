import axios from 'axios';

const API_URL = 'http://kart-app.brazilsouth.cloudapp.azure.com:9090/api/v1/clientes';

const crearCliente = (cliente) => {
  return axios.post(`${API_URL}/crear`, cliente);
};

const obtenerClientes = () => {
  return axios.get(`${API_URL}/listar`); // solo si tienes este endpoint
};

const obtenerClientePorRut = (rut) => {
  return axios.get(`${API_URL}/buscar/${rut}`); // si tienes algo así
};

export default {
  crearCliente,
  obtenerClientes,
  obtenerClientePorRut
};
