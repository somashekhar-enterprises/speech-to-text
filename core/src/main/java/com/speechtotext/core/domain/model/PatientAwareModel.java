package com.speechtotext.core.domain.model;

import com.speechtotext.core.domain.Patient;

public interface PatientAwareModel {

    Patient getPatient();
}
