package com.esiitech.monbondocteur.service;

import com.esiitech.monbondocteur.dto.AgendaDTO;
import com.esiitech.monbondocteur.mapper.AgendaMapper;
import com.esiitech.monbondocteur.model.Agenda;
import com.esiitech.monbondocteur.model.Role;
import com.esiitech.monbondocteur.model.Utilisateur;
import com.esiitech.monbondocteur.repository.AgendaRepository;
import com.esiitech.monbondocteur.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private AgendaMapper agendaMapper;

    public AgendaDTO ajouterDisponibilite(AgendaDTO agendaDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Utilisateur medecin = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!medecin.getRole().equals(Role.MEDECIN)) {
            throw new RuntimeException("Seuls les médecins peuvent ajouter des disponibilités !");
        }

        Agenda agenda = agendaMapper.toEntity(agendaDTO, medecin);
        agenda = agendaRepository.save(agenda);
        return agendaMapper.toDTO(agenda);
    }


    public List<AgendaDTO> getDisponibilitesParMedecin(Long medecinId) {
        return agendaRepository.findByMedecinId(medecinId)
                .stream()
                .map(agendaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AgendaDTO> getDisponibilitesParMedecinEtDate(Long medecinId, LocalDate date) {
        return agendaRepository.findByMedecinIdAndDate(medecinId, date)
                .stream()
                .map(agendaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AgendaDTO> getAllDisponibilites() {
        return agendaRepository.findAll()
                .stream()
                .map(agendaMapper::toDTO)
                .collect(Collectors.toList());
    }


}
