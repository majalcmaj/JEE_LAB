package pl.pg.eti.kask.jeelab.miccies1.entities.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by mc on 2016-10-19.
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GreaterThanZeroValidator.class)
public @interface GreaterThanZero {
    String message() default "{GreaterThanZero.LESS_THAN_ZERO}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
