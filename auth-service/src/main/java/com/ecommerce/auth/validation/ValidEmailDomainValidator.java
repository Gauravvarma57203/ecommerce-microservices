package com.ecommerce.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ValidEmailDomainValidator implements ConstraintValidator<ValidEmailDomain, String> {

    private List<String> allowedDomains;

    @Override
    public void initialize(ValidEmailDomain constraintAnnotation) {
        allowedDomains = Arrays.asList(constraintAnnotation.allowed());
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || !email.contains("@")) {
            return false;
        }

        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();

        if (allowedDomains.contains(domain)) {
            return true;
        }

        // Override default message with list of allowed domains
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                "Please use a valid email from: " + String.join(", ", allowedDomains)
        ).addConstraintViolation();

        return false;
    }
}
