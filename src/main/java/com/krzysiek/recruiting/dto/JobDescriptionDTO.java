package com.krzysiek.recruiting.dto;

import com.krzysiek.recruiting.enums.ContractType;
import com.krzysiek.recruiting.enums.WorkLocation;

import java.math.BigDecimal;

public record JobDescriptionDTO(
        Long id,
        String positionName,
        BigDecimal minSalary,
        BigDecimal maxSalary,
        ContractType[] possibleContractType,
        WorkLocation[] possibleWorkLocation,
        String requirements,
        String officeAddress
){}
