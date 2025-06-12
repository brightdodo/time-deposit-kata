package org.ikigaidigital.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Withdrawal details")
public record WithdrawalDTO(
        @Schema(description = "Withdrawal identifier") Integer id,
        @Schema(description = "Amount withdrawn") Double amount,
        @Schema(description = "Date of withdrawal") LocalDate date
) {}
