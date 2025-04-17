package com.esiitech.monbondocteur.mapper;

import com.esiitech.monbondocteur.dto.AgendaDTO;
import com.esiitech.monbondocteur.model.Agenda;
import com.esiitech.monbondocteur.model.Utilisateur;
import org.springframework.stereotype.Component;

@Component
public class AgendaMapper {

    public Agenda toEntity(AgendaDTO agendaDTO, Utilisateur medecin) {
        Agenda agenda = new Agenda();
        agenda.setMedecin(medecin);
        agenda.setDate(agendaDTO.getDate());
        agenda.setHeureDebut(agendaDTO.getHeureDebut());
        agenda.setHeureFin(agendaDTO.getHeureFin());
        return agenda;
    }

    public AgendaDTO toDTO(Agenda agenda) {
        AgendaDTO dto = new AgendaDTO();
        dto.setDate(agenda.getDate());
        dto.setHeureDebut(agenda.getHeureDebut());
        dto.setHeureFin(agenda.getHeureFin());
        return dto;
    }

}
