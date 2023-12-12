package com.speechtotext.core.converter;

import com.speechtotext.core.domain.Patient;
import com.speechtotext.core.dto.response.PatientAttributeResponse;
import com.speechtotext.core.dto.response.PatientResponse;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toSet;

@Component
public class PatientToPatientResponseConverter {

    public PatientResponse convert(Patient source) {
        PatientResponse response = new PatientResponse();
        response.setFirstName(source.getFirstName());
        response.setLastName(source.getLastName());
        populateAttributesIfNonNull(source, response);
        response.setTenant(source.getTenant().getUsername());
        response.setPatientId(source.getId());
        response.setSummary(source.getSummary());
        return response;
    }

    private static void populateAttributesIfNonNull(Patient source, PatientResponse response) {
        PatientAttributeResponse attributeResponse = new PatientAttributeResponse();
        if (nonNull(source.getAttributes())) {
            attributeResponse.setComplaints(source.getAttributes().getComplaints());
            attributeResponse.setDiagnosis(source.getAttributes().getDiagnosis());
            attributeResponse.setClinicalNotes(source.getAttributes().getClinicalNotes());
        }
        response.setAttributes(attributeResponse);
    }

    public Set<PatientResponse> convert(Collection<Patient> source) {
        return source.stream().map(this::convert).collect(toSet());
    }
}
