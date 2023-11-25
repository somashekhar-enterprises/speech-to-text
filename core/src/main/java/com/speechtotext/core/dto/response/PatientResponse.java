package com.speechtotext.core.dto.response;

import java.util.List;

public class PatientResponse {

    private String firstName;

    private String lastName;

    private PatientAttributeResponse attributes;

    private TenantResponse tenant;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public PatientAttributeResponse getAttributes() {
        return attributes;
    }

    public void setAttributes(PatientAttributeResponse attributes) {
        this.attributes = attributes;
    }

    public TenantResponse getTenant() {
        return tenant;
    }

    public void setTenant(TenantResponse tenant) {
        this.tenant = tenant;
    }
}
