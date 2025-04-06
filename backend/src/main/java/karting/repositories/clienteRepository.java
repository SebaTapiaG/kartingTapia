package karting.repositories;

import karting.entities.clienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface clienteRepository extends JpaRepository<clienteEntity, String> {
    Optional<clienteEntity> findByRut(String rut);
    clienteEntity findByNombre(String nombre);
    clienteEntity findByCorreo(String correo);
    //clienteEntity aumentarReservas(String rut);

}