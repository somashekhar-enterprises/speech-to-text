package com.speechtotext.core.dto.response;

import java.util.Set;

public class TenantResponse {
    private String name;

    private String email;

    private String username;

    private String phone;

    private Set<PatientResponse> patients;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<PatientResponse> getPatients() {
        return patients;
    }

    public void setPatients(Set<PatientResponse> patients) {
        this.patients = patients;
    }
}
