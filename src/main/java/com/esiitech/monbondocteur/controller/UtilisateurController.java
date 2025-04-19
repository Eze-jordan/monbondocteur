package com.esiitech.monbondocteur.controller;

import com.esiitech.monbondocteur.dto.AuthentificationDTO;
import com.esiitech.monbondocteur.dto.UtilisateurDTO;
import com.esiitech.monbondocteur.security.JwtService;
import com.esiitech.monbondocteur.service.UtilisateurService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/utilisateurs")
@Tag(name = "Utilisateurs", description = "Opérations de gestion des utilisateurs et authentification")
public class UtilisateurController {

    private static final Logger log = LoggerFactory.getLogger(UtilisateurController.class);

    private final AuthenticationManager authenticationManager;
    private final UtilisateurService utilisateurService;
    private final JwtService jwtService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService,
                                 AuthenticationManager authenticationManager,
                                 JwtService jwtService) {
        this.utilisateurService = utilisateurService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/activation")
    @Operation(summary = "Activer un compte utilisateur", description = "Active un utilisateur via un token ou un code d’activation")
    public void activation(@RequestBody Map<String, String> activation) {
        this.utilisateurService.activation(activation);
    }

    @PostMapping("/connexion")
    @Operation(summary = "Connexion", description = "Permet de se connecter et de récupérer un token JWT")
    public Map<String, String> connexion(@RequestBody AuthentificationDTO authentificationDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authentificationDTO.email(),
                        authentificationDTO.motDePasse()
                )
        );

        if (authentication.isAuthenticated()) {
            return this.jwtService.generate(authentificationDTO.email());
        }
        return null;
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les utilisateurs", description = "Retourne la liste de tous les utilisateurs")
    public ResponseEntity<List<UtilisateurDTO>> getAllUtilisateurs() {
        List<UtilisateurDTO> utilisateurs = utilisateurService.findAll();
        return ResponseEntity.ok(utilisateurs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un utilisateur par ID", description = "Retourne les infos d’un utilisateur spécifique")
    public ResponseEntity<UtilisateurDTO> getUtilisateurById(
            @Parameter(description = "ID de l'utilisateur", required = true) @PathVariable Long id) {
        UtilisateurDTO utilisateurDTO = utilisateurService.findById(id);
        if (utilisateurDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(utilisateurDTO);
    }

    @PostMapping
    @Operation(summary = "Créer un utilisateur", description = "Permet de créer un nouvel utilisateur")
    public ResponseEntity<UtilisateurDTO> createUtilisateur(@RequestBody UtilisateurDTO utilisateurDTO) {
        UtilisateurDTO createdUtilisateur = utilisateurService.save(utilisateurDTO);
        return ResponseEntity.status(201).body(createdUtilisateur);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un utilisateur", description = "Permet de modifier les infos d’un utilisateur existant")
    public ResponseEntity<UtilisateurDTO> updateUtilisateur(
            @Parameter(description = "ID de l'utilisateur à modifier", required = true) @PathVariable Long id,
            @RequestBody UtilisateurDTO utilisateurDTO) {
        utilisateurDTO.setId(id);
        UtilisateurDTO updatedUtilisateur = utilisateurService.update(utilisateurDTO);
        if (updatedUtilisateur == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUtilisateur);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un utilisateur", description = "Permet de supprimer un utilisateur par ID")
    public ResponseEntity<Void> deleteUtilisateur(
            @Parameter(description = "ID de l'utilisateur à supprimer", required = true) @PathVariable Long id) {
        if (utilisateurService.existsById(id)) {
            utilisateurService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
