package com.achrafaitibba.trackcompoundingtrades.dto.response;

public record AccountRegister(
        String username,
        String accessToken,
        String refreshToken
) {
}
