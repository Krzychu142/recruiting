package com.krzysiek.recruiting.validator.implementation;

import jakarta.validation.ValidationException;
import org.springframework.data.domain.Sort;

public abstract class SortValidator {

    public Sort.Direction validateSortDirection(String sortDirection) {
        try {
            return Sort.Direction.fromString(sortDirection.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid sort direction: " + sortDirection);
        }
    }

    public abstract void validateSortBy(String sortBy);
}
