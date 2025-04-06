package karting.controllers;

import karting.entities.comprobanteEntity;
import karting.services.comprobanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comprobantes")
@CrossOrigin("*")
public class comprobanteController {
    @Autowired
    comprobanteService comprobanteService;

    @GetMapping("/")
    public ResponseEntity<List<comprobanteEntity>> listComprobantes() {
        List<comprobanteEntity> comprobantes = comprobanteService.getComprobantes();
        return ResponseEntity.ok(comprobantes);
    }

    @GetMapping("/{idComprobante}")
    public ResponseEntity<comprobanteEntity> getComprobanteByIdComprobante(@PathVariable Long idComprobante) {
        comprobanteEntity comprobante = comprobanteService.getComprobanteByIdComprobante(idComprobante);
        return ResponseEntity.ok(comprobante);
    }

    @PostMapping("/")
    public ResponseEntity<comprobanteEntity> saveComprobante(@RequestBody comprobanteEntity comprobante) {
        comprobanteEntity comprobanteNew = comprobanteService.saveComprobante(comprobante);
        return ResponseEntity.ok(comprobanteNew);
    }

    @PutMapping("/")
    public ResponseEntity<comprobanteEntity> updateComprobante(@RequestBody comprobanteEntity comprobante){
        comprobanteEntity comprobanteUpdated = comprobanteService.updateComprobante(comprobante);
        return ResponseEntity.ok(comprobanteUpdated);
    }

    @DeleteMapping("/{idComprobante}")
    public ResponseEntity<Boolean> deleteComprobanteByIdComprobante(@PathVariable Long idComprobante) throws Exception {
        var isDeleted = comprobanteService.deleteComprobante(idComprobante);
        return ResponseEntity.noContent().build();
    }
}