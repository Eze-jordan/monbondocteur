package com.esiitech.monbondocteur.repository;

import com.esiitech.monbondocteur.model.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    List<RendezVous> findByMedecinId(Long medecinId);
}
