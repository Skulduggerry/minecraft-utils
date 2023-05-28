package me.skulduggerry.utilities.scheduler;

import org.jetbrains.annotations.Range;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows to execute the annotated method with a {@link Scheduler}.
 * Annotated methods must have no parameters and must not be static.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatingTask {

    /**
     * @return Whether the task should be executed sync or async
     */
    boolean sync() default true;

    /**
     * @return the delay before the first execution
     */
    @Range(from = 0, to = Long.MAX_VALUE)
    long delay() default 0;

    /**
     * @return the period between two executions
     */
    @Range(from = 1, to = Long.MAX_VALUE)
    long period() default 20;

    /**
     * put in all classes which have a static method marked with {@link ExceptionListener} to handle exceptions
     *
     * @return listener classes
     */
    Class<?>[] listeners() default {};
}
