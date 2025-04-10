package com.esiitech.monbondocteur.service;

import com.esiitech.monbondocteur.dto.UtilisateurDTO;
import com.esiitech.monbondocteur.exception.UtilisateurNotFoundException;
import com.esiitech.monbondocteur.mapper.UtilisateurMapper;
import com.esiitech.monbondocteur.model.Role;
import com.esiitech.monbondocteur.model.Utilisateur;
import com.esiitech.monbondocteur.model.Validation;
import com.esiitech.monbondocteur.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilisateurService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;

    public UtilisateurService(
            UtilisateurRepository utilisateurRepository,
            UtilisateurMapper utilisateurMapper,
            PasswordEncoder passwordEncoder,
            ValidationService validationService
    ) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
        this.passwordEncoder = passwordEncoder;
        this.validationService = validationService;
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
        if (!utilisateurDTO.getEmail().contains("@") || !utilisateurDTO.getEmail().contains(".")) {
            throw new RuntimeException("Utilisateur mail non valide");
        }

        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findByEmail(utilisateurDTO.getEmail());
        if (utilisateurOptional.isPresent()) {
            throw new RuntimeException("Votre mail est déjà utilisé");
        }

        if (utilisateurDTO.getRole() == null) {
            utilisateurDTO.setRole(Role.USER);
        }

        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDTO);
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateur = utilisateurRepository.save(utilisateur);

        validationService.enregister(utilisateur);

        return utilisateurMapper.toDto(utilisateur);
    }

    public UtilisateurDTO update(UtilisateurDTO utilisateurDTO) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurDTO.getId())
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur avec l'ID " + utilisateurDTO.getId() + " non trouvé."));

        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setEmail(utilisateurDTO.getEmail());

        if (utilisateurDTO.getMotDePasse() != null && !utilisateurDTO.getMotDePasse().isEmpty()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateurDTO.getMotDePasse()));
        }

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
        Validation validation = validationService.lireEnFonctionDuCode(activation.get("code"));
        if (Instant.now().isAfter(validation.getExpiration())) {
            throw new RuntimeException("Votre code a expiré");
        }

        Utilisateur utilisateurActiver = utilisateurRepository.findById(validation.getUtilisateur().getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur inconnu"));

        utilisateurActiver.setActif(true);
        utilisateurRepository.save(utilisateurActiver);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        if (!utilisateur.isActif()) {
            throw new UsernameNotFoundException("Le compte n'est pas encore activé.");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(utilisateur.getEmail())
                .password(utilisateur.getMotDePasse())
                .roles(utilisateur.getRole().name())
                .build();
    }
}
