package karting.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "comprobante")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class comprobanteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long idComprobante;

    private String rutCliente;
    private String nombreCliente;
    private long idReserva;
    private Date fechaEmision;
    private int descuento;
    private int montoTotal;
    private String estado;
    private int cantVueltas;
    private int tiempoMax;
}