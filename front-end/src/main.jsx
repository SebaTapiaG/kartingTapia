import React from 'react';
import { createRoot } from 'react-dom/client'; // Importación correcta de createRoot
import './index.css';
import App from './App.jsx';

// Crear el root y renderizar la aplicación
const root = createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
