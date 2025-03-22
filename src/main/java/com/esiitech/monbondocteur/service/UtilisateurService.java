package com.esiitech.monbondocteur.service;

import com.esiitech.monbondocteur.dto.UtilisateurDTO;
import com.esiitech.monbondocteur.exception.UtilisateurNotFoundException;
import com.esiitech.monbondocteur.mapper.UtilisateurMapper;
import com.esiitech.monbondocteur.model.Role;
import com.esiitech.monbondocteur.model.Utilisateur;
import com.esiitech.monbondocteur.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, UtilisateurMapper utilisateurMapper, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
        this.passwordEncoder = passwordEncoder;
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
        validerRole(utilisateurDTO.getRole());
        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDTO);
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        return utilisateurMapper.toDto(utilisateurRepository.save(utilisateur));
    }

    public UtilisateurDTO update(UtilisateurDTO utilisateurDTO) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurDTO.getId())
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur avec l'ID " + utilisateurDTO.getId() + " non trouvé."));
        validerRole(utilisateurDTO.getRole());
        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setEmail(utilisateurDTO.getEmail());
        if (utilisateurDTO.getMotDePasse() != null && !utilisateurDTO.getMotDePasse().isEmpty()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateurDTO.getMotDePasse()));
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
}
