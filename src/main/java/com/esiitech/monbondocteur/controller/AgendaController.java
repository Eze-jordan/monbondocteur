package com.esiitech.monbondocteur.controller;

import com.esiitech.monbondocteur.dto.AgendaDTO;
import com.esiitech.monbondocteur.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/agenda")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @PostMapping("/ajouter")
    public ResponseEntity<AgendaDTO> ajouterDisponibilite(@RequestBody AgendaDTO agendaDTO) {
        return ResponseEntity.ok(agendaService.ajouterDisponibilite(agendaDTO));
    }
    @GetMapping
    public ResponseEntity<List<AgendaDTO>> getAllAgendas() {
        return ResponseEntity.ok(agendaService.getAllAgendas());
    }

    @GetMapping("/medecin/{medecinId}")
    public ResponseEntity<List<AgendaDTO>> getDisponibilitesParMedecin(@PathVariable Long medecinId) {
        return ResponseEntity.ok(agendaService.getDisponibilitesParMedecin(medecinId));
    }

    @GetMapping("/medecin/{medecinId}/date")
    public ResponseEntity<List<AgendaDTO>> getDisponibilitesParMedecinEtDate(
            @PathVariable Long medecinId,
            @RequestParam LocalDate date) {
        return ResponseEntity.ok(agendaService.getDisponibilitesParMedecinEtDate(medecinId, date));
    }
}
