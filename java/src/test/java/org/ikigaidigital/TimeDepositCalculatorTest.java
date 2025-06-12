package org.ikigaidigital;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

class TimeDepositCalculatorTest {
    private final TimeDepositCalculator calculator = new TimeDepositCalculator();
    private static final double INITIAL_BALANCE = 1234567.00;
    private static final double TOLERANCE = 0.01;

    @Test
    void updateBalance_Test() {
        TimeDepositCalculator calc = new TimeDepositCalculator();
        List<TimeDeposit> plans = List.of(
                new TimeDeposit(1, "basic", INITIAL_BALANCE, 45)
        );
        calc.updateBalance(plans);

        assertThat(1).isEqualTo(1);
    }


    @Test
    void basicPlan_noInterest_within30Days() {
        TimeDeposit td = new TimeDeposit(1, "basic", INITIAL_BALANCE, 30);
        calculator.updateBalance(List.of(td));
        assertThat(td.getBalance())
                .isCloseTo(INITIAL_BALANCE, offset(TOLERANCE));
    }

    @Test
    void studentPlan_noInterest_within30Days() {
        TimeDeposit td = new TimeDeposit(1, "student", INITIAL_BALANCE, 30);
        calculator.updateBalance(List.of(td));
        assertThat(td.getBalance())
                .isCloseTo(INITIAL_BALANCE, offset(TOLERANCE));
    }

    @Test
    void premiumPlan_noInterest_within30Days() {
        TimeDeposit td = new TimeDeposit(1, "premium", INITIAL_BALANCE, 30);
        calculator.updateBalance(List.of(td));
        assertThat(td.getBalance())
                .isCloseTo(INITIAL_BALANCE, offset(TOLERANCE));
    }

    @Test
    void basicPlan_interest_after30Days() {
        TimeDeposit td = new TimeDeposit(1, "basic", INITIAL_BALANCE, 31);
        calculator.updateBalance(List.of(td));
        double expected = INITIAL_BALANCE + Math.round(INITIAL_BALANCE * 0.01 / 12 * 100.0) / 100.0;
        assertThat(td.getBalance())
                .isCloseTo(expected, offset(TOLERANCE));
    }

    @Test
    void basicPlan_interest_at60Days() {
        TimeDeposit td = new TimeDeposit(1, "basic", INITIAL_BALANCE, 60);
        calculator.updateBalance(List.of(td));
        double expected = INITIAL_BALANCE + Math.round(INITIAL_BALANCE * 0.01 / 12 * 100.0) / 100.0;
        assertThat(td.getBalance())
                .isCloseTo(expected, offset(TOLERANCE));
    }

    @Test
    void studentPlan_interest_after30Days_and_beforeYear() {
        TimeDeposit td = new TimeDeposit(1, "student", INITIAL_BALANCE, 31);
        calculator.updateBalance(List.of(td));
        double expected = INITIAL_BALANCE + Math.round(INITIAL_BALANCE * 0.03 / 12 * 100.0) / 100.0;
        assertThat(td.getBalance())
                .isCloseTo(expected, offset(TOLERANCE));
    }

    @Test
    void studentPlan_interest_at365Days() {
        TimeDeposit td = new TimeDeposit(1, "student", INITIAL_BALANCE, 365);
        calculator.updateBalance(List.of(td));
        double expected = INITIAL_BALANCE + Math.round(INITIAL_BALANCE * 0.03 / 12 * 100.0) / 100.0;
        assertThat(td.getBalance())
                .isCloseTo(expected, offset(TOLERANCE));
    }

    @Test
    void studentPlan_noInterest_afterOneYear() {
        TimeDeposit td = new TimeDeposit(1, "student", INITIAL_BALANCE, 366);
        calculator.updateBalance(List.of(td));
        assertThat(td.getBalance())
                .isCloseTo(INITIAL_BALANCE, offset(TOLERANCE));
    }

    @Test
    void premiumPlan_noInterest_within45Days() {
        TimeDeposit td = new TimeDeposit(1, "premium", INITIAL_BALANCE, 45);
        calculator.updateBalance(List.of(td));
        assertThat(td.getBalance())
                .isCloseTo(INITIAL_BALANCE, offset(TOLERANCE));
    }

    @Test
    void premiumPlan_noInterest_after30Days_but_before46Days() {
        TimeDeposit td = new TimeDeposit(1, "premium", INITIAL_BALANCE, 31);
        calculator.updateBalance(List.of(td));
        assertThat(td.getBalance())
                .isCloseTo(INITIAL_BALANCE, offset(TOLERANCE));
    }

    @Test
    void premiumPlan_interest_after45Days() {
        TimeDeposit td = new TimeDeposit(1, "premium", INITIAL_BALANCE, 46);
        calculator.updateBalance(List.of(td));
        double expected = INITIAL_BALANCE + Math.round(INITIAL_BALANCE * 0.05 / 12 * 100.0) / 100.0;
        assertThat(td.getBalance())
                .isCloseTo(expected, offset(TOLERANCE));
    }
}
