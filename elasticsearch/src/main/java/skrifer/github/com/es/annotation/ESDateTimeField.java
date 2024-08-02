package skrifer.github.com.es.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ESDateTimeField {
    String format() default "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis";
}
