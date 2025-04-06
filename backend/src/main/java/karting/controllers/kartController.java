package karting.controllers;

import karting.entities.kartEntity;
import karting.repositories.kartRepository;
import karting.services.kartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/karts")
@CrossOrigin("*")
public class kartController {
    @Autowired
    kartService kartService;

    @GetMapping("/")
    public List<kartEntity> listKarts() {
        return kartService.getKarts();
    }

    @GetMapping("/{idKart}")
    public kartEntity getKartById(@PathVariable int idKart) {
        return kartService.getKartById(idKart);
    }

    @PostMapping("/")
    public kartEntity saveKart(@RequestBody kartEntity kart) {
        return kartService.saveKart(kart);
    }

    @PutMapping("/")
    public kartEntity updateKart(@RequestBody kartEntity kart){
        return kartService.updateKart(kart);
    }

    @DeleteMapping("/{idKart}")
    public boolean deleteKartById(@PathVariable int idKart) throws Exception {
        return kartService.deleteKart(idKart);
    }
}