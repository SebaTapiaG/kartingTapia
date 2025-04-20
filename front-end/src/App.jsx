
import './App.css'
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import Home from './components/home'
import AddEditReserva from './components/AddEditReserva'
import ListReserva from './components/ListReserva'
import FormularioReserva from './components/FormularioReserva'
import Navbar from './components/Navbar'
import AddEditCliente from './components/AddEditCliente'
import RackSemanal from './components/RackCalendar'
import ReportePersonas from './components/ReportePersonas'
import ReporteVueltas from './components/ReporteVueltas'
import Reporte from './components/Reporte'



function App() {
  return (
    <Router>
      <div className="container">
        <Navbar />
        <h1>Reservas de Karting</h1>
        <p>Bienvenido a la aplicación de reservas de karting. Aquí puedes agregar, editar y listar tus reservas.</p>
        <p>Utiliza el menú de navegación para acceder a las diferentes secciones.</p>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/reservas/add" element={<AddEditReserva />} />
          <Route path="/reservas/edit/:id" element={<AddEditReserva />} />
          <Route path="/reservas/list" element={<ListReserva />} />
          <Route path="/reservas/formulario/:id" element={<FormularioReserva />} />
          <Route path="/clientes/add" element={<AddEditCliente />} />
          <Route path="/clientes/edit/:id" element={<AddEditCliente />} />
          <Route path="/rack" element={<RackSemanal />} />
          <Route path="/reporte" element={<Reporte />} />
          <Route path="/reporte/personas" element={<ReportePersonas />} />
          <Route path="/reporte/vueltas" element={<ReporteVueltas />} />
      
        </Routes>
      </div>
    </Router>
   
  
  )
}

export default App
