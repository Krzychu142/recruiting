package com.krzysiek.recruiting.validator;

import com.krzysiek.recruiting.dto.RegisterRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegisterRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterRequestDTO request = (RegisterRequestDTO) target;

        if (request.password().length() < 6) {
            errors.rejectValue(
                    "password",
                    "ToShortPassword",
                    "Password must has at least 6 characters."
            );
        }

        if (!request.password().equals(request.confirmedPassword())){
            errors.rejectValue("confirmedPassword",
                    "ConfirmedPasswordNotMath",
                    "Password and Confirmed password must be the same values."
            );
        }
    }
}
