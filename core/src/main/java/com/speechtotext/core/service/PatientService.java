package com.speechtotext.core.service;

import com.speechtotext.core.dao.PatientRepository;
import com.speechtotext.core.domain.Patient;
import com.speechtotext.core.dto.request.PatientRequest;
import jakarta.inject.Inject;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Service
public class PatientService {

    @Inject
    private PatientRepository patientRepository;

    @Inject
    private ThreadLocalService threadLocalService;

    @Inject
    private TenantService tenantService;

    public Patient register(PatientRequest request) {
        Patient patient = new Patient();
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setCreated(Date.from(Instant.now()));
        patient.setTenant(tenantService.findById(threadLocalService.getTenantID()));
        return patientRepository.save(patient);

    }

    public Patient setSummary(String patientId, String summary) {
        Patient patient = patientRepository.findById(UUID.fromString(patientId)).orElseThrow(RuntimeException::new);
        patient.setSummary(summary);
        return patientRepository.save(patient);
    }

    public Collection<Patient> getAllForTenant() {
        return patientRepository.findAllByTenantId(UUID.fromString(threadLocalService.getTenantID()));
    }

    public Patient findById(String id) {
        return patientRepository.findById(UUID.fromString(id)).orElseThrow(RuntimeException::new);
    }

    public void deletePatient(String id) {
        patientRepository.deleteById(UUID.fromString(id));
    }
}
