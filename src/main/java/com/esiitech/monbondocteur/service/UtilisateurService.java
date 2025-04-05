package com.esiitech.monbondocteur.service;

import com.esiitech.monbondocteur.dto.UtilisateurDTO;
import com.esiitech.monbondocteur.exception.UtilisateurNotFoundException;
import com.esiitech.monbondocteur.mapper.UtilisateurMapper;
import com.esiitech.monbondocteur.model.Role;
import com.esiitech.monbondocteur.model.Utilisateur;
import com.esiitech.monbondocteur.model.Validation;
import com.esiitech.monbondocteur.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;  // Dépendance ValidationService

    // Ajouter ValidationService dans le constructeur
    public UtilisateurService(UtilisateurRepository utilisateurRepository, UtilisateurMapper utilisateurMapper, PasswordEncoder passwordEncoder, ValidationService validationService) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
        this.passwordEncoder = passwordEncoder;
        this.validationService = validationService;  // Injection de ValidationService
    }

    public List<UtilisateurDTO> findAll() {
        return utilisateurRepository.findAll()
                .stream()
                .map(utilisateurMapper::toDto)
                .collect(Collectors.toList());
    }

    public UtilisateurDTO findById(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur avec l'ID " + id + " non trouvé."));
        return utilisateurMapper.toDto(utilisateur);
    }

    public UtilisateurDTO save(UtilisateurDTO utilisateurDTO) {
        // Validation de l'email
        if (!utilisateurDTO.getEmail().contains("@") || !utilisateurDTO.getEmail().contains(".")) {
            throw new RuntimeException("Utilisateur mail non valide");
        }

        // Vérification si l'email est déjà utilisé
        Optional<Utilisateur> utilisateurOptional = this.utilisateurRepository.findByEmail(utilisateurDTO.getEmail());
        if (utilisateurOptional.isPresent()) {
            throw new RuntimeException("Votre mail est déjà utilisé");
        }

        // ✅ Rôle par défaut si non fourni
        if (utilisateurDTO.getRole() == null) {
            utilisateurDTO.setRole(Role.USER); // Attribuer le rôle USER par défaut
        }

        // Conversion de UtilisateurDTO en Utilisateur
        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDTO);

        // Encodage du mot de passe
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));

        // Sauvegarde de l'utilisateur dans la base de données
        utilisateur = this.utilisateurRepository.save(utilisateur);

        // Enregistrement de la validation (par exemple, code de validation pour l'utilisateur)
        this.validationService.enregister(utilisateur);

        // Retourne l'UtilisateurDTO après l'enregistrement
        return utilisateurMapper.toDto(utilisateur);
    }

    public UtilisateurDTO update(UtilisateurDTO utilisateurDTO) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurDTO.getId())
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur avec l'ID " + utilisateurDTO.getId() + " non trouvé."));

        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setEmail(utilisateurDTO.getEmail());

        // Mise à jour éventuelle du mot de passe
        if (utilisateurDTO.getMotDePasse() != null && !utilisateurDTO.getMotDePasse().isEmpty()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateurDTO.getMotDePasse()));
        }

        // ✅ Si tu veux permettre la mise à jour du rôle aussi (facultatif)
        if (utilisateurDTO.getRole() != null) {
            utilisateur.setRole(utilisateurDTO.getRole());
        }

        return utilisateurMapper.toDto(utilisateurRepository.save(utilisateur));
    }

    public void delete(Long id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new UtilisateurNotFoundException("Utilisateur avec l'ID " + id + " non trouvé.");
        }
        utilisateurRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return utilisateurRepository.existsById(id);
    }

    private void validerRole(String role) {
        try {
            Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Le rôle '" + role + "' est invalide.");
        }
    }

    public void activation(Map<String, String> activation) {
        Validation validation = this.validationService.lireEnFonctionDuCode(activation.get("code"));
        if (Instant.now().isAfter(validation.getExpiration())){
            throw new RuntimeException(" Votre code a expiré ");
        }
       Utilisateur utilisateurActiver = this.utilisateurRepository.findById(validation.getUtilisateur().getId()).orElseThrow(()
               -> new RuntimeException("Utilisateur inconnu"));
       utilisateurActiver.setActif(true);
       this.utilisateurRepository.save(utilisateurActiver);
    }
}
