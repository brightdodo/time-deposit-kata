package org.ikigaidigital.helper;

public class StudentInterestStrategy implements InterestStrategy {
    @Override
    public double calculate(double balance, int days) {
        // 3% per annum only for days < 366
        if (days >= 366) {
            return 0;
        }
        return balance * 0.03 / 12;
    }
}
