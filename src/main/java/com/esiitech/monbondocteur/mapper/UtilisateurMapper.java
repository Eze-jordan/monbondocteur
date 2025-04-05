package com.esiitech.monbondocteur.mapper;

import com.esiitech.monbondocteur.dto.UtilisateurDTO;
import com.esiitech.monbondocteur.model.Role;
import com.esiitech.monbondocteur.model.Utilisateur;
import org.springframework.stereotype.Component;

@Component
public class UtilisateurMapper {

    public UtilisateurDTO toDto(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setId(utilisateur.getId());
        utilisateurDTO.setNom(utilisateur.getNom());
        utilisateurDTO.setPrenom(utilisateur.getPrenom());
        utilisateurDTO.setEmail(utilisateur.getEmail());
        utilisateurDTO.setMotDePasse(utilisateur.getMotDePasse());
        utilisateurDTO.setRole(utilisateur.getRole());
        return utilisateurDTO;
    }

    public Utilisateur toEntity(UtilisateurDTO utilisateurDTO) {
        if (utilisateurDTO == null) {
            return null;
        }
        // Utilisation du constructeur avec les paramètres de la classe Utilisateur
        Utilisateur utilisateur = new Utilisateur(
                utilisateurDTO.getId(),
                utilisateurDTO.getNom(),
                utilisateurDTO.getPrenom(),
                utilisateurDTO.getEmail(),
                utilisateurDTO.getMotDePasse(),
                utilisateurDTO.getRole() // Conversion String -> Enum
        );
        return utilisateur;
    }
}
