package com.enterprisehub.backend.identity.presentation;

import com.enterprisehub.backend.identity.application.TokenResult;

import java.time.Instant;

public record TokenResponse(String accessToken, String tokenType, Instant expiresAt) {

    static TokenResponse from(TokenResult result) {
        return new TokenResponse(result.accessToken(), result.tokenType(), result.expiresAt());
    }
}
