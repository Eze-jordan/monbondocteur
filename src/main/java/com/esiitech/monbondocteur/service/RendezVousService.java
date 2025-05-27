package com.esiitech.monbondocteur.service;

import com.esiitech.monbondocteur.dto.RendezVousDTO;
import com.esiitech.monbondocteur.mapper.RendezVousMapper;
import com.esiitech.monbondocteur.model.Agenda;
import com.esiitech.monbondocteur.model.RendezVous;
import com.esiitech.monbondocteur.model.Role;
import com.esiitech.monbondocteur.model.Utilisateur;
import com.esiitech.monbondocteur.repository.AgendaRepository;
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
    @Autowired
    private AgendaRepository agendaRepository;




    public RendezVous ajouterRendezVous(RendezVousDTO dto) {
        // ✅ Vérification de la présence des IDs
        if (dto.getMedecinId() == null) {
            throw new IllegalArgumentException("L'ID du médecin ne doit pas être null.");
        }
        if (dto.getAgendaId() == null) {
            throw new IllegalArgumentException("L'ID de l'agenda ne doit pas être null.");
        }

        // 🔍 Vérification de l'existence du médecin
        Utilisateur medecin = utilisateurRepository.findById(dto.getMedecinId())
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));

        // ✅ Vérification si l'utilisateur est un médecin
        if (!medecin.getRole().equals(Role.MEDECIN)) {
            throw new RuntimeException("L'utilisateur sélectionné n'est pas un médecin.");
        }

        // 🔍 Récupération de l'agenda correspondant
        Agenda agenda = agendaRepository.findById(dto.getAgendaId())
                .orElseThrow(() -> new RuntimeException("Agenda non trouvé"));

        // 📧 Envoi des notifications aux parties concernées
        try {
            notificationService.envoyerAuPatient(dto.getEmail(), dto.getPrenom(), medecin.getNom());
            notificationService.envoyerAuMedecin(medecin.getEmail(), medecin.getNom(), dto.getPrenom());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi des notifications : " + e.getMessage(), e);
        }

        // 💉 Mapper le DTO en entité RendezVous
        RendezVous rendezVous = rendezVousMapper.toEntity(dto, medecin, agenda);

        // 🔄 Sauvegarde du rendez-vous
        return rendezVousRepository.save(rendezVous);
    }


    public List<RendezVous> getRendezVousByMedecin(Long medecinId) {
        return rendezVousRepository.findByMedecinId(medecinId);
    }

    public RendezVous modifierRendezVous(Long id, RendezVousDTO dto) {
        // 🔍 Vérifie si le rendez-vous existe
        RendezVous existant = rendezVousRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rendez-vous non trouvé"));

        // 🔄 Met à jour les champs
        existant.setNomComplet(dto.getNomComplet());
        existant.setPrenom(dto.getPrenom());
        existant.setAge(dto.getAge());
        existant.setSexe(dto.getSexe());
        existant.setEmail(dto.getEmail());
        existant.setNumeroTelephone(dto.getNumeroTelephone());
        existant.setDescription(dto.getDescription());
        existant.setQuartier(dto.getQuartier());

        // 🔄 Met à jour le médecin si l'ID a changé
        if (dto.getMedecinId() != null && !dto.getMedecinId().equals(existant.getMedecin().getId())) {
            Utilisateur nouveauMedecin = utilisateurRepository.findById(dto.getMedecinId())
                    .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));
            if (!nouveauMedecin.getRole().equals(Role.MEDECIN)) {
                throw new RuntimeException("L'utilisateur sélectionné n'est pas un médecin.");
            }
            existant.setMedecin(nouveauMedecin);
        }

        // 🔄 Met à jour l’agenda si besoin
        if (dto.getAgendaId() != null && !dto.getAgendaId().equals(existant.getAgenda().getId())) {
            Agenda nouvelAgenda = agendaRepository.findById(dto.getAgendaId())
                    .orElseThrow(() -> new RuntimeException("Agenda non trouvé"));
            existant.setAgenda(nouvelAgenda);
        }

        // 🧠 Si tu veux, tu peux renvoyer une notification de modification ici

        // ✅ Sauvegarde
        return rendezVousRepository.save(existant);
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
