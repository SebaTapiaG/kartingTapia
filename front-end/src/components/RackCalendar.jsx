import React, { useEffect, useState } from 'react';
import axios from 'axios';

const diasSemana = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo'];

// Generar bloques de media hora desde 10:00 hasta 20:30
const bloquesHorarios = Array.from({ length: 22 }, (_, i) => {
  const hora = 10 + Math.floor(i / 2);
  const minutos = i % 2 === 0 ? '00' : '30';
  return `${hora.toString().padStart(2, '0')}:${minutos}`;
});

const RackSemanal = () => {
  const [reservas, setReservas] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:9090/api/v1/reservas/')
      .then(response => {
        console.log("Reservas recibidas:", response.data); // Debug
        setReservas(response.data);
      })
      .catch(error => {
        console.error('Error al obtener reservas:', error);
      });
  }, []);

  const obtenerReserva = (dia, horaBloque) => {
    return reservas.find(r => {
      const fecha = new Date(r.fechaReserva); // Ajustar nombre de campo según el backend
      const diaReserva = fecha.getDay(); // 0 (Domingo) - 6 (Sábado)

      const horaReserva = fecha.getHours().toString().padStart(2, '0');
      const minutosReserva = fecha.getMinutes().toString().padStart(2, '0');
      const horaCompleta = `${horaReserva}:${minutosReserva}`;

      const indexDia = dia === 'Domingo' ? 0 : diasSemana.indexOf(dia) + 1;

      return diaReserva === indexDia && horaCompleta === horaBloque;
    });
  };

  return (
    <div className="overflow-x-auto p-4">
      <table className="table-auto border-collapse w-full text-sm text-center">
        <thead>
          <tr>
            <th className="border p-2 bg-gray-100">Hora</th>
            {diasSemana.map(dia => (
              <th key={dia} className="border p-2 bg-gray-100">{dia}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {bloquesHorarios.map(hora => (
            <tr key={hora}>
              <td className="border p-2 font-semibold bg-gray-50">{hora}</td>
              {diasSemana.map(dia => {
                const reserva = obtenerReserva(dia, hora);
                return (
                  <td key={`${dia}-${hora}`} className="border p-2">
                    {reserva ? (
                      <div className="text-green-600 font-medium">
                        Cliente: {reserva.rutCliente}<br />
                        Estado: {reserva.estado}
                      </div>
                    ) : '-'}
                  </td>
                );
              })}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default RackSemanal;
