package com.krzysiek.recruiting.exception;

import com.krzysiek.recruiting.dto.FieldErrorDTO;
import lombok.Getter;

@Getter
public class NonIdenticalVariablesException extends Exception{

    private final FieldErrorDTO fieldErrorDTO;

    public NonIdenticalVariablesException(String errorMessage, FieldErrorDTO fieldErrorDTO){
        super(errorMessage);
        this.fieldErrorDTO = fieldErrorDTO;
    }
}
