package com.OEzoa.OEasy.util.timeTrace;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeTrace {
    String startMessage() default "Started Method";
    String endMessage() default "Ended Method";
}