package com.esiitech.monbondocteur.repository;

import com.esiitech.monbondocteur.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByMedecinId(Long medecinId);
    List<Agenda> findByMedecinIdAndDate(Long medecinId, LocalDate date);
}
