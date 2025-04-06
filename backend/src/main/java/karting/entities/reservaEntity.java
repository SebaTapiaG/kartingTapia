package karting.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class reservaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)

    private long idReserva;

    private String rutCliente;
    private String idKart;
    private Date fechaReserva;
    private Date horaInicio;
    private Date horaTermino;
    private int cantidadPersonas;
    private double descuento;
    private double montoTotal;
    private String estado;

}