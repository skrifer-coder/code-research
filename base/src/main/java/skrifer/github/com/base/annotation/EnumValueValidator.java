package skrifer.github.com.base.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * 注解效验器
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

    private String[] expectedStrValue;
    private int[] expectedIntValue;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.expectedStrValue = constraintAnnotation.strValues();
        this.expectedIntValue = constraintAnnotation.intValues();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof Integer) {
            int target = (int) value;
            for (int val : expectedIntValue) {
                if (val == target) {
                    return true;
                }
            }
        } else {
            String target = (String) value;
            for (String val : expectedStrValue) {
                if (Objects.equals(target, val)) {
                    return true;
                }
            }
        }
        return false;
    }
}
