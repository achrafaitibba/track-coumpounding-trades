package com.achrafaitibba.trackcompoundingtrades.service;

import com.achrafaitibba.trackcompoundingtrades.configuration.token.JwtService;
import com.achrafaitibba.trackcompoundingtrades.configuration.token.Token;
import com.achrafaitibba.trackcompoundingtrades.configuration.token.TokenRepository;
import com.achrafaitibba.trackcompoundingtrades.configuration.token.TokenType;
import com.achrafaitibba.trackcompoundingtrades.dto.response.AccountRegister;
import com.achrafaitibba.trackcompoundingtrades.model.User;
import com.achrafaitibba.trackcompoundingtrades.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;


    public AccountRegister accountRegister(User user){
        User toSave = userRepository.save(User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .build());
        /** Instead of initiating an empty hashmap you can create a list of claims and add them to the hashmap
         Such as birthdate, account status... and any other data needed to be sent to the client whiting the token
         Example:
         Map<String, Object> currentDate = new HashMaps<>();
         currentDate.put("now", LocalDateTime.now()....);
         Claims could be : email, pictureLink, roles & groups , authentication time...
         */
        var jwtToken = jwtService.generateToken(new HashMap<>(), toSave);
        var refreshToken = jwtService.generateRefreshToken(toSave);
        saveUserToken(toSave, jwtToken);
        return new AccountRegister(user.getUsername(), jwtToken, refreshToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

}
