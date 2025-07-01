package com.example.fluxoSpringOauth.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "client-credentials")
public class getController {

    @GetMapping("/protegido")
    public ResponseEntity<String> dadoProtegido() {
        return ResponseEntity.ok("Acesso autorizado com client credentials!");
    }
}
