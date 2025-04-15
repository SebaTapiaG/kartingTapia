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

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReserva;

    private int cantidadPersonas;
    //private String rutPersonas; //Contiene los rut de las personas separadas por comas
    private double descuento;
    private double montoTotal;
    private int cantVueltas;
    private int tiempoMax;
    private int tiempoReserva;
    private String estado;

}