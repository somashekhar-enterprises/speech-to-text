package com.speechtotext.core.converter;

import com.speechtotext.core.domain.Patient;
import com.speechtotext.core.dto.response.PatientAttributeResponse;
import com.speechtotext.core.dto.response.PatientResponse;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class PatientToPatientResponseConverter {

    @Inject
    private TenantToTenantResponseConverter tenantResponseConverter;

    public PatientResponse convert(Patient source) {
        PatientResponse response = new PatientResponse();
        response.setFirstName(source.getFirstName());
        response.setLastName(source.getLastName());
        PatientAttributeResponse attributeResponse = new PatientAttributeResponse();
        attributeResponse.setComplaints(source.getAttributes().getComplaints());
        attributeResponse.setInvestigation(source.getAttributes().getInvestigation());
        attributeResponse.setDiagnosis(source.getAttributes().getDiagnosis());
        attributeResponse.setClinicalNotes(source.getAttributes().getClinicalNotes());
        attributeResponse.setTreatment(source.getAttributes().getTreatment());
        response.setAttributes(attributeResponse);
        response.setTenant(tenantResponseConverter.convert(source.getTenant()));
        return response;
    }
}
