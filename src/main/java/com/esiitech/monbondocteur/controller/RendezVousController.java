package com.esiitech.monbondocteur.controller;

import com.esiitech.monbondocteur.dto.RendezVousDTO;
import com.esiitech.monbondocteur.mapper.RendezVousMapper;
import com.esiitech.monbondocteur.model.RendezVous;
import com.esiitech.monbondocteur.service.RendezVousService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/appointment")
@Tag(name = "Rendez-vous", description = "Gestion des rendez-vous médicaux")
public class RendezVousController {

    @Autowired
    private RendezVousService rendezVousService;

    private final RendezVousMapper rendezVousMapper; // ✅ ajoute ça

    public RendezVousController(RendezVousMapper rendezVousMapper) {
        this.rendezVousMapper = rendezVousMapper;
    }


    @PostMapping
    @Operation(summary = "Ajouter un rendez-vous", description = "Permet d'enregistrer un nouveau rendez-vous avec les informations du patient et du médecin.")
    public ResponseEntity<RendezVous> ajouterRendezVous(@RequestBody RendezVousDTO dto) {
        RendezVous rendezVous = rendezVousService.ajouterRendezVous(dto);
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/medecin/{medecinId}")
    @Operation(summary = "Lister les rendez-vous d’un médecin", description = "Récupère la liste des rendez-vous pour un médecin donné.")
    public ResponseEntity<List<RendezVous>> getRendezVousByMedecin(
            @Parameter(description = "ID du médecin", required = true) @PathVariable Long medecinId) {
        List<RendezVous> rendezVousList = rendezVousService.getRendezVousByMedecin(medecinId);
        return ResponseEntity.ok(rendezVousList);
    }

    @GetMapping
    @Operation(summary = "Lister tous les rendez-vous", description = "Récupère tous les rendez-vous enregistrés.")
    public ResponseEntity<List<RendezVous>> getAllRendezVous() {
        List<RendezVous> rendezVousList = rendezVousService.getAllRendezVous();
        return ResponseEntity.ok(rendezVousList);
    }
    @PutMapping("/{id}")
    public ResponseEntity<RendezVousDTO> modifierRendezVous(@PathVariable Long id, @RequestBody RendezVousDTO dto) {
        RendezVous modifie = rendezVousService.modifierRendezVous(id, dto);
        return ResponseEntity.ok(rendezVousMapper.toDTO(modifie));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un rendez-vous", description = "Supprime un rendez-vous à partir de son identifiant.")
    public ResponseEntity<String> supprimerRendezVous(
            @Parameter(description = "ID du rendez-vous", required = true) @PathVariable Long id) {
        rendezVousService.supprimerRendezVous(id);
        return ResponseEntity.ok("Rendez-vous supprimé avec succès");
    }
}
