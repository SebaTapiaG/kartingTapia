package karting.services;

import karting.entities.comprobanteEntity;
import karting.repositories.comprobanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class comprobanteService {
    @Autowired
    comprobanteRepository comprobanteRepository;

    public List<comprobanteEntity> getComprobantes() {
        return comprobanteRepository.findAll();
    }

    public comprobanteEntity getComprobanteByIdComprobante(Long idComprobante) {
        return comprobanteRepository.findByIdComprobante(idComprobante);
    }

    public comprobanteEntity saveComprobante(comprobanteEntity comprobante) {
        return comprobanteRepository.save(comprobante);
    }

    public comprobanteEntity updateComprobante(comprobanteEntity comprobante) {
        return comprobanteRepository.save(comprobante);
    }

    public boolean deleteComprobante(Long idComprobante) {
        comprobanteRepository.deleteById(idComprobante);
        return true;
    }

}
