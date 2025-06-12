package org.ikigaidigital;

/**
 * Strategy interface for calculating monthly interest
 */
public interface InterestStrategy {
    /**
     * Calculate raw monthly interest based on balance and days elapsed
     * @param balance current balance
     * @param days    total days elapsed
     * @return interest amount
     */
    double calculate(double balance, int days);
}
