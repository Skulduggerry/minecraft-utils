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

import java.util.function.Function;

/**
 * A class which stores exactly three non mull values
 *
 * @param <T>    The type of the first parameter of the triple.
 * @param <U>    The type of the second parameter of the triple.
 * @param <V>The type of the third parameter of the triple.
 * @author Skulduggerry
 * @since 0.0.1
 */
public record Triple<T, U, V>(T first, U second, V third) {

    /**
     * Make a triple out of three given values.
     *
     * @param t      The first value.
     * @param u      The second value.
     * @param v      The third value.
     * @param <T>    The type of the first parameter of the triple.
     * @param <U>    The type of the second parameter of the triple.
     * @param <V>The type of the third parameter of the triple.
     * @return The new triple.
     */
    public static <T, U, V> Triple<T, U, V> makeTriple(@NotNull T t, @NotNull U u, @NotNull V v) {
        return new Triple<>(t, u, v);
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
    public String toString() {
        return "Triple{" +
                "t=" + first +
                ", u=" + second +
                ", v=" + third +
                '}';
    }

    /**
     * Maps all three values of the triple to new values.
     *
     * @param firstMapper  The mapper function for the first value.
     * @param secondMapper The mapper function for the second value
     * @param thirdMapper  The mapper function for the third value
     * @param <A>          The new type of the first parameter.
     * @param <B>          The new type of the second parameter.
     * @param <C>          The new type of the third parameter.
     * @return The new triple.
     */
    @NotNull
    public <A, B, C> Triple<A, B, C> map(@NotNull Function<? super T, ? extends A> firstMapper,
                                         @NotNull Function<? super U, ? extends B> secondMapper,
                                         @NotNull Function<? super V, ? extends C> thirdMapper) {
        return makeTriple(
                firstMapper.apply(first),
                secondMapper.apply(second),
                thirdMapper.apply(third)
        );
    }

    /**
     * Maps the first value of the triple to a new value.
     *
     * @param mapper The mapper function for the first value.
     * @param <A>    The new type of the first parameter.
     * @return The new triple.
     */
    @NotNull
    public <A> Triple<A, U, V> mapFirst(Function<? super T, ? extends A> mapper) {
        return makeTriple(
                mapper.apply(first),
                second,
                third
        );
    }

    /**
     * Maps the second value of the triple to a new value.
     *
     * @param mapper The mapper function for the second value
     * @param <B>    The new type of the second parameter.
     * @return The new triple.
     */
    @NotNull
    public <B> Triple<T, B, V> mapSecond(@NotNull Function<? super U, ? extends B> mapper) {
        return makeTriple(
                first,
                mapper.apply(second),
                third
        );
    }

    /**
     * Maps the third value of the triple to a new value.
     *
     * @param mapper The mapper function for the second value
     * @param <C>    The new type of the third parameter.
     * @return The new triple.
     */
    @NotNull
    public <C> Triple<T, U, C> mapThird(@NotNull Function<? super V, ? extends C> mapper) {
        return makeTriple(
                first,
                second,
                mapper.apply(third)
        );
    }
}
