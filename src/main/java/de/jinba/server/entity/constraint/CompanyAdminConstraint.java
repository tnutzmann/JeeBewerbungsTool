package de.jinba.server.entity.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CompanyAdminValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CompanyAdminConstraint {
    String message() default "Company admin must be a company user";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
