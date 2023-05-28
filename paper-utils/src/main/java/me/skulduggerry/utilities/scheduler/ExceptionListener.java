package me.skulduggerry.utilities.scheduler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Makes a method to a listener for exceptions in a {@link RepeatingTask}.
 * The annotated methods must be static.
 * Methods which have this annotation must take a {@link Scheduler.TaskHandle} as first argument and a {@link Throwable} as second argument.
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionListener {
}

