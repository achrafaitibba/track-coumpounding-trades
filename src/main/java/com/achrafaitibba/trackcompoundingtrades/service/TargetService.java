package com.achrafaitibba.trackcompoundingtrades.service;

import com.achrafaitibba.trackcompoundingtrades.dto.request.AccountRegisterRequest;
import com.achrafaitibba.trackcompoundingtrades.enumeration.TimeFrame;
import com.achrafaitibba.trackcompoundingtrades.model.Target;
import com.achrafaitibba.trackcompoundingtrades.repository.TargetRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Builder
public class TargetService {

    private final TargetRepository targetRepository;

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
        double compoundPercentage = request.compoundPercentage() / 100;
        double estimatedFeesByTradePercentage = request.estimatedFeesByTradePercentage() / 100;
        int estimatedLossPossibilities = request.estimatedLossPossibilities();
        double stopLossPercentage = request.stopLossPercentage() / 100;
        double currentBalance = baseCapital; // todo; (baseCapital + sum(profit by trades)
        final int cycleLimit = request.tradingCycle();
        LocalDate targetDate = request.officialStartDate();
        ////////////////////////////////////::
        int cycleLimitCount = cycleLimit; //days
        int profitableTradesCount = 0;
        int losingTradesCount = 0;
        for (var i = 0; i < days; i++) {
            if (cycleLimitCount <= cycleLimit & cycleLimitCount > 0) {
                if (profitableTradesCount < (cycleLimit - estimatedLossPossibilities)) {
                    currentBalance = calculateTarget("win", currentBalance, compoundPercentage, estimatedFeesByTradePercentage);
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
                } else if (profitableTradesCount == (cycleLimit - estimatedLossPossibilities) & losingTradesCount < (cycleLimit - profitableTradesCount)) {
                    currentBalance = calculateTarget("loss", currentBalance, stopLossPercentage, estimatedFeesByTradePercentage);
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
            } else {
                cycleLimitCount = cycleLimit - 1;
                profitableTradesCount = 0;
                losingTradesCount = 0;
                if (profitableTradesCount < (cycleLimit - estimatedLossPossibilities)) {
                    currentBalance = calculateTarget("win", currentBalance, compoundPercentage, estimatedFeesByTradePercentage);
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
                } else if (profitableTradesCount == (cycleLimit - estimatedLossPossibilities) & losingTradesCount < (cycleLimit - profitableTradesCount)) {
                    currentBalance = calculateTarget("loss", currentBalance, stopLossPercentage, estimatedFeesByTradePercentage);
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
        return targets;
    }

    public double calculateTarget(String type, double currentBalance, double changePercentage, double estimatedFeesByTradePercentage) {
        switch (type) {
            case "loss":
                currentBalance = currentBalance * (1 - changePercentage);
                break;
            case "win":
                currentBalance = currentBalance * (1 + changePercentage);
                break;
        }
        currentBalance -= currentBalance * estimatedFeesByTradePercentage;
        return currentBalance;
    }

    /**
     * Weekly target : est of the latest day within the week
     * target start date = first day of the week
     * target end date = last day of the week OR start date + 6
     * timeframe = week
     */
    public void calculateWeeklyTargets(AccountRegisterRequest request) {
        List<Target> weeklyTargets = new ArrayList<>();
        List<Target> dailyTargets = calculateDailyTargets(request);
        int days = convertCompoundingPeriodToDays(request.compoundingPeriod().timeFrame(), request.compoundingPeriod().number());
        int weeks = days / 7;
        LocalDate startDate = request.officialStartDate();
        LocalDate endDate = startDate.plusDays(6);
        int mod = days % 7;
        int count = 1;
        while (count <= weeks) {
            for (Target t : dailyTargets) {
                if (t.getStartDate().equals(endDate)) {
                    weeklyTargets.add(
                            Target.builder()
                                    .timeFrame(TimeFrame.WEEK)
                                    .startDate(startDate)
                                    .endDate(t.getEndDate())
                                    .estimatedBalanceByTargetAndTimeFrame(t.getEstimatedBalanceByTargetAndTimeFrame())
                                    .build()
                    );
                    startDate = startDate.plusDays(7);
                    endDate = endDate.plusDays(7);
                    count++;
                }
            }
        }
        for (Target t : dailyTargets) {
            if (t.getStartDate().equals(endDate.minusDays(7 - mod)) & mod != 0) {
                weeklyTargets.add(
                        Target.builder()
                                .timeFrame(TimeFrame.WEEK)
                                .startDate(endDate)
                                .endDate(t.getEndDate())
                                .estimatedBalanceByTargetAndTimeFrame(t.getEstimatedBalanceByTargetAndTimeFrame())
                                .build()
                );
            }
        }
        targetRepository.saveAll(dailyTargets);
        targetRepository.saveAll(weeklyTargets);
    }

    public List<Target> calculateMonthlyTargets(AccountRegisterRequest request) {
        List<Target> dailyTarget = calculateDailyTargets(request);
        calculateWeeklyTargets(request);
        List<Target> monthlyTarget = new ArrayList<>();
        List<LocalDate> dateList = dailyTarget.stream().map(
                Target::getEndDate
        ).toList();
        Map<YearMonth, LocalDate> lastRecordOfMonthAndYearMap = dateList.stream()
                .collect(Collectors.groupingBy(
                        date -> YearMonth.from(date),
                        Collectors.collectingAndThen(Collectors.maxBy(LocalDate::compareTo), Optional::get)
                ));
        lastRecordOfMonthAndYearMap.forEach(
                (month, lastRecord) -> {
                    for (Target t : dailyTarget) {
                        if (t.getEndDate().equals(lastRecord)) {
                            monthlyTarget.add(Target.builder()
                                    .timeFrame(TimeFrame.MONTH)
                                    .startDate((t.getEndDate().with(TemporalAdjusters.firstDayOfMonth())))
                                    .endDate(lastRecord)
                                    .estimatedBalanceByTargetAndTimeFrame(t.getEstimatedBalanceByTargetAndTimeFrame())
                                    .build());
                        }
                    }
                }
        );
        targetRepository.saveAll(monthlyTarget);
        return monthlyTarget;
    }

    public List<Target> calculateYearlyTargets(AccountRegisterRequest request) {
        calculateMonthlyTargets(request);
        List<Target> yearlyTargets = new ArrayList<>();
        List<Target> dailyTarget = calculateDailyTargets(request);
        List<LocalDate> dateList = dailyTarget.stream().map(
                Target::getEndDate
        ).toList();

        Map<Integer, LocalDate> lastRecordOfYearMap = dateList.stream()
                .collect(Collectors.groupingBy(LocalDate::getYear,
                        Collectors.collectingAndThen(Collectors.maxBy(LocalDate::compareTo), Optional::get)));

        lastRecordOfYearMap.forEach(
                (month, lastRecord) -> {
                    for (Target t : dailyTarget) {
                        if (t.getEndDate().equals(lastRecord)) {
                            yearlyTargets.add(Target.builder()
                                    .timeFrame(TimeFrame.YEAR)
                                    .startDate((t.getEndDate().with(TemporalAdjusters.firstDayOfMonth())))
                                    .endDate(lastRecord)
                                    .estimatedBalanceByTargetAndTimeFrame(t.getEstimatedBalanceByTargetAndTimeFrame())
                                    .build());
                        }
                    }
                }
        );
        targetRepository.saveAll(yearlyTargets);

        return yearlyTargets;
    }


    public List<Target> calculateTargets(AccountRegisterRequest request) {
        return calculateYearlyTargets(request);
    }
}
