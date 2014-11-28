package com.example.utils;
import java.util.List;
import java.util.ArrayList;


/**
 * Polynomial.java
 * This class represents a polynomial with coefficients of a finite field on
 * integers. It support a minimal set of arithmetic operations.
 *
 * @author Tianyu Geng, Brandon Amos
 * @version Dec 2, 2012
 */
public class Polynomial implements FieldInterface<Polynomial> {
    // The zero of its coefficient.
    private Field<Int> zero;
    // The list of coefficients in this polynomial.
    private List<Field<Int>> coefficients;

    /**
     * Constructor for Polynomial
     *
     * @param zero
     *            the zero elements in that finite field. This is necessary
     *            because this polynomial supports finite field with size of any
     *            prime number. This zero will contain the this information.
     */
    public Polynomial(Field<Int> zero) {
        this.zero = zero;
        coefficients = new ArrayList<Field<Int>>();
    }

    /**
     * Construct a copy of Polynomial p.
     *
     * @param p the original polynomial
     */
    public Polynomial(Polynomial p) {
        coefficients = new ArrayList<Field<Int>>(p.coefficients);
        this.zero = p.zero;
    }

    /**
     * Set/reset the coefficients of this polynomial. (Note if the integer is
     * bigger than the prime number chosen to create the finite field, the mod
     * operation will be carried out during construction of the finite field)
     *
     * @param ints the coefficients
     */
    public void setCoefficients(int... ints) {
        coefficients.clear();
        for (int i : ints) {
            coefficients.add(new Field<Int>(new Int(i), zero.N));
        }
        this.clean();
    }

    public List<Field<Int>> getCoefficients() {
        return this.coefficients;
    }

    /**
     * Add a higher order element to the polynomial with coefficient c. This
     * method is intended to use to build the polynomial term by term.
     *
     * @param c the coefficient for the next high order.
     * @return Return the order of this polynomial after this operation
     */
    public int addHigherPower(Field<Int> c) {
        this.coefficients.add(c);
        return this.coefficients.size() - 1;
    }

    @Override
    public Polynomial zero() {
        return new Polynomial(zero);
    }

    @Override
    public Polynomial one() {
        Polynomial one = new Polynomial(zero);
        one.coefficients.add(zero.one());
        return one;
    }

    @Override
    public boolean equal(Polynomial i) {
        if (coefficients.size() != i.coefficients.size())
            return false;
        // compare the coefficients
        for (int j = 0; j < coefficients.size(); j++) {
            if (!coefficients.get(j).equal(i.coefficients.get(j))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Polynomial add(Polynomial p) {
        Polynomial result = new Polynomial(zero);
        // Add the coefficients piece by piece.
        if (this.coefficients.size() < p.coefficients.size()) {
            int i;
            for (i = 0; i < this.coefficients.size(); i++) {
                result.coefficients.add(this.coefficients.get(i).add(
                        p.coefficients.get(i)));
            }
            for (; i < p.coefficients.size(); i++) {
                result.coefficients.add(p.coefficients.get(i));
            }
        }
        else {
            int i;
            for (i = 0; i < p.coefficients.size(); i++) {
                result.coefficients.add(this.coefficients.get(i).add(
                        p.coefficients.get(i)));
            }
            for (; i < this.coefficients.size(); i++) {
                result.coefficients.add(this.coefficients.get(i));
            }
        }
        result.clean();
        return result;
    }

    @Override
    public Polynomial mul(Polynomial p) {
        Polynomial result = new Polynomial(zero);
        // Create a polynomial to hold the result and put zeros to the terms.
        for (int i = 0; i < this.coefficients.size()
                + p.coefficients.size() - 1; i++) {
            result.coefficients.add(zero);
        }
        // For each term in the current polynomial and p, calculate the product
        // of the coefficient and add this result to the overall result.
        for (int i = 0; i < this.coefficients.size(); i++) {
            for (int j = 0; j < p.coefficients.size(); j++) {
            	Field<Int> coefficient = result.coefficients.get(i+j).add(
                    this.coefficients.get(i).mul(p.coefficients.get(j)));
                result.coefficients.set(i+j, coefficient);
            }
        }
        result.clean();
        return result;
    }

    /**
     * Clean up the high order terms with zero coefficients.
     *
     * @return the number of terms cleaned.
     */
    public int clean() {
        int counter = 0;
        while (!this.coefficients.isEmpty()
                && this.coefficients.get(this.coefficients.size() - 1)
                        .equal(zero)) {
            counter++;
            this.coefficients.remove(this.coefficients.size() - 1);
        }
        return counter;
    }

    @Override
    public Polynomial sub(Polynomial p) {
        Polynomial result = new Polynomial(zero);
        // Subtract term by term
        if (this.coefficients.size() < p.coefficients.size()) {
            int i;
            for (i = 0; i < this.coefficients.size(); i++) {
                result.coefficients.add(this.coefficients.get(i).sub(
                        p.coefficients.get(i)));
            }
            for (; i < p.coefficients.size(); i++) {
                result.coefficients.add(p.coefficients.get(i).minus());
            }
        }
        else {
            int i;
            for (i = 0; i < p.coefficients.size(); i++) {
                result.coefficients.add(this.coefficients.get(i).sub(
                        p.coefficients.get(i)));
            }
            for (; i < this.coefficients.size(); i++) {
                result.coefficients.add(this.coefficients.get(i));
            }
        }
        result.clean();
        return result;
    }

    @Override
    public Polynomial div(Polynomial p) {
        // If the order of current polynomial is lower than that of p, return
        // zero.
        if (this.coefficients.size() < p.coefficients.size()) {
            return zero();
        }
        Polynomial result = new Polynomial(zero);
        // Pad the result with correct number of zeros for the coefficients
        for (int i = 0; i < this.coefficients.size()
                - p.coefficients.size() + 1; i++) {
            result.coefficients.add(zero);
        }
        Polynomial temp = new Polynomial(this);
        // Do the polynomial division
        for (int j = temp.coefficients.size() - p.coefficients.size(); j >= 0;) {
            // Calculate the coefficients when the highest order term in the
            // current polynomial is divided by the highest order term in
            // polynomial p.
            Field<Int> c =
                    temp.coefficients
                            .get(temp.coefficients.size() - 1)
                            .div(p.coefficients.get(p.coefficients.size() - 1));
            result.coefficients.set(j, c);
            // Multiply the coefficient c to p and subtract the result from temp
            for (int i = 1; i <= p.coefficients.size(); i++) {
            	Field<Int> coefficient = temp.coefficients.get(
	                temp.coefficients.size()-i).sub(p.coefficients
	                		.get(p.coefficients.size()-i).mul(c));
                temp.coefficients.set(temp.coefficients.size()-i, coefficient);
            }
            // Clean the high order terms in temp that has a zero coefficient
            // and decrease j accordingly.
            j -= temp.clean();
        }
        result.clean();
        return result;
    }

    @Override
    public Polynomial mod(Polynomial p) {
        if (this.coefficients.size() < p.coefficients.size()) {
            return this;
        }
        // See the notes for div() for the following code. They are exactly the
        // same except there is no need to create a polynomial to hold the
        // result.
        Polynomial temp = new Polynomial(this);
        for (int j = temp.coefficients.size() - p.coefficients.size(); j >= 0;) {
            Field<Int> c =
                    temp.coefficients
                            .get(temp.coefficients.size() - 1)
                            .div(p.coefficients.get(p.coefficients.size() - 1));
            for (int i = 1; i <= p.coefficients.size(); i++) {
            	Field<Int> coefficient = temp.coefficients.get(
                    temp.coefficients.size() - i).sub(p.coefficients.get(
                		p.coefficients.size() - i).mul(c));
                temp.coefficients.set(temp.coefficients.size()-i, coefficient);
            }
            j -= temp.clean();
        }
        temp.clean();
        return temp;
    }

    @Override
    public Polynomial minus() {
        Polynomial result = new Polynomial(zero);
        // Inverse the coefficients term by term.
        for (Field<Int> c : this.coefficients) {
            result.coefficients.add(c.minus());
        }
        return result;
    }

    @Override
    public String toString() {
        if (this.coefficients.isEmpty())
            return "0";
        StringBuilder sb = new StringBuilder();
        for (int i = this.coefficients.size() - 1; i >= 0; i--) {
            String coefficient;
            if (this.coefficients.get(i).equal(zero))
                continue;
            if (this.coefficients.get(i).equal(zero.one()) && i != 0) {
                coefficient = "";
            }
            else {
                coefficient = "" + this.coefficients.get(i);
            }
            if (i == 0) {
                sb.append(coefficient + " + ");
                continue;
            }
            if (i == 1) {
                sb.append(coefficient + "x + ");
                continue;
            }
            sb.append(coefficient + "x^" + i + " + ");
        }
        return sb.substring(0, sb.length() - 3);
    }

    @Override
    public void setValue(Polynomial p) {
        this.zero = p.zero;
        this.coefficients = p.coefficients;
    }
}