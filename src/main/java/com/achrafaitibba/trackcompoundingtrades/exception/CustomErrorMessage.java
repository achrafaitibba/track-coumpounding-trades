package com.achrafaitibba.trackcompoundingtrades.exception;

public enum CustomErrorMessage {
    ACCOUNT_ALREADY_EXIST("The username you provided already used"),
    COMPOUNDING_PERCENTAGE_LOWER_THAN_ZERO("The compounding percentage should be higher than ZERO"),
    COMPOUNDING_PERCENTAGE_LESS_THAN_HUNDRED("The compounding percentage should be less than 100"),
    FEES_HIGHER_THAN_COMPOUNDING_RATE("Fees should not be higher or equal to compounding rate/percentage"),
    POSITIVE_VALUE_FOR_FEES("Fees can't have negative value"),
    ESTIMATED_LOSS_BETWEEN_CYCLE_ZERO("Estimated loss possibilities should be between the trading cycle and zero"),
    OFFICIAL_START_DATE_IN_FUTURE("Official start date can't be in past"),
    COMPOUNDING_LESS_THAN_MONTH("Compounding period should be at least 30 days"),
    ACCOUNT_NOT_EXISTING("The account you provided doesn't exist"),
    PASSWORD_INCORRECT("The password you entered is incorrect"),
    TRADE_DATE_SHOULD_BE_AFTER_START_DATE("Trade date should be after the official start date"),
    NEGATIVE_INVESTED_CAP("The invested cap should be higher than Zero"),
    INVESTED_CAP_HIGHER_THAN_BASE_CAP("The invested capital can't be higher than current balance"),
    CLOSED_AT_ZERO_VALUE("The closed at value should be higher than ZERO")
    ;
    private final String msg;
    CustomErrorMessage(String msg){
        this.msg = msg;
    }
    public String getMessage(){
        return this.msg;
    }
}
