package karting.controllers;

import karting.entities.reservaEntity;
import karting.services.reservaService;
import karting.Dtos.CrearReservaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
@CrossOrigin("*")
public class reservaController {
    @Autowired
    reservaService reservaService;

    @GetMapping("/")
    public List<reservaEntity> listReservas() {
        return reservaService.getReservas();
    }

    @GetMapping("/{idReserva}")
    public reservaEntity getReservaByIdReserva(@PathVariable Long idReserva) {
        return reservaService.getReservaByIdReserva(idReserva);
    }

    @PostMapping("/crear")
    public reservaEntity crearReserva(@RequestBody CrearReservaRequest req) {
        if (req.getFecha() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha no puede ser nula");
        }
        return reservaService.crearReserva(
                req.getRut(),
                req.getFecha(),
                req.getCantidadPersonas(),
                req.getMontoTotal()
        );
    }


    @PostMapping("/")
    public ResponseEntity<reservaEntity> saveReserva(@RequestBody reservaEntity reserva) {
        reservaEntity reservaNew = reservaService.saveReserva(reserva);
        return ResponseEntity.ok(reservaNew);
    }
    

    @PostMapping("/descuento")
    public ResponseEntity<reservaEntity> aplicarDescuento(@RequestBody reservaEntity reserva) {
        reservaService.aplicarDescuento(reserva);
        return ResponseEntity.ok(reserva);
    }

    @PostMapping("/tarifa")
    public ResponseEntity<reservaEntity> aplicarTarifa(@RequestBody reservaEntity reserva) {
        reservaService.aplicarTarifa(reserva);
        return ResponseEntity.ok(reserva);
    }

    @PutMapping("/confirmar/{id}")
    public ResponseEntity<reservaEntity> confirmarReserva(@PathVariable Long id) {
        try {
            reservaEntity reservaConfirmada = reservaService.confirmarReserva(id);
            return ResponseEntity.ok(reservaConfirmada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/")
    public reservaEntity updateReserva(@RequestBody reservaEntity reserva){
        return reservaService.updateReserva(reserva);
    }

    @DeleteMapping("/{idReserva}")
    public boolean deleteReservaByIdReserva(@PathVariable Long idReserva) throws Exception {
        // Verificar si la reserva existe
        reservaEntity reserva = reservaService.getReservaByIdReserva(idReserva);
        if (reserva == null) {
            throw new Exception("Reserva no encontrada");
        }
        return reservaService.deleteReserva(idReserva);
    }

}
