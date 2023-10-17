package com.speechtotext.core.dto.response;

import java.util.List;

public class CategoriesResponse {

    private List<String> complaints;

    private List<String> diagnoses;

    private List<String> clinicalNotes;

    private List<String> investigations;

    private List<String> treatments;

    private List<String> billing;

    public List<String> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<String> complaints) {
        this.complaints = complaints;
    }

    public List<String> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(List<String> diagnoses) {
        this.diagnoses = diagnoses;
    }

    public List<String> getClinicalNotes() {
        return clinicalNotes;
    }

    public void setClinicalNotes(List<String> clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public List<String> getInvestigations() {
        return investigations;
    }

    public void setInvestigations(List<String> investigations) {
        this.investigations = investigations;
    }

    public List<String> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<String> treatments) {
        this.treatments = treatments;
    }

    public List<String> getBilling() {
        return billing;
    }

    public void setBilling(List<String> billing) {
        this.billing = billing;
    }
}
