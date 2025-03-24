package com.esiitech.monbondocteur.mapper;

import com.esiitech.monbondocteur.dto.RendezVousDTO;
import com.esiitech.monbondocteur.model.RendezVous;
import com.esiitech.monbondocteur.model.Utilisateur;
import org.springframework.stereotype.Component;

@Component
public class RendezVousMapper {

    public RendezVous toEntity(RendezVousDTO dto, Utilisateur medecin) {
        RendezVous rendezVous = new RendezVous();
        rendezVous.setNomComplet(dto.getNomComplet());
        rendezVous.setPrenom(dto.getPrenom());
        rendezVous.setAge(dto.getAge());
        rendezVous.setSexe(dto.getSexe());
        rendezVous.setNumeroTelephone(dto.getNumeroTelephone());
        rendezVous.setDescription(dto.getDescription());
        rendezVous.setQuartier(dto.getQuartier());
        rendezVous.setMedecin(medecin);
        return rendezVous;
    }
}
