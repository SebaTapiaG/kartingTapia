

-- Insertar datos en la tabla cliente
INSERT INTO cliente (rut, nombre, correo, cantidad_reservas, fecha_nacimiento)
VALUES
    ('12345678-9', 'Juan Pérez', 'juan.perez@example.com', 5, '1990-05-10'),
    ('98765432-1', 'Ana Gómez', 'ana.gomez@example.com', 3, '1985-11-23'),
    ('11122333-4', 'Carlos Fernández', 'carlos.fernandez@example.com', 2, '1992-07-15');

-- Insertar datos en la tabla kart
INSERT INTO kart (id_kart, nombre_kart, mantenimiento)
VALUES
    (1, 'Kart A', false),
    (2, 'Kart B', false),
    (3, 'Kart C', true);

-- Insertar datos en la tabla reserva

INSERT INTO reserva (id_reserva, rut_cliente, id_kart, fecha_reserva, hora_inicio, hora_termino, cantidad_personas, descuento, monto_total, estado)
VALUES
    (1, '12345678-9', '1', '2025-04-01', '2025-04-01 10:00:00', '2025-04-01 12:00:00', 2, 10.0, 5000.0, 'Confirmada'),
    (2, '98765432-1', '2', '2025-04-02', '2025-04-02 14:00:00', '2025-04-02 16:00:00', 3, 15.0, 7500.0, 'Confirmada'),
    (3, '11122333-4', '3', '2025-04-03', '2025-04-03 09:00:00', '2025-04-03 11:00:00', 1, 5.0, 2500.0, 'Pendiente');

-- Insertar datos en la tabla comprobante
INSERT INTO comprobante (id_reserva, fecha_emision, descuento, monto_total, estado)
VALUES
    (1, '2025-04-01', 10, 4500.0, 'Emitido'),
    (2, '2025-04-02', 15, 6375.0, 'Emitido'),
    (3, '2025-04-03', 5, 2375.0, 'Pendiente');
