package com.achrafaitibba.trackcompoundingtrades.enumeration;

public enum TradeSortingOption {
    DATE("date"),
    INVESTED("investedCap"),
    PNL("PNL"),
    TARGET("targetByInvestedCap"),
    DIFF("diffProfitTarget"),
    PAIR("tradingPair");
    private final String option;

    TradeSortingOption(String option){
        this.option = option;
    }

    public String getOption(){
        return this.option;
    }
}
