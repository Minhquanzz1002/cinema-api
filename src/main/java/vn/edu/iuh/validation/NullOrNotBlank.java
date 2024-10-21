package vn.edu.iuh.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullOrNotBlankValidator.class)
@Documented
public @interface NullOrNotBlank {
    String message() default "Phải là null hoặc không được để trống";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
