package com.example.fluxoSpringOauth.controller;

import com.example.fluxoSpringOauth.entity.OauthClient;
import com.example.fluxoSpringOauth.repository.OauthRepository;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "client-credentials")
@SecurityRequirement(name = "oauth2")
public class getController {


    public getController(BCryptPasswordEncoder passwordEncoder, OauthRepository oauthRepository) {
        this.passwordEncoder = passwordEncoder;
        this.oauthRepository = oauthRepository;
    }

    private final BCryptPasswordEncoder passwordEncoder;
    private OauthRepository oauthRepository;

    @GetMapping("/protegido")
    public ResponseEntity<String> dadoProtegido() {
        return ResponseEntity.ok("Acesso autorizado com client credentials!");
    }

    @PostMapping("/user")
    public ResponseEntity<OauthClient> createUser(@RequestBody OauthClient user) {
        OauthClient savedUser = new OauthClient();
        savedUser.setClientId(user.getClientId());
        savedUser.setClientSecret(passwordEncoder.encode( user.getClientSecret()));
        return ResponseEntity.ok(oauthRepository.save(savedUser));
    }
}
