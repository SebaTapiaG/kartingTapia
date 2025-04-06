package karting.services;

import karting.entities.kartEntity;
import karting.repositories.kartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class kartService {
    @Autowired
    kartRepository kartRepository;

    public List<kartEntity> getKarts() {
        return kartRepository.findAll();
    }

    public kartEntity getKartById(int idKart) {
        return kartRepository.findByIdKart(idKart);
    }

    public kartEntity saveKart(kartEntity kart) {
        return kartRepository.save(kart);
    }

    public kartEntity updateKart(kartEntity kart) {
        return kartRepository.save(kart);
    }

    public boolean deleteKart(int idKart) {
        kartRepository.deleteById(idKart);
        return true;
    }

}