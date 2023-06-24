package de.jinba.server.entity.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * This annotation is used to validate that a {@link de.jinba.server.entity.AppUser} is a company user.
 */
@Documented
@Constraint(validatedBy = CompanyAdminValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CompanyAdminConstraint {
    String message() default "Company admin must be a company user";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
