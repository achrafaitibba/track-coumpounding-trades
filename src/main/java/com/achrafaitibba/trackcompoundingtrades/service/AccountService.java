package com.achrafaitibba.trackcompoundingtrades.service;

import com.achrafaitibba.trackcompoundingtrades.configuration.token.JwtService;
import com.achrafaitibba.trackcompoundingtrades.configuration.token.Token;
import com.achrafaitibba.trackcompoundingtrades.configuration.token.TokenRepository;
import com.achrafaitibba.trackcompoundingtrades.configuration.token.TokenType;
import com.achrafaitibba.trackcompoundingtrades.dto.request.AccountAuthenticateRequest;
import com.achrafaitibba.trackcompoundingtrades.dto.request.AccountRegisterRequest;
import com.achrafaitibba.trackcompoundingtrades.dto.response.AccountAuthenticateResponse;
import com.achrafaitibba.trackcompoundingtrades.enumeration.CustomErrorMessage;
import com.achrafaitibba.trackcompoundingtrades.exception.RequestException;
import com.achrafaitibba.trackcompoundingtrades.model.Account;
import com.achrafaitibba.trackcompoundingtrades.model.CompoundingPeriod;
import com.achrafaitibba.trackcompoundingtrades.model.Target;
import com.achrafaitibba.trackcompoundingtrades.model.User;
import com.achrafaitibba.trackcompoundingtrades.repository.AccountRepository;
import com.achrafaitibba.trackcompoundingtrades.repository.CompoundingPeriodRepository;
import com.achrafaitibba.trackcompoundingtrades.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Builder
public class AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final CompoundingPeriodRepository compoundingPeriodRepository;
    private final TargetService targetService;

    public AccountAuthenticateResponse accountRegister(AccountRegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RequestException(CustomErrorMessage.ACCOUNT_ALREADY_EXIST.getMessage(), HttpStatus.CONFLICT);
        }
        if(request.compoundPercentage()<=0){
            throw new RequestException(CustomErrorMessage.COMPOUNDING_PERCENTAGE_LOWER_THAN_ZERO.getMessage(), HttpStatus.CONFLICT);
        }
        if(request.compoundPercentage()>=100){
            throw new RequestException(CustomErrorMessage.COMPOUNDING_PERCENTAGE_LESS_THAN_HUNDRED.getMessage(), HttpStatus.CONFLICT);
        }
        if(request.estimatedFeesByTradePercentage() >= request.compoundPercentage()){
            throw new RequestException(CustomErrorMessage.FEES_HIGHER_THAN_COMPOUNDING_RATE.getMessage(), HttpStatus.CONFLICT);
        }
        if(request.estimatedFeesByTradePercentage() <=0){
            throw new RequestException(CustomErrorMessage.POSITIVE_VALUE_FOR_FEES.getMessage(), HttpStatus.CONFLICT);
        }
        if(request.estimatedLossPossibilities()<=0 || request.estimatedLossPossibilities() > request.tradingCycle()){
            throw new RequestException(CustomErrorMessage.ESTIMATED_LOSS_BETWEEN_CYCLE_ZERO.getMessage(), HttpStatus.CONFLICT);
        }
        if(request.officialStartDate().isBefore(LocalDate.now())){
            throw new RequestException(CustomErrorMessage.OFFICIAL_START_DATE_IN_FUTURE.getMessage(), HttpStatus.CONFLICT);
        }

        if(targetService.convertCompoundingPeriodToDays(request.compoundingPeriod().timeFrame(), request.compoundingPeriod().number()) <30){
            throw new RequestException(CustomErrorMessage.COMPOUNDING_LESS_THAN_MONTH.getMessage(), HttpStatus.CONFLICT);

        }
        List<Target> targets = targetService.calculateTargets(request);
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
                        .estimatedLossPossibilities(request.estimatedLossPossibilities())
                        .tradingCycle(request.tradingCycle())
                        .stopLossPercentage(request.stopLossPercentage())
                        .currentBalance(request.baseCapital())
                        .estimatedCompoundedBalance(targets.get(targets.size()-1).getEstimatedBalanceByTargetAndTimeFrame())
                        .officialStartDate(request.officialStartDate())
                        .compoundingPeriod(
                                compoundingPeriod
                        )
                        .targets(targets)
                        .build());
        User toSave = userRepository.save(User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .account(account)
                .build());

        //todo add claims "baseCapital, current balance.."
        Map<String, Object> claims  = new HashMap<>();
        claims.put("accountId", account.getAccountId());
        var jwtToken = jwtService.generateToken(claims, toSave);
        var refreshToken = jwtService.generateRefreshToken(toSave);
        saveUserToken(toSave, jwtToken);
        return new AccountAuthenticateResponse(request.username(), jwtToken, refreshToken);
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
