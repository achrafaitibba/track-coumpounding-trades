package com.achrafaitibba.trackcompoundingtrades.service;

import com.achrafaitibba.trackcompoundingtrades.configuration.token.JwtService;
import com.achrafaitibba.trackcompoundingtrades.configuration.token.Token;
import com.achrafaitibba.trackcompoundingtrades.configuration.token.TokenRepository;
import com.achrafaitibba.trackcompoundingtrades.configuration.token.TokenType;
import com.achrafaitibba.trackcompoundingtrades.dto.request.AccountRegisterRequest;
import com.achrafaitibba.trackcompoundingtrades.dto.response.AccountRegisterResponse;
import com.achrafaitibba.trackcompoundingtrades.enumeration.CustomErrorMessage;
import com.achrafaitibba.trackcompoundingtrades.enumeration.TimeFrame;
import com.achrafaitibba.trackcompoundingtrades.exception.RequestException;
import com.achrafaitibba.trackcompoundingtrades.model.Account;
import com.achrafaitibba.trackcompoundingtrades.model.CompoundingPeriod;
import com.achrafaitibba.trackcompoundingtrades.model.User;
import com.achrafaitibba.trackcompoundingtrades.repository.AccountRepository;
import com.achrafaitibba.trackcompoundingtrades.repository.CompoundingPeriodRepository;
import com.achrafaitibba.trackcompoundingtrades.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final CompoundingPeriodRepository compoundingPeriodRepository;

    public AccountRegisterResponse accountRegister(AccountRegisterRequest request) {
        //check if account already exist
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RequestException(CustomErrorMessage.ACCOUNT_ALREADY_EXIST.getMessage(), HttpStatus.CONFLICT);
        }
        CompoundingPeriod compoundingPeriod = compoundingPeriodRepository.save(CompoundingPeriod
                .builder()
                .number(request.compoundingPeriod().number())
                .timeFrame(request.compoundingPeriod().timeFrame())
                .build());
        Account account = accountRepository.save(
                Account.builder()
                        .baseCapital(request.baseCapital())
                        .compoundPercentage(request.compoundPercentage())
                        .estimatedFeesByTradePercentage(request.estimatedFeesByTradePercentage())
                        .estimatedLossPossibilitiesPercentage(request.estimatedLossPossibilitiesPercentage())
                        .stopLossPercentage(request.stopLossPercentage())
                        .currentBalance(request.baseCapital())
                        .estimatedCompoundedBalance(100D)//todo
                        .officialStartDate(request.officialStartDate())
                        .compoundingPeriod(
                                compoundingPeriod
                        )
                        .build());
        User toSave = userRepository.save(User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .account(account)
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
        return new AccountRegisterResponse(request.username(), jwtToken, refreshToken);
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
