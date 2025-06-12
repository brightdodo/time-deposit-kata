package org.ikigaidigital.service;

import org.ikigaidigital.domain.entity.TimeDeposit;
import org.ikigaidigital.helper.TimeDepositCalculator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

class TimeDepositCalculatorTest {
    private final TimeDepositCalculator calculator = new TimeDepositCalculator();
    private static final double INITIAL_BALANCE = 1000.00;
    private static final double TOLERANCE = 0.01;

    @ParameterizedTest(name = "{0} plan at {1} days → {2}")
    @CsvSource({
            // No interest for first 30 days
            "basic,30,1000.00",
            "student,30,1000.00",
            "premium,30,1000.00",
            // Basic plan interest branch
            "basic,31,1000.83",
            "basic,60,1000.83",
            // Student plan interest branch
            "student,31,1002.50",
            "student,365,1002.50",
            "student,366,1000.00",
            // Premium plan interest branch
            "premium,31,1000.00",
            "premium,45,1000.00",
            "premium,46,1004.17",
            // Unknown plan type should yield no interest
            "gold,50,1000.00"
    })
    void planInterestScenarios(String planType, int days, double expectedBalance) {
        // Arrange
        TimeDeposit td = new TimeDeposit(1, planType, INITIAL_BALANCE, days);

        // Act
        calculator.updateBalance(List.of(td));

        // Assert
        assertThat(td.getBalance())
                .as("Balance for %s plan at %d days", planType, days)
                .isCloseTo(expectedBalance, offset(TOLERANCE));
    }
}
