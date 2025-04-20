import React, { useState } from 'react'
import axios from 'axios'

const ReportePersonas = () => {
  const [inicio, setInicio] = useState('2025-01')
  const [fin, setFin] = useState('2025-03')
  const [datos, setDatos] = useState([])

  const getFechaInicio = () => `${inicio}-01`

  const getFechaFin = () => {
    const [year, month] = fin.split('-')
    const lastDay = new Date(year, month, 0).getDate()
    return `${fin}-${lastDay}`
  }

  const fetchData = async () => {
    try {
      const response = await axios.get(
        `http://localhost:9090/api/v1/reservas/personas?inicio=${getFechaInicio()}&fin=${getFechaFin()}`
      )
      if (Array.isArray(response.data)) {
        setDatos(response.data)
      } else {
        console.error('Los datos obtenidos no son un arreglo:', response.data)
      }
    } catch (error) {
      console.error('Error al obtener el reporte', error)
    }
  }

  const handleConsultar = () => {
    fetchData()
  }

  const formatMoneda = (valor) =>
    valor != null ? valor.toLocaleString('es-CL', { style: 'currency', currency: 'CLP' }) : '0'

  // Calcular total general por mes y total global
  const obtenerTotalesGenerales = () => {
    const totalesPorMes = {}
    let totalGlobal = 0

    datos.forEach(fila => {
      for (const [mes, monto] of Object.entries(fila.montosPorMes)) {
        totalesPorMes[mes] = (totalesPorMes[mes] || 0) + monto
      }
      totalGlobal += fila.total
    })

    return { totalesPorMes, totalGlobal }
  }

  const { totalesPorMes, totalGlobal } = obtenerTotalesGenerales()

  const meses = datos.length > 0 ? Object.keys(datos[0].montosPorMes) : []

  return (
    <div className="bg-gray-100 p-4 rounded-md shadow mb-8">
      <h2 className="text-xl font-bold mb-4">Reporte por Cantidad de Personas</h2>

      <div className="flex items-center gap-4 mb-4">
        <label>Inicio: </label>
        <input
          type="month"
          value={inicio}
          onChange={(e) => setInicio(e.target.value)}
          className="border p-1 rounded"
        />
        <label>Fin: </label>
        <input
          type="month"
          value={fin}
          onChange={(e) => setFin(e.target.value)}
          className="border p-1 rounded"
        />
        <button 
          onClick={handleConsultar}
          className="bg-blue-500 text-white px-4 py-2 rounded ml-4"
        >
          Consultar
        </button>
      </div>

      <div className="overflow-auto">
        <table className="table-auto w-full text-sm text-left border border-gray-300">
          <thead>
            <tr className="bg-gray-200">
              <th className="p-2 border">N° Personas</th>
              {meses.map((mes) => (
                <th key={mes} className="p-2 border">{mes}</th>
              ))}
              <th className="p-2 border">TOTAL</th>
            </tr>
          </thead>
          <tbody>
            {Array.isArray(datos) && datos.length > 0 ? (
              <>
                {datos.map((fila, index) => (
                  <tr key={index} className="bg-white hover:bg-gray-50">
                    <td className="p-2 border">{fila.rango}</td>
                    {meses.map((mes, i) => (
                      <td key={i} className="p-2 border">{formatMoneda(fila.montosPorMes[mes])}</td>
                    ))}
                    <td className="p-2 border font-bold">{formatMoneda(fila.total)}</td>
                  </tr>
                ))}

                {/* Fila de Total General */}
                <tr className="bg-gray-100 font-bold">
                  <td className="p-2 border">Total General</td>
                  {meses.map((mes, i) => (
                    <td key={i} className="p-2 border">{formatMoneda(totalesPorMes[mes])}</td>
                  ))}
                  <td className="p-2 border">{formatMoneda(totalGlobal)}</td>
                </tr>
              </>
            ) : (
              <tr>
                <td colSpan="100%" className="p-2 text-center">Cargando datos...</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}

export default ReportePersonas
