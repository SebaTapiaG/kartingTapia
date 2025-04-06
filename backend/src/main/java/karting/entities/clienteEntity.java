package karting.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class clienteEntity {

    @Id
    @Column(unique = true, nullable = false)
    private String rut;

    private String nombre;
    private String correo;
    private int cantidadReservas;
    private Date fechaNacimiento;

}