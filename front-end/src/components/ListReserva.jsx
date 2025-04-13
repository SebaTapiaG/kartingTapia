import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import ReservaService from "../services/reserva.service";

import { Button, Table, TableBody, TableCell, TableHead, TableRow } from "@mui/material";

const ListaReservas = () => {
  const [reservas, setReservas] = useState([]);

  useEffect(() => {
    cargarReservas();
  }, []);

  const cargarReservas = () => {
    ReservaService.getAll()
      .then((res) => setReservas(res.data))
      .catch((err) => console.error(err));
  };

  const confirmarReserva = (id) => {
    ReservaService.confirmar(id).then(() => cargarReservas());
  };

  const eliminarReserva = (id) => {
    ReservaService.eliminar(id).then(() => cargarReservas());
  };

  return (
    <div>
      <h2>Listado de Reservas</h2>
      <Link to="/reservas/nueva">
        <Button variant="contained" color="primary">Nueva Reserva</Button>
      </Link>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>ID</TableCell>
            <TableCell>Cliente</TableCell>
            <TableCell>Fecha</TableCell>
            <TableCell>Precio</TableCell>
            <TableCell>Estado</TableCell>
            <TableCell>Acciones</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {reservas.map((r) => (
            <TableRow key={r.idReserva}>
              <TableCell>{r.idReserva}</TableCell>
              <TableCell>{r.rutCliente}</TableCell>
              <TableCell>{new Date(r.fechaReserva).toLocaleDateString()}</TableCell>
              <TableCell>{r.montoTotal}</TableCell>
              <TableCell>{r.estado}</TableCell>
              <TableCell>
                <Link to={`/reservas/editar/${r.idReserva}`}>
                  <Button variant="outlined">Editar</Button>
                </Link>
                <Button color="success" onClick={() => confirmarReserva(r.idReserva)}>
                  Confirmar
                </Button>
                <Button color="error" onClick={() => eliminarReserva(r.idReserva)}>
                  Eliminar
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
};

export default ListaReservas;
