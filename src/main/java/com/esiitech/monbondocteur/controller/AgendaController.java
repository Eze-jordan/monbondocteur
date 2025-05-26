package com.esiitech.monbondocteur.controller;

import com.esiitech.monbondocteur.dto.AgendaDTO;
import com.esiitech.monbondocteur.service.AgendaService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/agenda")
@Tag(name = "Agenda", description = "Gestion des disponibilités des médecins")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;
    @PostMapping("/ajouter")
    @Operation(summary = "Ajouter une disponibilité", description = "Ajoute une nouvelle disponibilité pour un médecin donné.")
    public ResponseEntity<AgendaDTO> ajouterDisponibilite(@RequestBody AgendaDTO agendaDTO, Principal principal) {
        return ResponseEntity.ok(agendaService.ajouterDisponibilite(agendaDTO, principal.getName()));
    }

    @GetMapping("/medecin/{medecinId}")
    @Operation(summary = "Récupérer les disponibilités d’un médecin", description = "Liste toutes les disponibilités d’un médecin spécifique.")
    public ResponseEntity<List<AgendaDTO>> getDisponibilitesParMedecin(
            @Parameter(description = "ID du médecin", required = true) @PathVariable Long medecinId) {
        return ResponseEntity.ok(agendaService.getDisponibilitesParMedecin(medecinId));
    }

    @GetMapping("/medecin/{medecinId}/date")
    @Operation(summary = "Récupérer les disponibilités par médecin et date", description = "Filtre les disponibilités d’un médecin selon une date précise.")
    public ResponseEntity<List<AgendaDTO>> getDisponibilitesParMedecinEtDate(
            @Parameter(description = "ID du médecin", required = true) @PathVariable Long medecinId,
            @Parameter(description = "Date recherchée au format yyyy-MM-dd", required = true) @RequestParam LocalDate date) {
        return ResponseEntity.ok(agendaService.getDisponibilitesParMedecinEtDate(medecinId, date));
    }

    // Nouvelle méthode pour récupérer toutes les disponibilités
    @GetMapping("/toutes")
    @Operation(summary = "Récupérer toutes les disponibilités", description = "Liste toutes les disponibilités de tous les médecins.")
    public ResponseEntity<List<AgendaDTO>> getAllDisponibilites() {
        return ResponseEntity.ok(agendaService.getAllDisponibilites());
    }

}
