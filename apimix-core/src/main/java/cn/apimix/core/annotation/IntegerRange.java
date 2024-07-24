package cn.apimix.core.annotation;

import cn.apimix.core.validator.IntegerRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 数值范围注解
 * @author Hor
 */
@Documented
@Constraint(validatedBy = IntegerRangeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegerRange {
    String message() default "类型不符合指定的范围";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min() default 0;

    int max() default 10;
}
