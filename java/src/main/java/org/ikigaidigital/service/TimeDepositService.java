package org.ikigaidigital.service;

import org.ikigaidigital.domain.entity.TimeDeposit;
import org.ikigaidigital.domain.repository.TimeDepositRepository;
import org.ikigaidigital.helper.TimeDepositCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TimeDepositService {
    private final TimeDepositRepository depositRepo;
    private final TimeDepositCalculator calculator;

    public TimeDepositService(TimeDepositRepository depositRepo, TimeDepositCalculator calculator) {
        this.depositRepo = depositRepo;
        this.calculator = calculator;
    }

    @Transactional(readOnly = true)
    public List<TimeDeposit> findAll() {
        return depositRepo.findAll();
    }

    @Transactional
    public void updateAllBalances() {
        List<TimeDeposit> deposits = depositRepo.findAll();
        calculator.updateBalance(deposits);
        depositRepo.saveAll(deposits);
    }
}
