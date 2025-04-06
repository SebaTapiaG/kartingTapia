package karting.services;


import karting.entities.clienteEntity;
import karting.entities.reservaEntity;
import karting.repositories.clienteRepository;
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

    public void crearReserva(reservaEntity reserva) {
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


}