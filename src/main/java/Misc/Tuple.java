package Misc;

import java.util.Arrays;

/**
 * Class representing tuples of values (immutable), of variable size.
 *
 * @param <E> the type of the values contained
 */
public class Tuple<E> {
    private final E[] values;

    /**
     * Constructor for Tuples.
     * @param values the values to contain.
     */
    public Tuple(E... values) {
        this.values = values;
    }

    /**
     * Gets a value from the Tuple.
     * @param index the index of the wanted value.
     * @return
     */
    public E get(int index) {
        return values[index];
    }

    /**
     * Determines if two instances of Tuples are to be considered equal.
     * @param other the Tuple to compare with.
     * @return whether the Tuples are considered equal or not.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Tuple)) {
            return false;
        } else {
            return Arrays.equals(((Tuple) other).values, values);
        }
    }

    /**
     * Returns a String representing this Tuple, using the toString method of every value.
     * @return a String representing this Tuple, using the toString method of every value.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');
        for (int i = 0; i < values.length; i++) {
            stringBuilder.append(values[i].toString());
            if (i != values.length - 1) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append(')');
        return stringBuilder.toString();
    }
}
