package me.skulduggerry.utilities.collection;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
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
public final class Triple<T, U, V> {

    public final T t;
    public final U u;
    public final V v;

    /**
     * Constructor.
     *
     * @param t The first value.
     * @param u The second value.
     * @param v The third value.
     */
    public Triple(@NotNull T t, @NotNull U u, @NotNull V v) {
        this.t = t;
        this.u = u;
        this.v = v;
    }

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
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     {@code x}, {@code x.equals(x)} should return
     *     {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     {@code x} and {@code y}, {@code x.equals(y)}
     *     should return {@code true} if and only if
     *     {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     {@code x}, {@code y}, and {@code z}, if
     *     {@code x.equals(y)} returns {@code true} and
     *     {@code y.equals(z)} returns {@code true}, then
     *     {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     {@code x} and {@code y}, multiple invocations of
     *     {@code x.equals(y)} consistently return {@code true}
     *     or consistently return {@code false}, provided no
     *     information used in {@code equals} comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value {@code x},
     *     {@code x.equals(null)} should return {@code false}.
     * </ul>
     *
     * <p>
     * An equivalence relation partitions the elements it operates on
     * into <i>equivalence classes</i>; all the members of an
     * equivalence class are equal to each other. Members of an
     * equivalence class are substitutable for each other, at least
     * for some purposes.
     *
     * @param other the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @implSpec The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * In other words, under the reference equality equivalence
     * relation, each equivalence class only has a single element.
     * @apiNote It is generally necessary to override the {@link Object#hashCode hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     * @see #hashCode()
     * @see java.util.HashMap
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) other;
        return Objects.equals(t, triple.t) && Objects.equals(u, triple.u) && Objects.equals(v, triple.v);
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * {@link java.util.HashMap}.
     * <p>
     * The general contract of {@code hashCode} is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     *     an execution of a Java application, the {@code hashCode} method
     *     must consistently return the same integer, provided no information
     *     used in {@code equals} comparisons on the object is modified.
     *     This integer need not remain consistent from one execution of an
     *     application to another execution of the same application.
     * <li>If two objects are equal according to the {@link
     *     Triple#equals(Object) equals} method, then calling the {@code
     *     hashCode} method on each of the two objects must produce the
     *     same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     *     according to the {@link Triple#equals(Object) equals} method, then
     *     calling the {@code hashCode} method on each of the two objects
     *     must produce distinct integer results.  However, the programmer
     *     should be aware that producing distinct integer results for
     *     unequal objects may improve the performance of hash tables.
     * </ul>
     *
     * @return a hash code value for this object.
     * @implSpec As far as is reasonably practical, the {@code hashCode} method defined
     * by class {@code Object} returns distinct integers for distinct objects.
     * @see java.lang.Object#equals(java.lang.Object)
     * @see java.lang.System#identityHashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(t, u, v);
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
                "t=" + t +
                ", u=" + u +
                ", v=" + v +
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
                firstMapper.apply(t),
                secondMapper.apply(u),
                thirdMapper.apply(v)
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
                mapper.apply(t),
                u,
                v
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
                t,
                mapper.apply(u),
                v
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
                t,
                u,
                mapper.apply(v)
        );
    }
}
