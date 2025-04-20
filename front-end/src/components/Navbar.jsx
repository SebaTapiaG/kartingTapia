import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import { Link } from "react-router-dom";

export default function Navbar() {
  return (
    <Box sx={{ flexGrow: 1, marginBottom: 2 }}>
      <AppBar position="static">
        <Toolbar>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
          >
            <MenuIcon />
          </IconButton>

          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Karting
          </Typography>

          <Button color="inherit" component={Link} to="/">
            Home
          </Button>
          <Button color="inherit" component={Link} to="/clientes/add">
            Agregar Cliente
          </Button>
          <Button color="inherit" component={Link} to="/reservas/add">
            Agregar Reserva
          </Button>
          <Button color="inherit" component={Link} to="/reservas/list">
            Listar Reservas
          </Button>
          <Button color="inherit" component={Link} to="/rack">
            Rack Semanal
          </Button>

          <Button color="inherit" component={Link} to="/reporte">
            Reportes
          </Button>
        
        </Toolbar>
      </AppBar>
    </Box>
  );
}
