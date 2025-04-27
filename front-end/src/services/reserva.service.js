import axios from 'axios';

const API_URL = 'http://kart-app.brazilsouth.cloudapp.azure.com:9090/api/v1/reservas';

const crearReserva = (reserva) => {
  return axios.post(`${API_URL}/crear`, reserva);
};

const obtenerReservas = () => {
  return axios.get(`${API_URL}/listar`);  // Solo si tienes un endpoint para listar las reservas
};

const obtenerReservaPorId = (id) => {
  return axios.get(`${API_URL}/buscar/${id}`);  // Solo si tienes un endpoint para obtener por ID
};

const actualizarReserva = (reserva) => {
  return axios.put(`${API_URL}/actualizar`, reserva); // Endpoint para actualizar
};

const eliminarReserva = (id) => {
  return axios.delete(`${API_URL}/eliminar/${id}`); // Endpoint para eliminar
};

const confirmarReserva = (id) => {
  return axios.put(`${API_URL}/confirmar/${id}`); // Endpoint para confirmar reserva
};

export default {
  crearReserva,
  obtenerReservas,
  obtenerReservaPorId,
  actualizarReserva,
  eliminarReserva,
  confirmarReserva
};
