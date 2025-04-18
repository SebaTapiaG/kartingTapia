package karting.services;

import karting.entities.clienteEntity;
import karting.repositories.clienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class clienteService {
    @Autowired
    clienteRepository clienteRepository;

    public List<clienteEntity> getClientes() {
        return clienteRepository.findAll();
    }

    public Optional<clienteEntity> getClienteByRut(String rut) {
        return clienteRepository.findByRut(rut);
    }

    public int getCantidadReservas(String rut) {
        Optional<clienteEntity> cliente = clienteRepository.findByRut(rut);
        if (cliente.isPresent()) {
            return cliente.get().getCantidadReservas();
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }

    public Date getFechaNacimiento(String rut) {
        Optional<clienteEntity> cliente = clienteRepository.findByRut(rut);
        if (cliente.isPresent()) {
            return cliente.get().getFechaNacimiento();
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }

    public String getNombreCliente(String rut) {
        Optional<clienteEntity> cliente = clienteRepository.findByRut(rut);
        if (cliente.isPresent()) {
            return cliente.get().getNombre();
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }

    public String getCorreoCliente(String rut) {
        Optional<clienteEntity> cliente = clienteRepository.findByRut(rut);
        if (cliente.isPresent()) {
            return cliente.get().getCorreo();
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }

    public clienteEntity saveCliente(clienteEntity cliente) {
        return clienteRepository.save(cliente);
    }

    public clienteEntity updateCliente(clienteEntity cliente) {
        return clienteRepository.save(cliente);
    }

    public boolean deleteCliente(String rut) {
        clienteRepository.deleteById(rut);
        return true;
    }


}