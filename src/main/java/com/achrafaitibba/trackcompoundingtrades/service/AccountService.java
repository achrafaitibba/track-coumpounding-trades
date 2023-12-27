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
import com.achrafaitibba.trackcompoundingtrades.model.Target;
import com.achrafaitibba.trackcompoundingtrades.model.User;
import com.achrafaitibba.trackcompoundingtrades.repository.AccountRepository;
import com.achrafaitibba.trackcompoundingtrades.repository.CompoundingPeriodRepository;
import com.achrafaitibba.trackcompoundingtrades.repository.TargetRepository;
import com.achrafaitibba.trackcompoundingtrades.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    private final TargetRepository targetRepository;

    public AccountRegisterResponse accountRegister(AccountRegisterRequest request) {
        /**
         * check if account already exist
         * */
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RequestException(CustomErrorMessage.ACCOUNT_ALREADY_EXIST.getMessage(), HttpStatus.CONFLICT);
        }
        List<Target> targets = calculateDailyTargets(request);
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

        //todo add claims "id, base capital.."
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

    /**
     * Compounding period = (number * timeframe)
     */
    public int convertCompoundingPeriodToDays(TimeFrame timeFrame, Integer number) {
        double timeFrameValue = 0;
        switch (timeFrame) {
            case DAY -> timeFrameValue = 1;
            case MONTH -> timeFrameValue = 30.41;
            case WEEK -> timeFrameValue = 7;
            case YEAR -> timeFrameValue = 365;
        }
        return (int) Math.round((number * timeFrameValue));
    }

    //timeframe = days
    // start date = today
    // end date = today
    // estimatedBalanceByTargetAndTimeFrame = check below
    /**
     * Based on
     * base capital
     * compound percentage
     * estimated fees by trade %
     * estimated loss possibilities %
     * stop loss percentage
     * current balance
     */
    public List<Target> calculateDailyTargets(AccountRegisterRequest request) {
        List<Target> targets = new ArrayList<>();
        int days = convertCompoundingPeriodToDays(request.compoundingPeriod().timeFrame(), request.compoundingPeriod().number());
        double baseCapital = request.baseCapital();
        double compoundPercentage = request.compoundPercentage()/100;
        double estimatedFeesByTradePercentage = request.estimatedFeesByTradePercentage()/100;
        int estimatedLossPossibilities = request.estimatedLossPossibilities();
        double stopLossPercentage = request.stopLossPercentage()/100;
        double currentBalance = baseCapital; // todo; (baseCapital + sum(profit by trades)
        final int cycleLimit = request.tradingCycle();
        LocalDate targetDate = request.officialStartDate();
        ////////////////////////////////////::
        int cycleLimitCount = cycleLimit; //days
        int profitableTradesCount = 0; //>>>>>>>>>> (cycleLimit - estimatedLossPossibilities);
        int losingTradesCount = 0 ; //>>>>>>>>>     (cycleLimit - profitableTradesCount);
        for(var i = 0; i<days; i++){
            if(cycleLimitCount<=cycleLimit & cycleLimitCount>0){
                if(profitableTradesCount < (cycleLimit-estimatedLossPossibilities)){
                    currentBalance = calculateTarget("win",currentBalance, compoundPercentage, estimatedFeesByTradePercentage);
                    targets.add(
                            Target.builder()
                                    .timeFrame(TimeFrame.DAY)
                                    .startDate(targetDate)
                                    .endDate(targetDate)
                                    .estimatedBalanceByTargetAndTimeFrame(currentBalance)
                                    .build()
                    );
                    targetDate = targetDate.plusDays(1);
                    profitableTradesCount++;
                }
                else if(profitableTradesCount == (cycleLimit-estimatedLossPossibilities) & losingTradesCount < (cycleLimit-profitableTradesCount)){
                    currentBalance = calculateTarget("loss",currentBalance, stopLossPercentage, estimatedFeesByTradePercentage);
                    targets.add(
                            Target.builder()
                                    .timeFrame(TimeFrame.DAY)
                                    .startDate(targetDate)
                                    .endDate(targetDate)
                                    .estimatedBalanceByTargetAndTimeFrame(currentBalance)
                                    .build()
                    );
                    targetDate = targetDate.plusDays(1);
                    losingTradesCount++;

                }
                cycleLimitCount--;
            }
            else {
                cycleLimitCount = cycleLimit - 1;
                profitableTradesCount = 0;
                losingTradesCount = 0;
                if(profitableTradesCount < (cycleLimit-estimatedLossPossibilities)){
                    currentBalance = calculateTarget("win",currentBalance, compoundPercentage, estimatedFeesByTradePercentage);
                    targets.add(
                            Target.builder()
                                    .timeFrame(TimeFrame.DAY)
                                    .startDate(targetDate)
                                    .endDate(targetDate)
                                    .estimatedBalanceByTargetAndTimeFrame(currentBalance)
                                    .build()
                    );
                    targetDate = targetDate.plusDays(1);
                    profitableTradesCount++;
                }
                else if(profitableTradesCount == (cycleLimit-estimatedLossPossibilities) & losingTradesCount < (cycleLimit-profitableTradesCount)){
                    currentBalance = calculateTarget("loss",currentBalance, stopLossPercentage, estimatedFeesByTradePercentage);
                    targets.add(
                            Target.builder()
                                    .timeFrame(TimeFrame.DAY)
                                    .startDate(targetDate)
                                    .endDate(targetDate)
                                    .estimatedBalanceByTargetAndTimeFrame(currentBalance)
                                    .build()
                    );
                    targetDate = targetDate.plusDays(1);
                    losingTradesCount++;

                }
            }
        }
        targetRepository.saveAll(targets);
        return targets;
    }

    public static void main(String[] args) {
        AccountService a = AccountService.builder().build();
        List<Target> targets = new ArrayList<>();
        int days = 365;
        double baseCapital = 400;
        double compoundPercentage = 2/100;
        double estimatedFeesByTradePercentage = 0.1/100;
        int estimatedLossPossibilities = 10;
        double stopLossPercentage = 2/100;
        double currentBalance = baseCapital; // todo; (baseCapital + sum(profit by trades)
        final int cycleLimit = 10;
        LocalDate targetDate = LocalDate.parse("2023-01-01");
        ////////////////////////////////////::
        int cycleLimitCount = cycleLimit; //days
        int profitableTradesCount = 0; //>>>>>>>>>> (cycleLimit - estimatedLossPossibilities);
        int losingTradesCount = 0 ; //>>>>>>>>>     (cycleLimit - profitableTradesCount);
        for(var i = 0; i<days; i++){
            if(cycleLimitCount<=cycleLimit & cycleLimitCount>0){
                if(profitableTradesCount < (cycleLimit-estimatedLossPossibilities)){
                    currentBalance = a.calculateTarget("win",currentBalance, compoundPercentage, estimatedFeesByTradePercentage);
                    targets.add(
                            Target.builder()
                                    .targetId((long) i)
                                    .timeFrame(TimeFrame.DAY)
                                    .startDate(targetDate)
                                    .endDate(targetDate)
                                    .estimatedBalanceByTargetAndTimeFrame(currentBalance)
                                    .build()
                    );
                    targetDate = targetDate.plusDays(1);
                    profitableTradesCount++;
                }
                else if(profitableTradesCount == (cycleLimit-estimatedLossPossibilities) & losingTradesCount < (cycleLimit-profitableTradesCount)){
                    currentBalance = a.calculateTarget("loss",currentBalance, stopLossPercentage, estimatedFeesByTradePercentage);
                    targets.add(
                            Target.builder()
                                    .targetId((long) i)
                                    .timeFrame(TimeFrame.DAY)
                                    .startDate(targetDate)
                                    .endDate(targetDate)
                                    .estimatedBalanceByTargetAndTimeFrame(currentBalance)
                                    .build()
                    );
                    targetDate = targetDate.plusDays(1);
                    losingTradesCount++;
                }
                cycleLimitCount--;
            }
            else {
                cycleLimitCount = cycleLimit - 1;
                profitableTradesCount = 0;
                losingTradesCount = 0;
                if(profitableTradesCount < (cycleLimit-estimatedLossPossibilities)){
                    currentBalance = a.calculateTarget("win",currentBalance, compoundPercentage, estimatedFeesByTradePercentage);
                    targets.add(
                            Target.builder()
                                    .targetId((long) i)
                                    .timeFrame(TimeFrame.DAY)
                                    .startDate(targetDate)
                                    .endDate(targetDate)
                                    .estimatedBalanceByTargetAndTimeFrame(currentBalance)
                                    .build()
                    );
                    targetDate = targetDate.plusDays(1);
                    profitableTradesCount++;
                }
                else if(profitableTradesCount == (cycleLimit-estimatedLossPossibilities) & losingTradesCount < (cycleLimit-profitableTradesCount)){
                    currentBalance = a.calculateTarget("loss",currentBalance, stopLossPercentage, estimatedFeesByTradePercentage);
                    targets.add(
                            Target.builder()
                                    .targetId((long) i)
                                    .timeFrame(TimeFrame.DAY)
                                    .startDate(targetDate)
                                    .endDate(targetDate)
                                    .estimatedBalanceByTargetAndTimeFrame(currentBalance)
                                    .build()
                    );
                    targetDate = targetDate.plusDays(1);
                    losingTradesCount++;
                }
            }
        }

        for (Target t:targets){
            System.out.println(t.toString());
        }
    }
    public double calculateTarget(String type, double currentBalance, double changePercentage, double estimatedFeesByTradePercentage){
        switch (type){
            case "loss":  currentBalance = currentBalance * (1 - changePercentage);
            break;
            case "win": currentBalance = currentBalance * (1 + changePercentage);
            break;
        }
        currentBalance -= currentBalance * estimatedFeesByTradePercentage;
        return currentBalance;
    }

    //streams on calculateDailyTargets, filtering or grouping by weeks
    public List<Target> calculateWeeklyTargets() {
        return null;
    }
    //streams on calculateDailyTargets, filtering or grouping by months
    public List<Target> calculateMonthlyTargets() {
        return null;
    }
    //streams on calculateDailyTargets, filtering or grouping by years
    public List<Target> calculateYearlyTargets() {
        return null;
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
