package com.speechtotext.core.controller;

import com.speechtotext.core.converter.PatientToPatientResponseConverter;
import com.speechtotext.core.dto.request.PatientRequest;
import com.speechtotext.core.dto.response.PatientResponse;
import com.speechtotext.core.service.PatientService;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patient")
public class PatientController {

    @Inject
    private PatientService patientService;

    @Inject
    private PatientToPatientResponseConverter patientToPatientResponseConverter;

    @PostMapping("register")
    PatientResponse register(PatientRequest request) {
        return patientToPatientResponseConverter.convert(patientService.register(request));
    }
}
