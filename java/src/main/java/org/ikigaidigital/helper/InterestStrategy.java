package org.ikigaidigital.helper;

/**
 * Strategy interface for calculating monthly interest
 */
public interface InterestStrategy {
    /**
     * Unique plan code, e.g. 'basic', 'student', 'premium'.
     */
    String getPlanCode();
    /**
     * Calculate raw monthly interest based on balance and days elapsed
     * @param balance current balance
     * @param days    total days elapsed
     * @return interest amount
     */
    double calculate(double balance, int days);
}
