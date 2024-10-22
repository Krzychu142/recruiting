package com.krzysiek.recruiting.validator;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecruitmentProcessSortValidator extends SortValidator {

    private static final List<String> ALLOWED_SORT_FIELDS = List.of("dateOfApplication", "status");

    @Override
    public void validateSortBy(String sortBy) {
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new ValidationException("Invalid sort by: " + sortBy);
        }
    }
}
