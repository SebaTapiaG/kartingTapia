import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import ReservaService from "../services/reserva.service";

import { Box, Button, FormControl, TextField } from "@mui/material";

const FormularioReserva = () => {
  const [reserva, setReserva] = useState({
    rutCliente: "",
    idKart: "",
    fechaReserva: "",
    horaInicio: "",
    cantidadPersonas: 0,
    montoTotal: 0,
    estado: "pendiente",
  });

  const { id } = useParams();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setReserva({ ...reserva, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (id) {
      ReservaService.update(reserva).then(() => navigate("/reservas"));
    } else {
      ReservaService.create(reserva).then(() => navigate("/reservas"));
    }
  };

  useEffect(() => {
    if (id) {
      ReservaService.get(id).then((res) => setReserva(res.data));
    }
  }, [id]);

  return (
    <Box component="form" onSubmit={handleSubmit}>
      <h3>{id ? "Editar Reserva" : "Nueva Reserva"}</h3>
      <FormControl fullWidth margin="normal">
        <TextField
          label="RUT Cliente"
          name="rutCliente"
          value={reserva.rutCliente}
          onChange={handleChange}
          required
        />
      </FormControl>

      <FormControl fullWidth margin="normal">
        <TextField
          type="date"
          name="fechaReserva"
          value={reserva.fechaReserva}
          onChange={handleChange}
          InputLabelProps={{ shrink: true }}
          label="Fecha de Reserva"
        />
      </FormControl>

      <FormControl fullWidth margin="normal">
        <TextField
          label="Hora Inicio"
          type="time"
          name="horaInicio"
          value={reserva.horaInicio}
          onChange={handleChange}
          InputLabelProps={{ shrink: true }}
        />
      </FormControl>

      <FormControl fullWidth margin="normal">
        <TextField
          label="Cantidad de Personas"
          type="number"
          name="cantidadPersonas"
          value={reserva.cantidadPersonas}
          onChange={handleChange}
        />
      </FormControl>

      <FormControl fullWidth sx={{ my: 1 }}>
        <TextField
          name="montoTotal"
          label="Monto Total"
          type="number"
          value={reserva.montoTotal}
          onChange={handleChange}
          variant="standard"
        />
      </FormControl>

      <Button type="submit" variant="contained" color="primary">
        Guardar
      </Button>
    </Box>
  );
};

export default FormularioReserva;
