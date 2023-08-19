package com.friend.app.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DebtDTO {

    private Long id;

    @NotEmpty
    private String debtorUsername;

    @NotEmpty
    private String lenderUsername;

    @Min(value = 1)
    private double amount;

    private LocalDate creditRepaymentDate;

    @Min(value = 0)
    @Max(value = 100)
    private int percent;

    public DebtDTO() {
    }

    public DebtDTO(String lenderUsername) {
        this.lenderUsername = lenderUsername;
    }
}
