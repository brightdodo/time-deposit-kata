package org.ikigaidigital.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Time Deposit details")
public record TimeDepositDTO(
        @Schema(description = "Deposit identifier") Integer id,
        @Schema(description = "Type of plan") String planType,
        @Schema(description = "Current balance") Double balance,
        @Schema(description = "Elapsed days") Integer days,
        @Schema(description = "List of withdrawals") List<WithdrawalDTO> withdrawals
) {}
