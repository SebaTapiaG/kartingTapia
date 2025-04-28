import React, { useState, useEffect } from 'react'
import axios from 'axios'

const ReporteVueltas = () => {
  const [inicio, setInicio] = useState('2025-04')
  const [fin, setFin] = useState('2025-04')
  const [datos, setDatos] = useState([])
  const [cargando, setCargando] = useState(false)

  const getFechaInicio = () => `${inicio}-01`
  
  const getFechaFin = () => {
    const [year, month] = fin.split('-')
    const lastDay = new Date(year, month, 0).getDate()
    return `${fin}-${lastDay}`
  }

  const fetchData = async () => {
    setCargando(true)
    try {
      const response = await axios.get(
        `http://kart-app.brazilsouth.cloudapp.azure.com:9090/api/v1/reservas/vueltas`,
        {
          params: {
            inicio: getFechaInicio(),
            fin: getFechaFin()
          }
        }
      )
      
      // Convertir el objeto a array para facilitar el renderizado
      const datosArray = Object.entries(response.data)
        .filter(([key]) => key !== 'TOTAL') // Excluir el total general
        .map(([rango, valores]) => ({
          rango,
          ...valores,
          total: valores.TOTAL || 0
        }))
      
      // Agregar el total general como última fila
      if (response.data.TOTAL) {
        datosArray.push({
          rango: 'TOTAL GENERAL',
          ...response.data.TOTAL,
          total: response.data.TOTAL.TOTAL || 0
        })
      }
      
      setDatos(datosArray)
    } catch (error) {
      console.error('Error al obtener el reporte', error)
    } finally {
      setCargando(false)
    }
  }

  useEffect(() => {
    fetchData()
  }, [inicio, fin])

  const formatMoneda = (valor) => {
    if (valor === null || valor === undefined) return '$0'
    return new Intl.NumberFormat('es-CL', {
      style: 'currency',
      currency: 'CLP',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(valor)
  }

  // Obtener todos los meses únicos para las columnas
  const getMeses = () => {
    const mesesSet = new Set()
    
    datos.forEach(fila => {
      Object.keys(fila).forEach(key => {
        if (key !== 'rango' && key !== 'total' && key !== 'TOTAL') {
          mesesSet.add(key)
        }
      })
    })
    
    return Array.from(mesesSet).sort((a, b) => {
      // Ordenar cronológicamente
      return new Date(a.split(' ')[1], mesesES.indexOf(a.split(' ')[0])) - 
             new Date(b.split(' ')[1], mesesES.indexOf(b.split(' ')[0]))
    })
  }

  const mesesES = [
    'enero', 'febrero', 'marzo', 'abril', 'mayo', 'junio',
    'julio', 'agosto', 'septiembre', 'octubre', 'noviembre', 'diciembre'
  ]

  const meses = getMeses()

  return (
    <div className="bg-gray-100 p-4 rounded-md shadow mb-8">
      <h2 className="text-xl font-bold mb-4">Reporte por Vueltas o Tiempo Máximo</h2>

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
      </div>

      <button 
        onClick={fetchData}
        disabled={cargando}
        className="bg-blue-500 text-white px-4 py-2 rounded mb-4 hover:bg-blue-600"
      >
        {cargando ? 'Cargando...' : 'Actualizar'}
      </button>

      <div className="overflow-auto">
        <table className="table-auto w-full text-sm text-left border border-gray-300">
          <thead>
            <tr className="bg-gray-200">
              <th className="p-2 border">N° de vueltas o tiempo máximo</th>
              {meses.map((mes) => (
                <th key={mes} className="p-2 border text-center capitalize">
                  {mes}
                </th>
              ))}
              <th className="p-2 border text-center">TOTAL</th>
            </tr>
          </thead>
          <tbody>
            {cargando ? (
              <tr>
                <td colSpan={meses.length + 2} className="p-4 text-center">
                  Cargando datos...
                </td>
              </tr>
            ) : datos.length > 0 ? (
              datos.map((fila, index) => (
                <tr 
                  key={index} 
                  className={`${index % 2 === 0 ? 'bg-white' : 'bg-gray-50'} hover:bg-gray-100`}
                >
                  <td className="p-2 border font-medium">
                    {fila.rango}
                  </td>
                  {meses.map((mes) => (
                    <td key={mes} className="p-2 border text-right">
                      {formatMoneda(fila[mes])}
                    </td>
                  ))}
                  <td className="p-2 border text-right font-bold">
                    {formatMoneda(fila.total)}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={meses.length + 2} className="p-4 text-center">
                  No hay datos disponibles para el rango seleccionado
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}

export default ReporteVueltas