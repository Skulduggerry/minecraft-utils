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

package me.skulduggerry.utilities.exception;

/**
 * Thrown to indicate that a slot has been accessed with an illegal index. The
 * index is either negative or greater than or equal to the size of the maximum
 * number of slots.
 *
 * @since 0.2.1
 */
public class SlotIndexOutOfBoundsException extends IndexOutOfBoundsException {

    /**
     * Constructs an {@code SlotIndexOutOfBoundsException} with no detail message.
     */
    public SlotIndexOutOfBoundsException() {
    }

    /**
     * Constructs an {@code SlotIndexOutOfBoundsException} with the specified detail
     * message.
     *
     * @param s the detail message
     */
    public SlotIndexOutOfBoundsException(String s) {
        super(s);
    }

    /**
     * Constructs a new {@code SlotIndexOutOfBoundsException} class with an
     * argument indicating the illegal index.
     *
     * <p>The index is included in this exception's detail message.  The
     * exact presentation format of the detail message is unspecified.
     *
     * @param index the illegal index.
     * @since 9
     */
    public SlotIndexOutOfBoundsException(int index) {
        super("Slot index out of range: " + index);
    }
}
