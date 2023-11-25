package com.speechtotext.core.service;

import com.speechtotext.core.dao.PatientRepository;
import com.speechtotext.core.domain.Patient;
import com.speechtotext.core.dto.request.PatientRequest;
import jakarta.inject.Inject;
import org.springframework.stereotype.Service;

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
        patient.setTenant(tenantService.findById(threadLocalService.getTenantID()));
        return patientRepository.save(patient);

    }
}
