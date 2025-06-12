package org.ikigaidigital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ikigaidigital.domain.entity.TimeDeposit;
import org.ikigaidigital.domain.entity.Withdrawal;
import org.ikigaidigital.domain.repository.TimeDepositRepository;
import org.ikigaidigital.domain.repository.WithdrawalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TimeDepositIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TimeDepositRepository depositRepo;

    @Autowired
    private WithdrawalRepository withdrawRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        withdrawRepo.deleteAll();
        depositRepo.deleteAll();
    }

    @Test
    void getAllTimeDeposits_returnsDepositsWithWithdrawals() throws Exception {
        // Seed deposit
        TimeDeposit deposit = new TimeDeposit(0, "basic", 1000.00, 40);
        deposit = depositRepo.save(deposit);

        // Seed withdrawal
        Withdrawal w = new Withdrawal();
        w.setTimeDeposit(deposit);
        w.setAmount(100.00);
        w.setDate(LocalDate.now());
        withdrawRepo.save(w);

        // Perform GET
        mockMvc.perform(get("/api/time-deposits")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(deposit.getId()))
                .andExpect(jsonPath("[0].withdrawals[0].amount").value(100.00));
    }

    @Test
    void updateBalances_updatesAndPersistsNewBalances() throws Exception {
        // Seed deposit
        TimeDeposit deposit = new TimeDeposit(0, "basic", 1200.00, 31);
        deposit = depositRepo.save(deposit);

        // Call PUT to update balances
        mockMvc.perform(put("/api/time-deposits/update-balances")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Fetch from DB and assert (using Double + offset)
        TimeDeposit updated = depositRepo.findById(deposit.getId()).orElseThrow();
        double expectedInterest = Math.round(1200.00 * 0.01 / 12 * 100.0) / 100.0;
        double expectedBalance = 1200.00 + expectedInterest;

        assertThat(updated.getBalance())
                .as("Expected balance after interest")
                .isCloseTo(expectedBalance, offset(0.01));
    }
}
