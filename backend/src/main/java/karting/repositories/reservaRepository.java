package karting.repositories;

import karting.entities.reservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface reservaRepository extends JpaRepository<reservaEntity, Long> {
    public reservaEntity findByIdReserva(long idReserva);


}