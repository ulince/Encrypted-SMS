package com.example.utils;
import java.util.List;

public class PolynomialBytes {
    // The internal finite field of polynomial.
    private Field<Polynomial> value;
    // The upper bound for the coefficients.
    private static Int N;
    // The zero of the coefficients of this polynomial.
    private static Field<Int> zero;
    // The irreducible polynomial for the polynomial field.
    private static Polynomial PN;
    // Initialize the static variables.
    {
        N = new Int(2);
        zero = new Field<Int>(N);
        PN = new Polynomial(zero);
        // x^8 + x^4 + x^3 + x + 1
        PN.setCoefficients(1, 1, 0, 1, 1, 0, 0, 0, 1);
    }

    /**
     * Construct a PolyByte based on the internal value.
     *
     * @param value the internal value
     */
    public PolynomialBytes(Field<Polynomial> value) {
        this.value = value;
    }

    /**
     * Construct a PolyByte basing on a integer byte.
     *
     * @param v the integer byte
     */
    public PolynomialBytes(int v) {
        Polynomial p = new Polynomial(zero);
        for (int i = 0; i < 8; i++) {
            Field<Int> c =
                    new Field<Int>(new Int((v >> i) % 2), N);
            p.addHigherPower(c);
        }
        p.clean();
        value = new Field<Polynomial>(p, PN);
    }

    /**
     * Convert this PolyByte to a integer byte.
     *
     * @return the byte
     */
    public int toByte() {
        List<Field<Int>> cs = value.getValue().getCoefficients();
        int result = 0;
        for (int i = 0; i < value.getValue().getCoefficients().size(); i++) {
            result += cs.get(i).getValue().getValue() << i;
        }
        return result;
    }

    public PolynomialBytes inverse() {
        // in the case with zero, just return zero
        if (value.equal(value.zero())) {
            return new PolynomialBytes(value.zero());
        }
        return new PolynomialBytes(value.inverse());
    }

    public PolynomialBytes mul(PolynomialBytes pb) {
        return new PolynomialBytes(value.mul(pb.value));
    }

    public PolynomialBytes add(PolynomialBytes pb) {
        return new PolynomialBytes(value.add(pb.value));
    }

    public PolynomialBytes sub(PolynomialBytes pb) {
        return new PolynomialBytes(value.sub(pb.value));
    }

    public PolynomialBytes div(PolynomialBytes pb) {
        return new PolynomialBytes(value.div(pb.value));
    }

    public PolynomialBytes pow(int n) {
        Field<Polynomial> rvalue = value.one();
        for (int i = 0; i < n; i++) {
            rvalue = rvalue.mul(value);
        }
        return new PolynomialBytes(rvalue);
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
