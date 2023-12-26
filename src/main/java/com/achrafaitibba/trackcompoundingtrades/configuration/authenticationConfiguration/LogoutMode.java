package com.achrafaitibba.trackcompoundingtrades.configuration.authenticationConfiguration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogoutMode {
    ALL_DEVICES("ALL"),
    CURRENT_DEVICE("CURRENT");
    private final String mode;
}
