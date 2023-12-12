package com.speechtotext.core.controller;

import com.speechtotext.core.converter.PatientToPatientResponseConverter;
import com.speechtotext.core.dto.request.PatientRequest;
import com.speechtotext.core.dto.response.PatientResponse;
import com.speechtotext.core.service.PatientService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/patient")
public class PatientController {

    @Inject
    private PatientService patientService;

    @Inject
    private PatientToPatientResponseConverter patientToPatientResponseConverter;

    @PostMapping("register")
    PatientResponse register(@Valid @RequestBody PatientRequest request) {
        return patientToPatientResponseConverter.convert(patientService.register(request));
    }

    @GetMapping("/all")
    Set<PatientResponse> getAllForTenant() {
        return patientToPatientResponseConverter.convert(patientService.getAllForTenant());
    }

    @GetMapping("{id}/get")
    PatientResponse getPatient(@PathVariable String id) {
        return patientToPatientResponseConverter.convert(patientService.findById(id));
    }

    @PutMapping("{id}/attach_summary")
    PatientResponse attachSummary(@PathVariable String id, @RequestBody String summary) {
        return patientToPatientResponseConverter.convert(patientService.setSummary(id, summary));
    }

    @DeleteMapping("{id}/delete")
    void deletePatient(@PathVariable String id) {
        patientService.deletePatient(id);
    }
}
