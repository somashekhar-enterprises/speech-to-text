package com.speechtotext.core.dao;

import com.speechtotext.core.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Collection<Patient> findAllByTenantId(UUID tenant_id);
}
