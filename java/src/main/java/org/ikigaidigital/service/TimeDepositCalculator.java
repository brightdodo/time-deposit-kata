package org.ikigaidigital.service;

import org.ikigaidigital.domain.entity.TimeDeposit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class TimeDepositCalculator {
    /**
     * Iterates over deposits, calculates interest, and updates balances.
     */
    public void updateBalance(List<TimeDeposit> deposits) {
        for (TimeDeposit td : deposits) {
            double interest = calculateInterest(td);
            double newBalance = computeNewBalance(td.getBalance(), interest);
            td.setBalance(newBalance);
        }
    }

    /**
     * Determines interest for a given deposit, handling unknown plans.
     */
    private double calculateInterest(TimeDeposit td) {
        try {
            PlanType plan = PlanType.valueOf(td.getPlanType().toUpperCase());
            return plan.calculateMonthlyInterest(td.getBalance(), td.getDays());
        } catch (IllegalArgumentException e) {
            // Unknown plan: no interest
            return 0;
        }
    }

    /**
     * Computes the new balance by adding rounded interest to the original balance.
     */
    private double computeNewBalance(double balance, double interest) {
        return balance + roundToTwoDecimals(interest);
    }

    /**
     * Rounds a decimal amount to two places, using HALF_UP.
     */
    private double roundToTwoDecimals(double amount) {
        return BigDecimal.valueOf(amount)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
