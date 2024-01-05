package com.achrafaitibba.trackcompoundingtrades.dto.request;

public record AccountUpdatePassword(
        String username,
        String newPassword,
        String securityAnswer
) {
}
