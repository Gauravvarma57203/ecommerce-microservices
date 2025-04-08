package com.ecommerce.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidEmailDomainValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmailDomain {
    String message() default "Invalid email domain";

    String[] allowed();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
