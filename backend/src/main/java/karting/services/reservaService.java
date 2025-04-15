package karting.services;


import karting.entities.clienteEntity;
import karting.entities.comprobanteEntity;
import karting.entities.reservaEntity;
import karting.repositories.clienteRepository;
import karting.repositories.comprobanteRepository;
import karting.repositories.reservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class reservaService {
    @Autowired
    reservaRepository reservaRepository;

    @Autowired
    clienteService clienteService;

    @Autowired
    clienteRepository clienteRepository;

    @Autowired
    comprobanteRepository comprobanteRepository;

    public reservaEntity crearReserva(String rut, Date fecha, int cantidadPersonas, double montoTotal) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        reservaEntity reserva = new reservaEntity();
        reserva.setRutCliente(rut);
        reserva.setFechaReserva(fecha);
        reserva.setCantidadPersonas(cantidadPersonas);
        reserva.setMontoTotal(montoTotal);
        reserva.setEstado("PENDIENTE");

        // Asignar vueltas y tiempo máximo según el monto total
        asignarVueltasYTiempo(reserva);

        // Aplicar tarifa especial
        aplicarTarifa(reserva);

        // Aplicar descuentos
        aplicarDescuento(reserva);

        // Guardar la reserva
        guardarReserva(reserva);
        return reserva;
    }

    public void guardarReserva(reservaEntity reserva) {
        // Lógica para crear una reserva
        reservaRepository.save(reserva);

        //Buscar el cliente por su rut
        Optional<clienteEntity> clienteOptional = clienteRepository.findByRut(reserva.getRutCliente());
        if (clienteOptional.isPresent()) {
            clienteEntity cliente = clienteOptional.get();
            // Aumentar la cantidad de reservas del cliente
            cliente.setCantidadReservas(cliente.getCantidadReservas() + 1);
            clienteRepository.save(cliente);
        }else {
            // Manejo de errores
            throw new RuntimeException("No se puede crear la reserva");
        }
    }

    public List<reservaEntity> getReservas() {
        return reservaRepository.findAll();
    }

    public reservaEntity getReservaByIdReserva(Long idReserva) {
        return reservaRepository.findByIdReserva(idReserva);
    }

    public reservaEntity saveReserva(reservaEntity reserva) {
        return reservaRepository.save(reserva);
    }

    public reservaEntity updateReserva(reservaEntity reserva) {
        return reservaRepository.save(reserva);
    }

    public boolean deleteReserva(Long idReserva) {
        reservaRepository.deleteById(idReserva);
        return true;
    }
    //Asignar cantidad de vueltas y tiempo maximo segun el monto total
    public void asignarVueltasYTiempo(reservaEntity reserva) {
        double montoTotal = reserva.getMontoTotal();
        int cantVueltas=0;
        int tiempoMax=0;
        int tiempoReserva=0;

        if (montoTotal == 15000) {
            cantVueltas = 10;
            tiempoMax = 10;
            tiempoReserva = 30;
        } else if (montoTotal == 20000) {
            cantVueltas = 15;
            tiempoMax = 15;
            tiempoReserva = 35;
        } else if (montoTotal == 25000) {
            cantVueltas = 20;
            tiempoMax = 20;
            tiempoReserva = 40;
        }

        reserva.setCantVueltas(cantVueltas);
        reserva.setTiempoMax(tiempoMax);
        reserva.setTiempoReserva(tiempoReserva);
    }

    //Aplicar tarifa especial
    public void aplicarTarifa(reservaEntity reserva) {
        // Lógica para aplicar tarifas especiales a la reserva
        double tarifaXdia = obtenerTarifaXdia(reserva);
        double precioTotal = reserva.getMontoTotal();
        double precioConTarifa = precioTotal * tarifaXdia;
        reserva.setMontoTotal(precioConTarifa);
        reservaRepository.save(reserva);
    }

    //Adquirir Descuentos
    public void aplicarDescuento(reservaEntity reserva) {
        // Lógica para aplicar descuentos a la reserva

        double descuentoXcant = obtenerDescuentoXcant(reserva);
        double descuentoXcl = obtenerDescuentoXcl(reserva);
        double descuentoXcumple = obtenerDescuentoXcumple(reserva);

        // Comparar descuentos y usar el mayor
        double descuentoMayor = Math.max(descuentoXcant, Math.max(descuentoXcl, descuentoXcumple));
        double precioTotal = reserva.getMontoTotal();
        double precioConDescuento = precioTotal - (precioTotal * descuentoMayor);
        reserva.setMontoTotal(precioConDescuento);
        reserva.setDescuento(descuentoMayor);
        reservaRepository.save(reserva);
    }

    //Descuento por numero de personas
    public Double obtenerDescuentoXcant(reservaEntity reserva) {

        double descuentoXcant = 0.0;
        //Descuento por numero de personas
        if (reserva.getCantidadPersonas() >= 3 && reserva.getCantidadPersonas() <= 5) {
            descuentoXcant = 0.1; // 10% de descuento
        } else if (reserva.getCantidadPersonas() >= 6 && reserva.getCantidadPersonas() <= 10) {
            descuentoXcant = 0.2; // 20% de descuento
        } else if (reserva.getCantidadPersonas() > 10) {
            descuentoXcant = 0.3; // 30% de descuento
        }

        return descuentoXcant;
    }

    //Descuento para clientes frecuentes
    public Double obtenerDescuentoXcl(reservaEntity reserva) {
        double descuentoXcl = 0.0;

        // Obtener cantidad de visitas del cliente
        String rutCliente = reserva.getRutCliente();
        int visitasMes = clienteService.getCantidadReservas(rutCliente);

        //Descuento por Numero de visitas al mes
        if (visitasMes >= 2 && visitasMes<= 4) {
            descuentoXcl = 0.1; // 10% de descuento
        } else if (visitasMes >= 5 && visitasMes <= 6) {
            descuentoXcl = 0.2; // 20% de descuento
        } else if (visitasMes >= 7) {
            descuentoXcl = 0.3; // 30% de descuento
        }

        return descuentoXcl;
    }

    //Descuento por cumpleaños
    public Double obtenerDescuentoXcumple(reservaEntity reserva) {
        double descuentoXcumple = 0.0;

        // Obtener fecha de cumpleaños del cliente
        String rutCliente = reserva.getRutCliente();
        Date fechaCum = clienteService.getFechaNacimiento(rutCliente); // Suponiendo que retorna java.util.Date

        // Convertir fechas a LocalDate
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaCumple = fechaCum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Comparar día y mes
        if (fechaActual.getMonthValue() == fechaCumple.getMonthValue() &&
                fechaActual.getDayOfMonth() == fechaCumple.getDayOfMonth()) {
            descuentoXcumple = 0.5; // 50% de descuento
        }

        return descuentoXcumple;
    }

    //Tarifa para dias especiales
    public Double obtenerTarifaXdia(reservaEntity reserva) {
        double tarifaXdia;

        // Obtener fecha de la reserva
        Date fechaReserva = reserva.getFechaReserva();
        LocalDate fechaReservaLocal = fechaReserva.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Comparar día y mes
        if (fechaReservaLocal.getMonthValue() == 9 && fechaReservaLocal.getDayOfMonth() == 18) {
            tarifaXdia = 1.5; // Tarifa especial para el 18 de septiembre
        }
        // Tarifa especial para el 1 de enero
        else if (fechaReservaLocal.getMonthValue() == 1 && fechaReservaLocal.getDayOfMonth() == 1) {
            tarifaXdia = 2.0; // Tarifa especial para el 1 de enero
        }
        // Tarifa especial para el 25 de diciembre
        else if (fechaReservaLocal.getMonthValue() == 12 && fechaReservaLocal.getDayOfMonth() == 25) {
            tarifaXdia = 2.5; // Tarifa especial para el 25 de diciembre
        }
        //Tarifa especial para dias Sabados y Domingos
        else if (fechaReservaLocal.getDayOfWeek().getValue() == 6 || fechaReservaLocal.getDayOfWeek().getValue() == 7) {
            tarifaXdia = 1.2; // Tarifa especial para fines de semana
        } else {
            tarifaXdia = 1.0; // Tarifa normal
        }

        return tarifaXdia;
    }

    public void generarComprobanteDesdeReserva(reservaEntity reserva) {
        comprobanteEntity comprobante = new comprobanteEntity();

        String rutCliente = reserva.getRutCliente();
        comprobante.setRutCliente(rutCliente);
        comprobante.setNombreCliente(clienteService.getNombreCliente(rutCliente));
        comprobante.setIdReserva(reserva.getIdReserva());
        comprobante.setFechaEmision(new Date()); // Emisión actual
        comprobante.setDescuento((int) (reserva.getDescuento() * 100)); // Si el descuento es 0.15 → 15%
        comprobante.setMontoTotal((int) reserva.getMontoTotal());
        comprobante.setEstado("Emitido");
        comprobante.setCantVueltas(reserva.getCantVueltas());
        comprobante.setTiempoMax(reserva.getTiempoMax());

        comprobanteRepository.save(comprobante);
    }

    public reservaEntity confirmarReserva(long idReserva) {
        // 1. Buscar la reserva por su ID
        Optional<reservaEntity> reservaOptional = reservaRepository.findById(idReserva);

        if (reservaOptional.isEmpty()) {
            throw new RuntimeException("Reserva no encontrada con ID: " + idReserva);
        }

        reservaEntity reserva = reservaOptional.get();

        // 2. Aplicar descuento
        aplicarDescuento(reserva);

        // 3. Cambiar el estado a "CONFIRMADA"
        reserva.setEstado("CONFIRMADA");

        // 4. Guardar la reserva actualizada
        reservaRepository.save(reserva);

        // 5. Generar comprobante
        generarComprobanteDesdeReserva(reserva);

        return reserva;
    }


}