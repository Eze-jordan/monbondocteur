package com.esiitech.monbondocteur.controller;

import com.esiitech.monbondocteur.dto.UtilisateurDTO;
import com.esiitech.monbondocteur.model.Utilisateur;
import com.esiitech.monbondocteur.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }
    @PostMapping("/activation")
    public void activation(@RequestBody Map<String, String> activation){

       this.utilisateurService.activation(activation);
    }

    // Récupère tous les utilisateurs
    @GetMapping
    public ResponseEntity<List<UtilisateurDTO>> getAllUtilisateurs() {
        List<UtilisateurDTO> utilisateurs = utilisateurService.findAll();
        return ResponseEntity.ok(utilisateurs);
    }

    // Récupère un utilisateur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getUtilisateurById(@PathVariable Long id) {
        UtilisateurDTO utilisateurDTO = utilisateurService.findById(id);
        if (utilisateurDTO == null) {
            return ResponseEntity.notFound().build(); // Utilisateur non trouvé
        }
        return ResponseEntity.ok(utilisateurDTO);
    }

    // Crée un nouvel utilisateur
    @PostMapping
    public ResponseEntity<UtilisateurDTO> createUtilisateur(@RequestBody UtilisateurDTO utilisateurDTO) {
        UtilisateurDTO createdUtilisateur = utilisateurService.save(utilisateurDTO);
        return ResponseEntity.status(201).body(createdUtilisateur); // Status 201 : Créé
    }

    // Met à jour un utilisateur existant
    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> updateUtilisateur(@PathVariable Long id, @RequestBody UtilisateurDTO utilisateurDTO) {
        utilisateurDTO.setId(id); // On s'assure que l'ID du DTO est le bon
        UtilisateurDTO updatedUtilisateur = utilisateurService.update(utilisateurDTO);
        if (updatedUtilisateur == null) {
            return ResponseEntity.notFound().build(); // Utilisateur non trouvé pour mise à jour
        }
        return ResponseEntity.ok(updatedUtilisateur); // Retourne l'utilisateur mis à jour
    }

    // Supprime un utilisateur par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        if (utilisateurService.existsById(id)) {
            utilisateurService.delete(id);
            return ResponseEntity.noContent().build(); // Status 204 : Suppression réussie
        }
        return ResponseEntity.notFound().build(); // Utilisateur non trouvé
    }
}
