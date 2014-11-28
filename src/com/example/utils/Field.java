package com.example.utils;


public class Field<T extends FieldInterface<T>> {

    /**
     * The "upper" bound (a prime integer or an irreducible polynomial)
     */
    public final T N;

    private T value;

    /**
     * Constructor for FiniteField.
     *
     * @param value the internal value representing this finite field
     * @param N the upper bound
     */
    public Field(T value, T N) {
        this.N = N;
        this.value = value.mod(N);
    }

    /**
     * Constructor for zero FiniteField.
     *
     * @param N the upper bound
     */
    public Field(T N) {
        this.N = N;
        this.value = N.zero();
    }

    public T getValue() {
        return value;
    }

    public Field<T> zero() {
        return new Field<T>(N);
    }

    public Field<T> one() {
        return new Field<T>(N.one(), N);
    }

    /**
     * Determine whether two finite fields are equal. Note: comparing the
     * internal integral domain is enough because the finite field will always
     * be created with the internal value smaller than the upper bound.
     *
     * @param f the other finite field
     * @return Return true if two finite fields are equal; otherwise false.
     */

    public boolean equal(Field<T> f) {
        return value.equal(f.value);
    }

    public Field<T> add(Field<T> f) {
        return new Field<T>(value.add(f.value), N);
    }

    public Field<T> mul(Field<T> f) {
        return new Field<T>(value.mul(f.value), N);
    }

    public Field<T> sub(Field<T> f) {
        return new Field<T>(value.sub(f.value), N);
    }

    public Field<T> div(Field<T> f) {
        return this.mul(f.inverse());
    }

    public Field<T> minus() {
        return new Field<T>(value.minus(), N);
    }

    public Field<T> inverse() {
    	// Use Euler's extended GCD algorithm to find the inverse
        Euclidean<T> xgcd = new Euclidean<T>();

        try {
            return new Field<T>(xgcd.xgcd(value, N).get(1), N);
        }
        // In the case of a polynomial, when the current polynomial is zero, it's
        // length will be 0 and the following exception will be thrown.
        catch (ArrayIndexOutOfBoundsException e) {
            throw new ArithmeticException("Division by zero: "
                    + this.toString());
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
