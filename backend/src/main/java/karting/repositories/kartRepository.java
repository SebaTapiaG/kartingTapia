package karting.repositories;

import karting.entities.kartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface kartRepository extends JpaRepository<kartEntity, Integer> {
    public kartEntity findByIdKart(int idKart);
}