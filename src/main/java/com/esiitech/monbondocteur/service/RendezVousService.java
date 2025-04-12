package com.esiitech.monbondocteur.service;

import com.esiitech.monbondocteur.dto.RendezVousDTO;
import com.esiitech.monbondocteur.mapper.RendezVousMapper;
import com.esiitech.monbondocteur.model.RendezVous;
import com.esiitech.monbondocteur.model.Role;
import com.esiitech.monbondocteur.model.Utilisateur;
import com.esiitech.monbondocteur.repository.RendezVousRepository;
import com.esiitech.monbondocteur.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RendezVousService {

    @Autowired
    private RendezVousRepository rendezVousRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RendezVousMapper rendezVousMapper;

    @Autowired
    private NotificationService notificationService;


    public RendezVous ajouterRendezVous(RendezVousDTO dto) {
        Utilisateur medecin = utilisateurRepository.findById(dto.getMedecinId())
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));

        // Vérifier si l'utilisateur a bien le rôle MEDECIN
        if (!medecin.getRole().equals(Role.MEDECIN)) {
            throw new RuntimeException("L'utilisateur sélectionné n'est pas un médecin.");
        }
        notificationService.envoyerAuPatient(dto.getEmail(), dto.getPrenom(), medecin.getNom());
        notificationService.envoyerAuMedecin(medecin.getEmail(), medecin.getNom(), dto.getPrenom());



        RendezVous rendezVous = rendezVousMapper.toEntity(dto, medecin);
        return rendezVousRepository.save(rendezVous);
    }

    public List<RendezVous> getRendezVousByMedecin(Long medecinId) {
        return rendezVousRepository.findByMedecinId(medecinId);
    }

    // ✅ Nouvelle méthode pour récupérer tous les rendez-vous
    public List<RendezVous> getAllRendezVous() {
        return rendezVousRepository.findAll();
    }

    public void supprimerRendezVous(Long rendezVousId) {
        RendezVous rendezVous = rendezVousRepository.findById(rendezVousId)
                .orElseThrow(() -> new RuntimeException("Rendez-vous non trouvé"));

        rendezVousRepository.delete(rendezVous);
    }


}
