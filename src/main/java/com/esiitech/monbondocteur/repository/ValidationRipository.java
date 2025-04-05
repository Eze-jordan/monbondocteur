package com.esiitech.monbondocteur.repository;

import com.esiitech.monbondocteur.model.Validation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidationRipository extends JpaRepository<Validation, Long> {

    Optional<Validation> findByCode(String code);
}

