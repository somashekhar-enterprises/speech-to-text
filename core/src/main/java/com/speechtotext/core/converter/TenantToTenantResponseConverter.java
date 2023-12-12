package com.speechtotext.core.converter;

import com.speechtotext.core.domain.Tenant;
import com.speechtotext.core.dto.response.TenantResponse;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toSet;

@Component
public class TenantToTenantResponseConverter {

    @Inject
    private PatientToPatientResponseConverter patientToPatientResponseConverter;

    public TenantResponse convert(Tenant source) {
        TenantResponse response = new TenantResponse();
        response.setName(source.getName());
        response.setEmail(source.getEmail());
        response.setUsername(source.getUsername());
        response.setPhone(source.getPhone());
        response.setPatients(source.getPatients()
                .stream().map(patientToPatientResponseConverter::convert).collect(toSet()));
        return response;
    }
}
