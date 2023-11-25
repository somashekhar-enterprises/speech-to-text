package com.speechtotext.core.dto.response;

import java.util.List;

public class PatientAttributeResponse {

    private List<String> complaints;

    private List<String> investigation;

    private List<String> diagnosis;

    private List<String> clinicalNotes;

    private List<String> treatment;

    public List<String> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<String> complaints) {
        this.complaints = complaints;
    }

    public List<String> getInvestigation() {
        return investigation;
    }

    public void setInvestigation(List<String> investigation) {
        this.investigation = investigation;
    }

    public List<String> getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(List<String> diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<String> getClinicalNotes() {
        return clinicalNotes;
    }

    public void setClinicalNotes(List<String> clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public List<String> getTreatment() {
        return treatment;
    }

    public void setTreatment(List<String> treatment) {
        this.treatment = treatment;
    }
}
