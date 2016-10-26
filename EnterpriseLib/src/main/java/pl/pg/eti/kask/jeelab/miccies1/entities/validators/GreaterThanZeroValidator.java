package pl.pg.eti.kask.jeelab.miccies1.entities.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by mc on 2016-10-19.
 */
public class GreaterThanZeroValidator implements ConstraintValidator<GreaterThanZero, Integer> {

    @Override
    public void initialize(GreaterThanZero greaterThanZero) {
    }

    @Override
    public boolean isValid(Integer number, ConstraintValidatorContext constraintValidatorContext) {
        return number > 0;
    }
}
