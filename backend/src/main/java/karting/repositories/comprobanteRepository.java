package karting.repositories;

import karting.entities.comprobanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface comprobanteRepository extends JpaRepository<comprobanteEntity, Long> {
    public comprobanteEntity findByIdComprobante(Long idComprobante);
}