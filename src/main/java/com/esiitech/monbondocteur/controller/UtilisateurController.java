package com.esiitech.monbondocteur.controller;
import com.esiitech.monbondocteur.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.esiitech.monbondocteur.dto.AuthentificationDTO;
import com.esiitech.monbondocteur.dto.UtilisateurDTO;
import com.esiitech.monbondocteur.service.UtilisateurService;
import lombok.extern.slf4j.Slf4j;
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
public class UtilisateurController {
    private static final Logger log = LoggerFactory.getLogger(UtilisateurController.class);

    private final AuthenticationManager authenticationManager;
    private final UtilisateurService utilisateurService;
    private JwtService jwtService;
    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService, AuthenticationManager authenticationManager , JwtService jwtService) {
        this.utilisateurService = utilisateurService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/activation")
    public void activation(@RequestBody Map<String, String> activation) {
        this.utilisateurService.activation(activation);
    }

    @PostMapping("/connexion")
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
    public ResponseEntity<List<UtilisateurDTO>> getAllUtilisateurs() {
        List<UtilisateurDTO> utilisateurs = utilisateurService.findAll();
        return ResponseEntity.ok(utilisateurs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getUtilisateurById(@PathVariable Long id) {
        UtilisateurDTO utilisateurDTO = utilisateurService.findById(id);
        if (utilisateurDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(utilisateurDTO);
    }

    @PostMapping
    public ResponseEntity<UtilisateurDTO> createUtilisateur(@RequestBody UtilisateurDTO utilisateurDTO) {
        UtilisateurDTO createdUtilisateur = utilisateurService.save(utilisateurDTO);
        return ResponseEntity.status(201).body(createdUtilisateur);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> updateUtilisateur(@PathVariable Long id, @RequestBody UtilisateurDTO utilisateurDTO) {
        utilisateurDTO.setId(id);
        UtilisateurDTO updatedUtilisateur = utilisateurService.update(utilisateurDTO);
        if (updatedUtilisateur == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUtilisateur);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        if (utilisateurService.existsById(id)) {
            utilisateurService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
