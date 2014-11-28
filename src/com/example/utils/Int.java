package com.example.utils;


public class Int implements FieldInterface<Int> {
    private int value;

    /**
     * Constructor for Int
     *
     * @param v the value for this int.
     */
    public Int(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }

    @Override
    public Int zero() {
        return new Int(0);
    }

    @Override
    public Int one() {
        return new Int(1);
    }

    @Override
    public boolean equal(Int i) {
        return value == i.value;
    }

    @Override
    public Int add(Int i) {
        return new Int(value + i.value);
    }

    @Override
    public Int mul(Int i) {
        return new Int(value * i.value);
    }

    @Override
    public Int sub(Int i) {
        return new Int(value - i.value);
    }

    @Override
    public Int div(Int i) {
        return new Int(value / i.value);
    }

    @Override
    public Int mod(Int i) {
        return new Int((value % i.value + i.value) % i.value);
    }

    @Override
    public Int minus() {
        return new Int(-value);
    }

    public void setValue(Int i) {
        this.value = i.value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

}
