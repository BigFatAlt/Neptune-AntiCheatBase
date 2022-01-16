package cc.bigfatman.anticheat.check;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CheckData {

    String checkName() default "";
    String checkID() default "";
    String description() default "";

    boolean enabled() default true;
    boolean rubberband() default false;
}
