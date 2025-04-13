import { useState, useEffect } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import reservaService from "../services/reserva.service";
import {
  Box,
  TextField,
  Button,
  FormControl,
  InputLabel,
  Select,
  MenuItem
} from "@mui/material";
import SaveIcon from "@mui/icons-material/Save";

const AddEditReserva = () => {
  const [reserva, setReserva] = useState({
    rutCliente: "",
    fechaReserva: "",
    horaInicio: "",
    cantidadPersonas: 1,
    montoTotal: 0,
    estado: "PENDIENTE",
  });

  const navigate = useNavigate();
  const { id } = useParams();

  useEffect(() => {
    if (id) {
      reservaService.get(id).then((res) => {
        setReserva(res.data);
      });
    }
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setReserva({ ...reserva, [name]: value });
  };

  const saveReserva = (e) => {
    e.preventDefault();
    if (id) {
      reservaService.update(reserva).then(() => {
        navigate("/reservas/list");
      });
    } else {
      reservaService.create(reserva).then(() => {
        navigate("/reservas/list");
      });
    }
  };

  return (
    <Box display="flex" flexDirection="column" alignItems="center" component="form">
      <h3>{id ? "Editar Reserva" : "Nueva Reserva"}</h3>
      
      <FormControl fullWidth sx={{ my: 1 }}>
        <TextField
          name="rutCliente"
          label="RUT Cliente"
          value={reserva.rutCliente}
          onChange={handleChange}
          variant="standard"
        />
      </FormControl>

      <FormControl fullWidth sx={{ my: 1 }}>
        <TextField
          name="fechaReserva"
          label="Fecha de Reserva"
          type="date"
          value={reserva.fechaReserva}
          onChange={handleChange}
          variant="standard"
          InputLabelProps={{ shrink: true }}
        />
      </FormControl>

      <FormControl fullWidth sx={{ my: 1 }}>
        <TextField
          name="horaInicio"
          label="Hora de Inicio"
          type="time"
          value={reserva.horaInicio}
          onChange={handleChange}
          variant="standard"
          InputLabelProps={{ shrink: true }}
        />
      </FormControl>

      <FormControl fullWidth sx={{ my: 1 }}>
        <TextField
          name="cantidadPersonas"
          label="Cantidad de Personas"
          type="number"
          value={reserva.cantidadPersonas}
          onChange={handleChange}
          variant="standard"
        />
      </FormControl>

      <FormControl fullWidth sx={{ my: 1 }}>
        <InputLabel>Monto Total</InputLabel>
        <Select
          name="montoTotal"
          value={reserva.montoTotal}
          onChange={handleChange}
          variant="standard"
          sx={{
            "& .MuiSelect-select": {
              backgroundColor: "background.paper", // Fondo claro
              color: "text.primary" // Texto oscuro
            }
          }}
        >
          <MenuItem value={0}>0</MenuItem>
          <MenuItem value={15000}>15.000</MenuItem>
          <MenuItem value={20000}>20.000</MenuItem>
          <MenuItem value={25000}>25n.000</MenuItem> {/* Corregido este valor */}
        </Select>
      </FormControl>

      <FormControl sx={{ my: 2 }}>
        <Button
          variant="contained"
          onClick={saveReserva}
          startIcon={<SaveIcon />}
        >
          Guardar
        </Button>
      </FormControl>

      <Link to="/reservas/list">Volver al listado</Link>
    </Box>
  );
};

export default AddEditReserva;