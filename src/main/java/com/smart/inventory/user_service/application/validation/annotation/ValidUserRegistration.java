package com.smart.inventory.user_service.application.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.smart.inventory.user_service.application.validation.validator.UserRegistrationValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = UserRegistrationValidator.class)
@Target({ ElementType.TYPE })  // Apply on class level
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserRegistration {
    String message() default "User with same Username or Email already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

