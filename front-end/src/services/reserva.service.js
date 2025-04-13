import httpClient from "../http-common.js";
 // Este ya tiene baseURL = http://localhost:9090/api/v1

const getAll = () => {
  return httpClient.get("/reservas/");
};

const get = (id) => {
  return httpClient.get(`/reservas/${id}`);
};

const create = (reserva) => {
  return httpClient.post("/reservas/", reserva);
};

const update = (reserva) => {
  return httpClient.put("/reservas/", reserva);
};

const eliminar = (id) => {
  return httpClient.delete(`/reservas/${id}`);
};

const confirmar = (id) => {
  return httpClient.put(`/reservas/confirmar/${id}`);
};

export default {
  getAll,
  get,
  create,
  update,
  eliminar,
  confirmar,
};
