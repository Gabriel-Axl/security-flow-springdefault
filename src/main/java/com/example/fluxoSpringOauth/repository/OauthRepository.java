package com.example.fluxoSpringOauth.repository;

import com.example.fluxoSpringOauth.entity.OauthClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthRepository extends JpaRepository<OauthClient, Long> {
    Optional<OauthClient> findByClientId(String clientId);
}
