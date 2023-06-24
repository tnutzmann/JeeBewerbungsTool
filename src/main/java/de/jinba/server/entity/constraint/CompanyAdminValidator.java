package de.jinba.server.entity.constraint;

import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.enumuration.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * This validator checks that the admin of a {@link de.jinba.server.entity.Company} has the role {@link Role#COMPANY_USER}.
 */
public class CompanyAdminValidator implements ConstraintValidator<CompanyAdminConstraint, AppUser> {
    @Override
    public void initialize(CompanyAdminConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(AppUser field,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (field == null) {
            return false;
        }
        return field.getRole() == Role.COMPANY_USER;
    }
}
