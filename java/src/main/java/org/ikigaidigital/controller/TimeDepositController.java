package org.ikigaidigital.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ikigaidigital.domain.dto.TimeDepositDTO;
import org.ikigaidigital.domain.dto.WithdrawalDTO;
import org.ikigaidigital.domain.entity.TimeDeposit;
import org.ikigaidigital.domain.entity.Withdrawal;
import org.ikigaidigital.service.TimeDepositService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/time-deposits")
@Tag(name = "Time Deposits", description = "Operations on time deposits")
public class TimeDepositController {
    private final TimeDepositService service;

    public TimeDepositController(TimeDepositService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all time deposits with withdrawals")
    @ApiResponse(responseCode = "200", description = "List of time deposits retrieved")
    public ResponseEntity<List<TimeDepositDTO>> getAll() {
        List<TimeDepositDTO> dtos = service.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/update-balances")
    @Operation(summary = "Update balances of all time deposits")
    @ApiResponse(responseCode = "204", description = "Balances updated successfully")
    public ResponseEntity<Void> updateBalances() {
        service.updateAllBalances();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private TimeDepositDTO toDTO(TimeDeposit td) {
        List<WithdrawalDTO> wDtos = td.getWithdrawals().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new TimeDepositDTO(
                td.getId(), td.getPlanType(), td.getBalance(), td.getDays(), wDtos
        );
    }

    private WithdrawalDTO toDTO(Withdrawal w) {
        return new WithdrawalDTO(
                w.getId(), w.getAmount(), w.getDate()
        );
    }
}
