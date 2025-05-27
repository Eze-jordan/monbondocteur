package com.esiitech.monbondocteur.mapper;

import com.esiitech.monbondocteur.dto.RendezVousDTO;
import com.esiitech.monbondocteur.model.Agenda;
import com.esiitech.monbondocteur.model.RendezVous;
import com.esiitech.monbondocteur.model.Utilisateur;
import org.springframework.stereotype.Component;
@Component
public class RendezVousMapper {

    public RendezVous toEntity(RendezVousDTO dto, Utilisateur medecin, Agenda agenda) {
        RendezVous rendezVous = new RendezVous();
        rendezVous.setNomComplet(dto.getNomComplet());
        rendezVous.setPrenom(dto.getPrenom());
        rendezVous.setAge(dto.getAge());
        rendezVous.setSexe(dto.getSexe());
        rendezVous.setEmail(dto.getEmail());
        rendezVous.setNumeroTelephone(dto.getNumeroTelephone());
        rendezVous.setDescription(dto.getDescription());
        rendezVous.setQuartier(dto.getQuartier());
        rendezVous.setMedecin(medecin);
        rendezVous.setAgenda(agenda);
        return rendezVous;
    }

    public RendezVousDTO toDTO(RendezVous rendezVous) {
        RendezVousDTO dto = new RendezVousDTO();
        dto.setNomComplet(rendezVous.getNomComplet());
        dto.setPrenom(rendezVous.getPrenom());
        dto.setAge(rendezVous.getAge());
        dto.setSexe(rendezVous.getSexe());
        dto.setEmail(rendezVous.getEmail());
        dto.setNumeroTelephone(rendezVous.getNumeroTelephone());
        dto.setDescription(rendezVous.getDescription());
        dto.setQuartier(rendezVous.getQuartier());
        dto.setMedecinId(rendezVous.getMedecin().getId());
        dto.setAgendaId(rendezVous.getAgenda().getId());
        return dto;
    }
}
