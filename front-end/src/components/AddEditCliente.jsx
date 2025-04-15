import React, { useState } from 'react';
import clienteService from '../services/cliente.service';
import { useNavigate } from 'react-router-dom';

const AddCliente = () => {
  const navigate = useNavigate();

  const [cliente, setCliente] = useState({
    rut: '',
    nombre: '',
    correo: '',
    fechaNacimiento: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCliente({ ...cliente, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const clienteConReservas = {
        ...cliente,
        cantidadReservas: 0,
      };
      await clienteService.crearCliente(clienteConReservas);
      alert('Cliente creado exitosamente');
      navigate('/clientes'); // ajusta la ruta si es necesario
    } catch (error) {
      console.error('Error al crear cliente:', error);
      alert('Error al crear cliente');
    }
  };

  return (
    <div className="container">
      <h2>Crear Cliente</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>RUT:</label>
          <input
            type="text"
            name="rut"
            value={cliente.rut}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Nombre:</label>
          <input
            type="text"
            name="nombre"
            value={cliente.nombre}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Correo:</label>
          <input
            type="email"
            name="correo"
            value={cliente.correo}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Fecha de Nacimiento:</label>
          <input
            type="date"
            name="fechaNacimiento"
            value={cliente.fechaNacimiento}
            onChange={handleChange}
            required
          />
        </div>
        <button type="submit">Guardar Cliente</button>
      </form>
    </div>
  );
};

export default AddCliente;
