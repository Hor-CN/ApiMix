package cn.apimix.core.validator;


import cn.apimix.core.annotation.IntegerRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Hor
 */
public class IntegerRangeValidator implements ConstraintValidator<IntegerRange, Integer> {

    private int min;
    private int max;

    @Override
    public void initialize(IntegerRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && (value >= min && value <= max);
    }
}

