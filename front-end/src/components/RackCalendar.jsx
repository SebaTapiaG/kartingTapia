import React, { useEffect, useState } from 'react';
import axios from 'axios';

// Days of the week (starting from Sunday to match the image)
const diasSemana = ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'];

// Generate time slots from 10:00 AM to 5:00 PM in 1-hour increments
const bloquesHorarios = Array.from({ length: 8 }, (_, i) => {
  const hora = 10 + i;
  return `${hora}:00`;
});

const RackSemanal = () => {
  const [reservas, setReservas] = useState([]);

  useEffect(() => {
    axios.get('http://kart-app.brazilsouth.cloudapp.azure.com:9090/api/v1/reservas/')
      .then(response => {
        console.log("Reservas recibidas:", response.data);
        setReservas(response.data);
      })
      .catch(error => {
        console.error('Error al obtener reservas:', error);
      });
  }, []);

  // Helper function to get reservations for a specific day
  const obtenerReservasPorDia = (dia) => {
    return reservas.filter(r => {
      const fecha = new Date(r.fechaReserva);
      const diaReserva = fecha.getDay(); // 0 (Domingo) - 6 (Sábado)
      const indexDia = dia === 'Domingo' ? 0 : diasSemana.indexOf(dia);
      return diaReserva === indexDia;
    });
  };

  // Convert time to a row index for positioning
  const timeToRowIndex = (time) => {
    const [hour] = time.split(':').map(Number);
    return (hour - 10) + 1; // Adjust for 10 AM start
  };

  // Calculate end time (default 1 hour after start)
  const calculateEndTime = (startTime) => {
    const startDate = new Date(startTime);
    const endDate = new Date(startDate.getTime() + 60 * 60 * 1000); // Add 1 hour
    return endDate;
  };

  // Format time for display (e.g., "10:00" -> "10am", "14:00" -> "2pm")
  const formatTime = (timeStr) => {
    const date = new Date(timeStr);
    const hour = date.getHours();
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const period = hour >= 12 ? 'pm' : 'am';
    const adjustedHour = hour % 12 || 12; // Convert 0 or 12 to 12
    return `${adjustedHour}${minutes !== '00' ? `:${minutes}` : ''}${period}`;
  };

  // Format time for the time column (e.g., "10:00" -> "10 AM", "14:00" -> "2 PM")
  const formatTimeColumn = (hora) => {
    const hour = parseInt(hora.split(':')[0], 10);
    const period = hour >= 12 ? 'PM' : 'AM';
    const adjustedHour = hour % 12 || 12; // Convert 0 or 12 to 12
    return `${adjustedHour} ${period}`;
  };

  return (
    <div className="p-4">
      <div className="grid grid-cols-8 gap-1 text-sm text-center">
        {/* Header: Days of the week */}
        <div className="font-semibold text-gray-600">Hora</div>
        {diasSemana.map((dia, index) => (
          <div key={index} className="font-semibold text-gray-600">
            {dia}
          </div>
        ))}

        {/* Time slots and events */}
        {bloquesHorarios.map((hora, rowIndex) => (
          <React.Fragment key={hora}>
            {/* Time column */}
            <div className="text-gray-500 text-right pr-2">
              {formatTimeColumn(hora)}
            </div>

            {/* Day columns */}
            {diasSemana.map((dia, colIndex) => {
              const reservasDia = obtenerReservasPorDia(dia);
              const reserva = reservasDia.find(r => {
                const fecha = new Date(r.fechaReserva);
                const horaReserva = `${fecha.getHours()}:00`;
                return horaReserva === hora;
              });

              return (
                <div
                  key={`${dia}-${hora}`}
                  className="relative h-12 border border-gray-200"
                  style={{ gridRow: rowIndex + 2, gridColumn: colIndex + 2 }}
                >
                  {reserva && (
                    <div
                      className="absolute top-0 left-0 w-full bg-blue-500 text-white text-xs p-1 rounded"
                      style={{
                        height: `48px`, // Fixed height for 1 hour (48px per hour)
                      }}
                    >
                      {`Cliente: ${reserva.rutCliente}`}
                      <br />
                      {`${formatTime(reserva.fechaReserva)} - ${formatTime(reserva.horaFin || calculateEndTime(reserva.fechaReserva))}`}
                    </div>
                  )}
                </div>
              );
            })}
          </React.Fragment>
        ))}
      </div>

      {/* Inline CSS for styling */}
      <style jsx>{`
        .grid {
          display: grid;
          grid-template-columns: 60px repeat(7, 1fr);
          grid-template-rows: auto repeat(${bloquesHorarios.length}, 48px);
        }
        .relative {
          position: relative;
        }
      `}</style>
    </div>
  );
};

export default RackSemanal;