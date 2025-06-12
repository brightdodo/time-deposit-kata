package org.ikigaidigital.helper;

public class BasicInterestStrategy implements InterestStrategy {
    @Override
    public String getPlanCode() {
        return "basic";
    }

    @Override
    public double calculate(double balance, int days) {
        if (days <= 30) {
            return 0;
        }
        return balance * 0.01 / 12;
    }
}
