import React from 'react';
import kartingImage from '../assets/images/karting.jpeg'; // Ajusta la ruta según tu estructura

const Home = () => {
    return (
        <div className="home-container">
            <div className="text-content">
                <h1>Reservas de Karting</h1>
                <p>Bienvenido a la aplicación de reservas de karting. Aquí puedes agregar, editar y listar tus reservas.</p>
                <p>Utiliza el menú de navegación para acceder a las diferentes secciones.</p>
            </div>
            
            {/* Imagen de karting */}
            <div className="image-container">
                <img 
                    src={kartingImage} 
                    alt="Karting de competición" 
                    className="karting-image"
                />
            </div>
        </div>
    );
}

export default Home;