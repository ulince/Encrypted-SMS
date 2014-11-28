package com.example.utils;
public interface FieldInterface<T> {
    public abstract T zero();
    public abstract T one();

    public abstract boolean equal(T i);
    public abstract T add(T i);
    public abstract T mul(T i);
    public abstract T sub(T i);
    public abstract T div(T i);
    public abstract T mod(T i);
    public abstract T minus();
    public abstract void setValue(T i);

}
