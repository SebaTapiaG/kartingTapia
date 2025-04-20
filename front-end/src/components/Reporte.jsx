import React from "react";
import ReportePersonas from "./ReportePersonas"; // Asegúrate de tener los componentes bien ubicados
import ReporteVueltas from "./ReporteVueltas"; // Asegúrate de tener los componentes bien ubicados


const Reporte = () => {
  return (
    <div className="p-6 max-w-full mx-auto bg-white rounded shadow-md">
      <h1 className="text-2xl font-bold mb-6">Reportes Generales</h1>

      <div className="space-y-8">
        <div className="bg-gray-100 p-4 rounded-md shadow mb-8">
          <ReportePersonas /> {/* Asegúrate de tener el componente ReportePersonas importado correctamente */}
          
        </div>

        <div className="bg-gray-100 p-4 rounded-md shadow mb-8">
          <ReporteVueltas /> {/* Asegúrate de tener el componente ReporteVueltas importado correctamente */}
      
        </div>
       
        
      </div>
    </div>
  );
};

export default Reporte;
