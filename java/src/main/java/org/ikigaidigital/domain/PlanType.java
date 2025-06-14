package org.ikigaidigital.domain;

import org.ikigaidigital.helper.BasicInterestStrategy;
import org.ikigaidigital.helper.InterestStrategy;
import org.ikigaidigital.helper.PremiumInterestStrategy;
import org.ikigaidigital.helper.StudentInterestStrategy;

public enum PlanType {
    BASIC(new BasicInterestStrategy()),
    STUDENT(new StudentInterestStrategy()),
    PREMIUM(new PremiumInterestStrategy());

    private final InterestStrategy strategy;

    PlanType(InterestStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Applies universal guard: no interest for first 30 days
     */
    public double calculateMonthlyInterest(double balance, int days) {
        if (days <= 30) {
            return 0;
        }
        return strategy.calculate(balance, days);
    }
}
