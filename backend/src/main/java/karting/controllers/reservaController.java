package karting.controllers;

import karting.entities.reservaEntity;
import karting.services.reservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/")
    public String crearReserva(@RequestBody reservaEntity reserva) {
        reservaService.crearReserva(reserva);
        return "Reserva creada exitosamente";
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