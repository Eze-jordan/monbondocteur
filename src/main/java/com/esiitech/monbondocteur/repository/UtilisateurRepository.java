package com.esiitech.monbondocteur.repository;

import com.esiitech.monbondocteur.model.Role;
import com.esiitech.monbondocteur.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
 Optional<Utilisateur> findByEmail(String email);
 List<Utilisateur> findByRole(Role role);



}
