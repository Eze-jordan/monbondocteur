package com.esiitech.monbondocteur.controller;

import com.esiitech.monbondocteur.dto.RendezVousDTO;
import com.esiitech.monbondocteur.model.RendezVous;
import com.esiitech.monbondocteur.service.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rendezvous")
public class RendezVousController {

    @Autowired
    private RendezVousService rendezVousService;

    @PostMapping
    public ResponseEntity<RendezVous> ajouterRendezVous(@RequestBody RendezVousDTO dto) {
        RendezVous rendezVous = rendezVousService.ajouterRendezVous(dto);
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/medecin/{medecinId}")
    public ResponseEntity<List<RendezVous>> getRendezVousByMedecin(@PathVariable Long medecinId) {
        List<RendezVous> rendezVousList = rendezVousService.getRendezVousByMedecin(medecinId);
        return ResponseEntity.ok(rendezVousList);
    }

    // ✅ Nouvel endpoint pour récupérer tous les rendez-vous
    @GetMapping
    public ResponseEntity<List<RendezVous>> getAllRendezVous() {
        List<RendezVous> rendezVousList = rendezVousService.getAllRendezVous();
        return ResponseEntity.ok(rendezVousList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerRendezVous(@PathVariable Long id) {
        rendezVousService.supprimerRendezVous(id);
        return ResponseEntity.ok("Rendez-vous supprimé avec succès");
    }

}
