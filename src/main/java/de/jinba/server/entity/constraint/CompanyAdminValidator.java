package de.jinba.server.entity.constraint;

import de.jinba.server.entity.AppUser;
import de.jinba.server.entity.enumuration.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

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
