package org.ikigaidigital.helper;

import org.ikigaidigital.domain.entity.TimeDeposit;
import org.ikigaidigital.domain.PlanType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class TimeDepositCalculator {
    private final InterestStrategyRegistry registry;

    public TimeDepositCalculator(InterestStrategyRegistry registry) {
        this.registry = registry;
    }

    public void updateBalance(List<TimeDeposit> deposits) {
        for (TimeDeposit td : deposits) {
            double interest = registry.interestFor(td.getPlanType(), td.getBalance(), td.getDays());
            td.setBalance(computeNewBalance(td.getBalance(), interest));
        }
    }

    private double computeNewBalance(double balance, double interest) {
        return balance + roundToTwoDecimals(interest);
    }

    private double roundToTwoDecimals(double amount) {
        return BigDecimal.valueOf(amount)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
