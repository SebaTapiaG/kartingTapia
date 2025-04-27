-- Insertar datos en la tabla cliente
INSERT INTO cliente (rut, nombre, correo, cantidad_reservas, fecha_nacimiento)
VALUES
    ('12345678-9', 'Juan Pérez', 'juan.perez@example.com', 5, '1990-05-10'),
    ('98765432-1', 'Ana Gómez', 'ana.gomez@example.com', 3, '1985-11-23'),
    ('11122333-4', 'Carlos Fernández', 'carlos.fernandez@example.com', 2, '1992-07-15');

-- Insertar datos en la tabla kart
INSERT INTO kart (nombre_kart, mantenimiento)
VALUES
    ('Kart A', false),
    ('Kart B', false),
    ('Kart C', true);

-- Insertar datos en la tabla reserva
INSERT INTO reserva (
    rut_cliente,
    fecha_reserva,
    cantidad_personas,
    descuento,
    monto_total,
    cant_vueltas,
    tiempo_max,
    tiempo_reserva,
    estado
)
VALUES
    ('12345678-9', '2025-04-01 10:00:00', 2, 10.0, 15000.0, 20, 30, 60, 'CONFIRMADA'),
    ('98765432-1', '2025-04-02 14:00:00', 3, 15.0, 25000.0, 25, 45, 90, 'CONFIRMADA'),
    ('11122333-4', '2025-04-03 09:00:00', 1, 5.0, 30000.0, 15, 30, 60, 'PENDIENTE');

-- Insertar datos en la tabla comprobante
INSERT INTO comprobante (
    rut_cliente,
    nombre_cliente,
    correo_cliente,
    id_reserva,
    fecha_emision,
    descuento,
    monto_total,
    estado,
    cant_vueltas,
    tiempo_max
)
VALUES
    ('12345678-9', 'Juan Pérez', 'juan.perez@example.com', 1, '2025-04-01', 10, 15000, 'EMITIDO', 20, 30),
    ('98765432-1', 'Ana Gómez', 'ana.gomez@example.com', 2, '2025-04-02', 15, 25000, 'EMITIDO', 25, 45),
    ('11122333-4', 'Carlos Fernández', 'carlos.fernandez@example.com', 3, '2025-04-03', 5, 30000, 'PENDIENTE', 15, 30);
