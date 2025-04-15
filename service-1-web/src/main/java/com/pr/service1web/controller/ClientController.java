package com.pr.service1web.controller;

import com.pr.service1web.service.AuthService;
import com.pr.service1web.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
@Slf4j
public class ClientController {
    private final ClientService clientService;
    private final AuthService authService;

    @PostMapping("/id")
    public ResponseEntity<String> process(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader,
                                          @Valid @RequestBody ClientRequest request) {
        log.info("Processing request: {}", request);
        if (!authService.isValidSessionId(authHeader)) {
            log.warn("Invalid SID in header: {}", authHeader);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid session ID");
        }
        clientService.process(request.clientId());
        return ResponseEntity.ok("Client processing initiated");
    }

    @PostMapping("/login")
    public ResponseEntity<String> createSession(@RequestParam String userId) {
        log.info("Creating session for user: {}", userId);
        return ResponseEntity.ok("Bearer " + authService.generateBearerToken(userId));
    }
}
