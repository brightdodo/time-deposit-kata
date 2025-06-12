package org.ikigaidigital.service;

import org.ikigaidigital.domain.entity.TimeDeposit;
import org.ikigaidigital.domain.repository.TimeDepositRepository;
import org.ikigaidigital.helper.TimeDepositCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeDepositServiceTest {

    @Mock
    private TimeDepositRepository depositRepo;
    @Mock
    private TimeDepositCalculator calculator;

    @InjectMocks
    private TimeDepositService service;

    private TimeDeposit td1;
    private TimeDeposit td2;

    @BeforeEach
    void setUp() {
        td1 = new TimeDeposit(1, "basic", 1000.00, 31);
        td2 = new TimeDeposit(2, "student", 2000.00, 365);
    }

    @Test
    void findAll_shouldReturnAllDeposits() {
        // Arrange
        List<TimeDeposit> deposits = Arrays.asList(td1, td2);
        when(depositRepo.findAll()).thenReturn(deposits);

        // Act
        List<TimeDeposit> result = service.findAll();

        // Assert
        assertThat(result).containsExactly(td1, td2);
        verify(depositRepo, times(1)).findAll();
        verifyNoMoreInteractions(depositRepo, calculator);
    }

    @Test
    void updateAllBalances_shouldCalculateAndPersist() {
        // Arrange
        List<TimeDeposit> deposits = Arrays.asList(td1, td2);
        when(depositRepo.findAll()).thenReturn(deposits);

        // Act
        service.updateAllBalances();

        // Assert
        // Calculator should be called with the same list
        verify(calculator, times(1)).updateBalance(deposits);
        // Repository should saveAll
        verify(depositRepo, times(1)).saveAll(deposits);
        // No further interactions
        verifyNoMoreInteractions(calculator, depositRepo);
    }
}