package com.example.fluxoSpringOauth.repository;

import com.example.fluxoSpringOauth.entity.oauth_client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthRepository extends JpaRepository<oauth_client, Long> {
    Optional<oauth_client> findByClientId(String clientId);
}
