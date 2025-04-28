import httpClient from "../http-common.js"; // 

const getAll = () => {
  return httpClient.get("/reservas/");
};

const get = (id) => {
  return httpClient.get(`/reservas/${id}`);
};

const createCustom = (reservaData) => {
  let fechaEnviar;

  if (reservaData.fechaReserva instanceof Date) {
    fechaEnviar = reservaData.fechaReserva.toISOString();
  } else if (typeof reservaData.fechaReserva === 'string') {
    fechaEnviar = new Date(reservaData.fechaReserva).toISOString();
  } else {
    fechaEnviar = new Date().toISOString();
  }

  const data = {
    rut: reservaData.rutCliente,
    fecha: fechaEnviar,
    cantidadPersonas: parseInt(reservaData.cantidadPersonas),
    montoTotal: reservaData.montoTotal
  };

  console.log('Datos a enviar:', data);
  return httpClient.post("/reservas/crear", data);
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

const obtenerReserva = (dia, hora) => {
  return reservas.find(r => {
    const fecha = new Date(r.fecha);
    const diaReserva = fecha.getDay();
    const horaReserva = fecha.getHours().toString().padStart(2, '0') + ':00';

    const indexDia = dia === 'Domingo' ? 0 : diasSemana.indexOf(dia) + 1;

    return diaReserva === indexDia && horaReserva === hora;
  });
};

export default {
  getAll,
  get,
  create,
  createCustom,
  update,
  eliminar,
  confirmar,
};
