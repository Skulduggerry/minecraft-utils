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

package me.skulduggerry.utilities.collection;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

/**
 * A class which stores exactly two non mull values
 *
 * @param <T> The type of the first parameter of the pair.
 * @param <U> The type of the second parameter of the pair.
 * @author Skulduggerry
 * @since 0.0.1
 */
public record Pair<T, U>(T first, U second) {

    /**
     * Make a pair out of two given values.
     *
     * @param t   The first parameter of the pair.
     * @param u   The second parameter of the pair.
     * @param <T> The type of the first parameter of the pair.
     * @param <U> The type of the second parameter of the pair.
     * @return The new pair.
     */
    @NotNull
    public static <T, U> Pair<T, U> makePair(@NotNull T t, @NotNull U u) {
        return new Pair<>(t, u);
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     * @apiNote In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * The string output is not necessarily stable over time or across
     * JVM invocations.
     * @implSpec The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     */
    @Override
    @NotNull
    public String toString() {
        return "Pair{" +
                "t=" + first +
                ", u=" + second +
                '}';
    }

    /**
     * Maps both values of the pair to new values.
     *
     * @param firstMapper  The mapper function for the first value.
     * @param secondMapper The mapper function for the second value
     * @param <A>          The new type of the first parameter.
     * @param <B>          The new type of the second parameter.
     * @return The new pair.
     */
    @NotNull
    public <A, B> Pair<A, B> map(@NotNull Function<? super T, ? extends A> firstMapper, @NotNull Function<? super U, ? extends B> secondMapper) {
        return makePair(
                firstMapper.apply(first),
                secondMapper.apply(second)
        );
    }

    /**
     * Maps the first value of the pair to a new value.
     *
     * @param mapper The mapper function for the first value.
     * @param <A>    The new type of the first parameter.
     * @return The new pair.
     */
    @NotNull
    public <A> Pair<A, U> mapFirst(Function<? super T, ? extends A> mapper) {
        return makePair(
                mapper.apply(first),
                second
        );
    }

    /**
     * Maps the second value of the pair to a new value.
     *
     * @param mapper The mapper function for the second value
     * @param <B>    The new type of the second parameter.
     * @return The new pair.
     */
    @NotNull
    public <B> Pair<T, B> mapSecond(@NotNull Function<? super U, ? extends B> mapper) {
        return makePair(
                first,
                mapper.apply(second)
        );
    }

    /**
     * Converts a map to a collection of pairs.
     *
     * @param map The map.
     * @param <T> The type of the key.
     * @param <U> The type of the value.
     * @return The collection of pairs.
     */
    public static <T, U> Collection<Pair<T, U>> fromMap(@NotNull Map<T, U> map) {
        return map.entrySet()
                .stream()
                .map(entry -> makePair(entry.getKey(), entry.getValue()))
                .toList();
    }

    /**
     * Converts a collection of pairs to a map.
     *
     * @param pairs The pairs.
     * @param <T>   The type of the key.
     * @param <U>   The type of the value.
     * @return The map.
     */
    public static <T, U> Map<T, U> toMap(@NotNull Collection<Pair<T, U>> pairs) {
        Map<T, U> map = new HashMap<>();
        for (Pair<T, U> pair : pairs) {
            map.put(pair.first, pair.second);
        }

        return map;
    }
}
