package com.learn.brainbridge.annotations;


import com.learn.brainbridge.dtos.RegisterUserDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override

    public void initialize(PasswordMatches constraintAnnotation) {
        // Optional initialization code
    }
    // Compiling the password from the form with the RegExp set
    @Override
    public boolean isValid(Object obj,ConstraintValidatorContext context) {
        if(obj instanceof RegisterUserDTO dto) {
            return dto.getPassword().equals(dto.getConfirmPassword());
        }
        return false;
    }
}
