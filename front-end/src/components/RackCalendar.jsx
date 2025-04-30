import React, { useEffect, useState } from 'react';
import axios from 'axios';

// Dias de la semana
const diasSemana = ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'];

// Bloques horarios
const bloquesHorarios = Array.from({ length: 8 }, (_, i) => {
  const hora = 10 + i;
  return `${hora}:00`;
});

const RackSemanal = () => {
  const [reservas, setReservas] = useState([]);
  const [startOfWeek, setStartOfWeek] = useState(() => {
    const today = new Date();
    const dayOfWeek = today.getDay(); // 0 (Sunday) - 6 (Saturday)
    const diff = dayOfWeek === 0 ? 0 : -dayOfWeek; // Adjust to start of week (Sunday)
    return new Date(today.setDate(today.getDate() + diff));
  });

  // Fetch reservations
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

  // Calculate dates for each day of the current week
  const weekDates = diasSemana.map((_, index) => {
    const date = new Date(startOfWeek);
    date.setDate(startOfWeek.getDate() + index);
    return date;
  });

  // Funcion para obtener reservas por fecha específica
  const obtenerReservasPorDia = (date) => {
    return reservas.filter(r => {
      const fechaReserva = new Date(r.fechaReserva);
      return (
        fechaReserva.getFullYear() === date.getFullYear() &&
        fechaReserva.getMonth() === date.getMonth() &&
        fechaReserva.getDate() === date.getDate()
      );
    });
  };

  // Funcion para obtener el indice de la fila correspondiente a la hora
  const timeToRowIndex = (time) => {
    const [hour] = time.split(':').map(Number);
    return (hour - 10) + 1; // Adjust for 10 AM start
  };

  // Calcula la hora de fin a partir de la hora de inicio
  const calculateEndTime = (startTime) => {
    const startDate = new Date(startTime);
    const endDate = new Date(startDate.getTime() + 60 * 60 * 1000); // Add 1 hour
    return endDate;
  };

  // Formato de la hora para mostrar (e.g., "10:00" -> "10am", "14:00" -> "2pm")
  const formatTime = (timeStr) => {
    const date = new Date(timeStr);
    const hour = date.getHours();
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const period = hour >= 12 ? 'pm' : 'am';
    const adjustedHour = hour % 12 || 12; // Convert 0 or 12 to 12
    return `${adjustedHour}${minutes !== '00' ? `:${minutes}` : ''}${period}`;
  };

  const formatTimeColumn = (hora) => {
    const hour = parseInt(hora.split(':')[0], 10);
    const period = hour >= 12 ? 'PM' : 'AM';
    const adjustedHour = hour % 12 || 12; // Convert 0 or 12 to 12
    return `${adjustedHour} ${period}`;
  };

  // Navigation functions
  const goToPreviousWeek = () => {
    const newStart = new Date(startOfWeek);
    newStart.setDate(startOfWeek.getDate() - 7);
    setStartOfWeek(newStart);
  };

  const goToNextWeek = () => {
    const newStart = new Date(startOfWeek);
    newStart.setDate(startOfWeek.getDate() + 7);
    setStartOfWeek(newStart);
  };

  // Format week range for display (e.g., "August 2021")
  const formatWeekRange = () => {
    const endOfWeek = new Date(startOfWeek);
    endOfWeek.setDate(startOfWeek.getDate() + 6);
    const month = startOfWeek.toLocaleString('default', { month: 'long' });
    const year = startOfWeek.getFullYear();
    return `${month} ${year}`;
  };

  return (
    <div className="p-4">
      {/* Navigation buttons and week range */}
      <div className="flex justify-between items-center mb-4">
        <button
          onClick={goToPreviousWeek}
          className="bg-gray-200 text-gray-700 px-4 py-2 rounded hover:bg-gray-300"
        >
          ← Semana Anterior
        </button>
        <div className="text-lg font-semibold text-gray-700">
          {formatWeekRange()}
        </div>
        <button
          onClick={goToNextWeek}
          className="bg-gray-200 text-gray-700 px-4 py-2 rounded hover:bg-gray-300"
        >
          Semana Siguiente →
        </button>
      </div>

      <div className="grid grid-cols-8 gap-1 text-sm text-center">
        {/* Header: Days of the week with dates */}
        <div className="font-semibold text-gray-600">Hora</div>
        {weekDates.map((date, index) => (
          <div key={index} className="font-semibold text-gray-600">
            {diasSemana[index]}<br />
            {date.getDate()}
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
            {weekDates.map((date, colIndex) => {
              const reservasDia = obtenerReservasPorDia(date);
              const reserva = reservasDia.find(r => {
                const fecha = new Date(r.fechaReserva);
                const horaReserva = `${fecha.getHours()}:00`;
                return horaReserva === hora;
              });

              return (
                <div
                  key={`${date}-${hora}`}
                  className="relative h-12 border border-gray-200"
                  style={{ gridRow: rowIndex + 2, gridColumn: colIndex + 2 }}
                >
                  {reserva && (
                    <div
                      className="absolute top-0 left-0 w-full bg-blue-500 text-white text-xs p-1 rounded"
                      style={{
                        height: `48px`,
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