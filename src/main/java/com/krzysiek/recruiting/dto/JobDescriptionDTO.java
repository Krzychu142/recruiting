package com.krzysiek.recruiting.dto;

import com.krzysiek.recruiting.enums.ContractType;
import com.krzysiek.recruiting.enums.WorkLocation;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record JobDescriptionDTO(
        Long id,
        @NotBlank(message = "Company name can't be empty.")
        String companyName,
        @NotBlank(message = "Job title can't be empty.")
        String jobTitle,
        String companyAddress,
        @NotNull(message = "Work location can't be empty.")
        WorkLocation workLocation,
        ContractType contractType,
        String requirements,
        @Digits(integer = 8, fraction = 2, message = "Min rate must have up to 8 integer digits and 2 fractional digits.")
        BigDecimal minRate,
        @Digits(integer = 8, fraction = 2, message = "Max rate must have up to 8 integer digits and 2 fractional digits.")
        BigDecimal maxRate
){}
