package com.krzysiek.recruiting.dto;

import com.krzysiek.recruiting.enums.TypeOfContract;
import com.krzysiek.recruiting.enums.WorkMode;

import java.math.BigDecimal;

public record JobDescriptionDTO(
        Long id,
        BigDecimal minSalary,
        BigDecimal maxSalary,
        TypeOfContract[] possibleTypeOfContract,
        WorkMode[] possibleWorkMode,
        String requirements,
        String officeAddress
){}
