package org.ikigaidigital.helper;

public class PremiumInterestStrategy implements InterestStrategy {
    @Override
    public String getPlanCode() {
        return "premium";
    }

    @Override
    public double calculate(double balance, int days) {
        if (days <= 45) {
            return 0;
        }
        return balance * 0.05 / 12;
    }
}
