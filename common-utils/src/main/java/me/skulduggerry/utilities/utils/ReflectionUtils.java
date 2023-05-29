/*
 * MIT License
 *
 * Copyright (c) 2023 Skulduggerry
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.skulduggerry.utilities.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Helper class for reflection
 *
 * @author Skulduggerry
 * @since 0.0.1
 */
public class ReflectionUtils {

    /**
     * Constructor
     */
    public ReflectionUtils() {
    }

    /**
     * Get all public fields in the class hierarchy and all not-public fields declared in the given class.
     *
     * @param type The class.
     * @return The list of fields.
     */
    @NotNull
    public static List<Field> getAllFields(@NotNull Class<?> type) {
        List<Field> result = new LinkedList<>();
        result.addAll(Arrays.asList(type.getFields()));
        result.addAll(Arrays.asList(type.getDeclaredFields()));
        return result;
    }

    /**
     * Get all public fields in the class hierarchy.
     *
     * @param type The class.
     * @return The list of fields.
     */
    @NotNull
    public static List<Field> getPublicFields(@NotNull Class<?> type) {
        return Arrays.asList(type.getFields());
    }

    /**
     * Gets all fields declared in this class.
     *
     * @param type The class.
     * @return The list of fields.
     */
    @NotNull
    public static List<Field> getDeclaredFields(@NotNull Class<?> type) {
        return Arrays.asList(type.getDeclaredFields());
    }

    /**
     * Tries to find a public field in the class hierarchy or a not-public field declared in this class which matches the given name.
     *
     * @param name The name of the field.
     * @param type The class.
     * @return The field wrapped in an optional
     */
    @NotNull
    public static Optional<Field> getField(@NotNull String name, @NotNull Class<?> type) {
        try {
            return Optional.of(type.getField(name));
        } catch (NoSuchFieldException e) {/*Ignored*/}

        try {
            return Optional.of(type.getDeclaredField(name));
        } catch (NoSuchFieldException e) {/*Ignored*/}

        return Optional.empty();
    }

    /**
     * Tries to get the value out of a field and convert it to the given target type.
     *
     * @param instance    The instance to get the value from (null when the field is static)
     * @param field       The field
     * @param targetClass The target class.
     * @param <T>         The target type of the field.
     * @return The value wrapped into an optional
     */
    public static <T> Optional<T> getFieldValue(@Nullable Object instance, @NotNull Field field, @NotNull Class<T> targetClass) {
        try {
            Object value = field.get(instance);
            T result = targetClass.cast(value);
            return Optional.of(result);
        } catch (IllegalAccessException | ClassCastException e) {/*Ignored*/}
        return Optional.empty();
    }

    /**
     * Get all constructors of this type.
     *
     * @param type The class.
     * @param <T>  The type of the class.
     * @return The list of constructors.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <T> List<Constructor<T>> getAllConstructors(@NotNull Class<T> type) {
        return Arrays.asList((Constructor<T>[]) type.getDeclaredConstructors());
    }

    /**
     * Get all public constructors for this class.
     *
     * @param type The class.
     * @param <T>  The type.
     * @return The list of constructors
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <T> List<Constructor<T>> getPublicConstructors(@NotNull Class<T> type) {
        return Arrays.asList((Constructor<T>[]) type.getConstructors());
    }

    /**
     * Get a constructor with the matching parameters in the given class.
     *
     * @param type           The class
     * @param parameterTypes The parameters' types
     * @param <T>            The type of the class
     * @return The constructor wrapped in an optional
     */
    @NotNull
    public static <T> Optional<Constructor<T>> getConstructor(@NotNull Class<T> type, @NotNull Class<?> @NotNull ... parameterTypes) {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor(parameterTypes);
            return Optional.of(constructor);
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    /**
     * Creates a new instance of the given type.
     *
     * @param type       The class.
     * @param parameters The parameters of the constructor
     * @param <T>        The type.
     * @return The new instance wrapped into an optional.
     */
    @NotNull
    public static <T> Optional<T> createNewInstance(@NotNull Class<T> type, @NotNull Object @NotNull ... parameters) {
        Optional<Constructor<T>> constructorOptional = getConstructor(type, getClasses(parameters));
        if (constructorOptional.isEmpty()) return Optional.empty();

        Constructor<T> constructor = constructorOptional.get();
        constructor.setAccessible(true);
        try {
            T instance = constructor.newInstance(parameters);
            return Optional.of(instance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return Optional.empty();
        }
    }

    /**
     * Get all public methods in the class hierarchy and all not-public methods declared in this class.
     *
     * @param type The class.
     * @return The list of methods.
     */
    @NotNull
    public static List<Method> getAllMethods(@NotNull Class<?> type) {
        List<Method> result = new LinkedList<>();
        result.addAll(Arrays.asList(type.getMethods()));
        result.addAll(Arrays.asList(type.getDeclaredMethods()));
        return result;
    }

    /**
     * Get all public methods in the class hierarchie.
     *
     * @param type The class.
     * @return The list of public methods.
     */
    @NotNull
    public static List<Method> getPublicMethods(@NotNull Class<?> type) {
        return Arrays.asList(type.getMethods());
    }

    /**
     * Get all methods declared in this class.
     *
     * @param type The class.
     * @return The list of methods.
     */
    public static List<Method> getDeclaredMethods(@NotNull Class<?> type) {
        return Arrays.asList(type.getDeclaredMethods());
    }

    /**
     * Tries to find a public method in the class hierarchy or a not-public method declared in the given class which matches the given name and parameters.
     *
     * @param name       The name of the method.
     * @param type       The class to search for the method on.
     * @param parameters The parameters' types of the method.
     * @return The method wrapped in an optional
     */
    @NotNull
    public static Optional<Method> getMethod(@NotNull Class<?> type, @NotNull String name, @NotNull Class<?> @NotNull ... parameters) {
        try {
            return Optional.of(type.getMethod(name, parameters));
        } catch (NoSuchMethodException ignored) {/**/}

        try {
            return Optional.of(type.getDeclaredMethod(name, parameters));
        } catch (NoSuchMethodException ignored) {/**/}

        return Optional.empty();
    }

    /**
     * Checks if the given executable has any parameters.
     *
     * @param executable The executable
     * @return The result.
     */
    public static boolean hasParameters(@NotNull Executable executable) {
        return executable.getParameters().length != 0;
    }

    /**
     * Checks if the executable can be called with the given parameters.
     *
     * @param executable       The executable to check
     * @param callerParameters The parameter types.
     * @return The result of the check.
     */
    public static boolean matchingParameters(@NotNull Executable executable, @NotNull Class<?> @NotNull ... callerParameters) {
        Class<?>[] parameterTypes = executable.getParameterTypes();
        if (parameterTypes.length != callerParameters.length) return false;
        for (int i = 0; i < callerParameters.length; ++i) {
            if (!parameterTypes[i].isAssignableFrom(callerParameters[i])) return false;
        }
        return true;
    }

    /**
     * Checks if the parameter types are exactly the same as the expected
     * @param executable The executable
     * @param expectedParameters The expected parameters
     * @return The result of the check.
     */
    public static boolean matchingParametersExact(@NotNull Executable executable, @NotNull Class<?> @NotNull... expectedParameters) {
        return Arrays.equals(executable.getParameterTypes(), expectedParameters);
    }

    /**
     * Checks if the return type of the given method is the same as the expected one.
     *
     * @param method             The method to check
     * @param expectedReturnType The expected return type.
     * @return The result of the check.
     */
    public static boolean matchingReturnType(@NotNull Method method, @NotNull Class<?> expectedReturnType) {
        return method.getReturnType().equals(expectedReturnType);
    }

    /**
     * Returns true if an annotation for the specified type
     * is <em>present</em> on this element, else false.  This method
     * is designed primarily for convenient access to marker annotations.
     *
     * <p>The truth value returned by this method is equivalent to:
     * {@code getAnnotation(annotationClass) != null}
     *
     * @param element        The element to search to annotation on.
     * @param annotationType the Class object corresponding to the annotation type
     * @return true if an annotation for the specified annotation type is present on this element, else false
     * @throws NullPointerException if the given annotation class is null
     */
    public static boolean hasAnnotation(@NotNull AnnotatedElement element, @NotNull Class<? extends Annotation> annotationType) {
        return element.isAnnotationPresent(annotationType);
    }

    /**
     * Returns this element's annotation for the specified type if
     * such an annotation is <em>present</em>, else null.
     *
     * @param <T>            the type of the annotation to query for and return if present
     * @param element        The element to search to annotation on.
     * @param annotationType the Class object corresponding to the
     *                       annotation type
     * @return this element's annotation for the specified annotation type if present on this element wrapped into an optional
     * @throws NullPointerException if the given annotation class is null
     */
    public static <T extends Annotation> Optional<T> getAnnotation(@NotNull AnnotatedElement element, @NotNull Class<T> annotationType) {
        return Optional.ofNullable(element.getAnnotation(annotationType));
    }

    /**
     * Returns annotations that are <em>present</em> on this element.
     * <p>
     * If there are no annotations <em>present</em> on this element, the return
     * value is an array of length 0.
     * <p>
     * The caller of this method is free to modify the returned array; it will
     * have no effect on the arrays returned to other callers.
     *
     * @return annotations present on this element
     */
    @NotNull
    public static Annotation[] getAnnotations(@NotNull AnnotatedElement element) {
        return element.getAnnotations();
    }

    /**
     * Transforms a method into a MethodHandle to allow faster execution.
     *
     * @param method The method.
     * @return The MethodHandle wrapped into an optional
     */
    @NotNull
    public static Optional<MethodHandle> toHandle(@NotNull Method method) {
        try {
            MethodType type = MethodType.methodType(method.getReturnType(), method.getParameterTypes());
            MethodHandle handle;

            if (Modifier.isStatic(method.getModifiers()))
                handle = MethodHandles.lookup().findStatic(method.getDeclaringClass(), method.getName(), type);
            else handle = MethodHandles.lookup().findVirtual(method.getDeclaringClass(), method.getName(), type);

            return Optional.of(handle);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Maps all objects to their class.
     *
     * @param args The objects.
     * @return The array of classes.
     */
    @NotNull
    public static Class<?>[] getClasses(@NotNull Object @NotNull ... args) {
        return Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class[]::new);
    }
}
