package org.ikigaidigital;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class TimeDepositCalculator {
    public void updateBalance(List<TimeDeposit> deposits) {
        for (TimeDeposit td : deposits) {
            double interest = 0;
            try {
                PlanType plan = PlanType.valueOf(td.getPlanType().toUpperCase());
                interest = plan.calculateMonthlyInterest(td.getBalance(), td.getDays());
            } catch (IllegalArgumentException e) {
                // Unknown plan: no interest
                interest = 0;
            }

            double rounded = roundToTwoDecimals(interest);
            td.setBalance(td.getBalance() + rounded);
        }
    }

    private double roundToTwoDecimals(double amount) {
        return BigDecimal.valueOf(amount)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
