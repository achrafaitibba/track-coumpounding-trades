package com.achrafaitibba.trackcompoundingtrades.dto.response;

public record AccountRegisterResponse(
        String username,
        String accessToken,
        String refreshToken
) {
}
