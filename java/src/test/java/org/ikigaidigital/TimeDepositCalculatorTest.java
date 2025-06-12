package org.ikigaidigital;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

class TimeDepositCalculatorTest {
    private final TimeDepositCalculator calculator = new TimeDepositCalculator();
    private static final double INITIAL_BALANCE = 1000.00;
    private static final double TOLERANCE = 0.01;

    @ParameterizedTest(name = "No interest for {0} plan at {1} days")
    @CsvSource({
            "basic,30,1000.00",
            "student,30,1000.00",
            "premium,30,1000.00"
    })
    void noInterestWithin30Days(String planType, int days, double expectedBalance) {
        TimeDeposit td = new TimeDeposit(1, planType, INITIAL_BALANCE, days);
        calculator.updateBalance(List.of(td));
        assertThat(td.getBalance())
                .as("Balance for %s plan at %d days", planType, days)
                .isCloseTo(expectedBalance, offset(TOLERANCE));
    }

    @ParameterizedTest(name = "Basic plan at {1} days → {2}")
    @CsvSource({
            "basic,31,1000.83",
            "basic,60,1000.83"
    })
    void basicPlanInterest(String planType, int days, double expectedBalance) {
        TimeDeposit td = new TimeDeposit(1, planType, INITIAL_BALANCE, days);
        calculator.updateBalance(List.of(td));
        assertThat(td.getBalance())
                .as("Basic plan balance at %d days", days)
                .isCloseTo(expectedBalance, offset(TOLERANCE));
    }

    @ParameterizedTest(name = "Student plan at {1} days → {2}")
    @CsvSource({
            "student,31,1002.50",
            "student,365,1002.50",
            "student,366,1000.00"
    })
    void studentPlanInterest(String planType, int days, double expectedBalance) {
        TimeDeposit td = new TimeDeposit(1, planType, INITIAL_BALANCE, days);
        calculator.updateBalance(List.of(td));
        assertThat(td.getBalance())
                .as("Student plan balance at %d days", days)
                .isCloseTo(expectedBalance, offset(TOLERANCE));
    }

    @ParameterizedTest(name = "Premium plan at {1} days → {2}")
    @CsvSource({
            "premium,31,1000.00",
            "premium,45,1000.00",
            "premium,46,1004.17"
    })
    void premiumPlanInterest(String planType, int days, double expectedBalance) {
        TimeDeposit td = new TimeDeposit(1, planType, INITIAL_BALANCE, days);
        calculator.updateBalance(List.of(td));
        assertThat(td.getBalance())
                .as("Premium plan balance at %d days", days)
                .isCloseTo(expectedBalance, offset(TOLERANCE));
    }
}
