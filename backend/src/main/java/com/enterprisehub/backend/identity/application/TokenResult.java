package com.enterprisehub.backend.identity.application;

import java.time.Instant;

public record TokenResult(String accessToken, String tokenType, Instant expiresAt) {
}
