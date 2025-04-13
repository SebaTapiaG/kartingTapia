package karting.controllers;

import karting.entities.reservaEntity;
import karting.services.reservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public ResponseEntity<ReservaResponse> crearReserva(@RequestBody CrearReservaRequest request) {
        reservaEntity reservaCreada = reservaService.crearReserva(
                request.getRut(),
                request.getFecha(),
                request.getHora(),
                request.getCantidadPersonas(),
                request.getMontoTotal()
        );

        ReservaResponse response = new ReservaResponse(
                reservaCreada.getIdReserva(),
                reservaCreada.getRutCliente(),
                reservaCreada.getFechaReserva(),
                reservaCreada.getHoraInicio(),
                reservaCreada.getCantidadPersonas(),
                reservaCreada.getMontoTotal(),
                reservaCreada.getCantVueltas(),
                reservaCreada.getTiempoMax(),
                reservaCreada.getEstado()
        );

        return ResponseEntity.ok(response);
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



    // DTOs para request/response
    public static class CrearReservaRequest {
        private String rut;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date fecha;
        @DateTimeFormat(pattern = "HH:mm:ss")
        private Date hora;
        private int cantidadPersonas;
        private double montoTotal;

        // Getters y Setters
        public String getRut() { return rut; }
        public void setRut(String rut) { this.rut = rut; }
        public Date getFecha() { return fecha; }
        public void setFecha(Date fecha) { this.fecha = fecha; }
        public Date getHora() { return hora; }
        public void setHora(Date hora) { this.hora = hora; }
        public int getCantidadPersonas() { return cantidadPersonas; }
        public void setCantidadPersonas(int cantidadPersonas) { this.cantidadPersonas = cantidadPersonas; }
        public double getMontoTotal() { return montoTotal; }
        public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }
    }

    public static class ReservaResponse {
        private Long idReserva;
        private String rutCliente;
        private Date fechaReserva;
        private Date horaInicio;
        private int cantidadPersonas;
        private double montoTotal;
        private int cantVueltas;
        private int tiempoMax;
        private String estado;

        public ReservaResponse(Long idReserva, String rutCliente, Date fechaReserva,
                               Date horaInicio, int cantidadPersonas, double montoTotal,
                               int cantVueltas, int tiempoMax, String estado) {
            this.idReserva = idReserva;
            this.rutCliente = rutCliente;
            this.fechaReserva = fechaReserva;
            this.horaInicio = horaInicio;
            this.cantidadPersonas = cantidadPersonas;
            this.montoTotal = montoTotal;
            this.cantVueltas = cantVueltas;
            this.tiempoMax = tiempoMax;
            this.estado = estado;
        }

        // Getters
        public Long getIdReserva() { return idReserva; }
        public String getRutCliente() { return rutCliente; }
        public Date getFechaReserva() { return fechaReserva; }
        public Date getHoraInicio() { return horaInicio; }
        public int getCantidadPersonas() { return cantidadPersonas; }
        public double getMontoTotal() { return montoTotal; }
        public int getCantVueltas() { return cantVueltas; }
        public int getTiempoMax() { return tiempoMax; }
        public String getEstado() { return estado; }
    }
}
