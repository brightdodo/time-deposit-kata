package org.ikigaidigital.helper;

public class StudentInterestStrategy implements InterestStrategy {
    @Override
    public String getPlanCode() {
        return "student";
    }

    @Override
    public double calculate(double balance, int days) {
        if (days <= 30 || days >= 366) {
            return 0;
        }
        return balance * 0.03 / 12;
    }
}
