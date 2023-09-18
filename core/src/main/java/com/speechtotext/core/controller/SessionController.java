package com.speechtotext.core.controller;

import com.speechtotext.core.dto.request.SessionRequest;
import com.speechtotext.core.dto.response.SessionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/session")
public class SessionController {

    @PostMapping(value = "establish", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<SessionResponse> establishSession(@RequestBody SessionRequest sessionRequest) {
        String sessionId = UUID.randomUUID().toString();
        SessionResponse response = new SessionResponse();
        response.setSessionID(sessionId);

        return ResponseEntity.ok().contentType(APPLICATION_JSON).body(response);
    }
}
