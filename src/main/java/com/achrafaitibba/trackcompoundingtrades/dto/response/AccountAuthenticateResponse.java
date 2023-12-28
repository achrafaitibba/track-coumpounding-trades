package com.achrafaitibba.trackcompoundingtrades.dto.response;

public record AccountAuthenticateResponse(
        String username,
        String accessToken,
        String refreshToken
) {
}
