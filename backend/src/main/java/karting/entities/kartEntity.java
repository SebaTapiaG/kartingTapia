package karting.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "kart")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class kartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int idKart;

    private String nombreKart;

    private boolean mantenimiento;

}