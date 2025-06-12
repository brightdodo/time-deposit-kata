package org.ikigaidigital;

public class PremiumInterestStrategy implements InterestStrategy {
    @Override
    public double calculate(double balance, int days) {
        // 5% per annum starting after 45 days
        if (days <= 45) {
            return 0;
        }
        return balance * 0.05 / 12;
    }
}
