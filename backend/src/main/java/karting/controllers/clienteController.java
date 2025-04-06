package karting.controllers;

import karting.entities.clienteEntity;
import karting.services.clienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/clientes")
@CrossOrigin("*")
public class clienteController {
    @Autowired
    clienteService clienteService;

    @GetMapping("/")
    public ResponseEntity<List<clienteEntity>> listClientes() {
        List<clienteEntity> clientes = clienteService.getClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{rut}")
    public ResponseEntity<clienteEntity> getClienteByRut(@PathVariable String rut) {
        Optional<clienteEntity> cliente = clienteService.getClienteByRut(rut);
        if (cliente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente.get());
    }

    @PostMapping("/")
    public ResponseEntity<clienteEntity> saveCliente(@RequestBody clienteEntity cliente) {
        clienteEntity clienteNew = clienteService.saveCliente(cliente);
        return ResponseEntity.ok(clienteNew);
    }

    @PutMapping("/")
    public ResponseEntity<clienteEntity> updateCliente(@RequestBody clienteEntity cliente){
        clienteEntity clienteUpdated = clienteService.updateCliente(cliente);
        return ResponseEntity.ok(clienteUpdated);
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<Boolean> deleteClienteByRut(@PathVariable String rut) throws Exception {
        var isDeleted = clienteService.deleteCliente(rut);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}