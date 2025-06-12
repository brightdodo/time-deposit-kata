package org.ikigaidigital;

public class BasicInterestStrategy implements InterestStrategy {
    @Override
    public double calculate(double balance, int days) {
        // 1% per annum, applied monthly
        return balance * 0.01 / 12;
    }
}
