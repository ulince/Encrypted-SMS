package com.example.utils;
import java.util.ArrayList;


public class Euclidean<T extends FieldInterface<T>> {

    public ArrayList<T> xgcd(T m, T n) {
        ArrayList<T> rpq = new ArrayList<T>(3); // The result list

        T temp;
        // The list of quotients, used to find p and q at the end
        ArrayList<T> quotients = new ArrayList<T>();
        // Carry out the GCD algorithm
        while (!(m.equal(m.zero()))) {
            quotients.add(n.div(m));
            temp = m;
            m = n.mod(m);
            n = temp;
        }
        // Put the gcd into the result list.
        rpq.add(n);
        // Begin to find p and q. We will use the 'magic' Dr. Brown showed in
        // class for the calculation.

        // First put zero and one at the end of the array.
        quotients.add(m.zero());
        quotients.set(quotients.size() - 2, m.one());
        // Then do the magic calculation.
        for (int i = quotients.size() - 3; i >= 0; i--) {
            quotients.set(i, quotients.get(i).mul(quotients.get(i + 1))
                    .add(quotients.get(i + 2)));
        }
        // Put the result into the result list with the correct sign.
        if (quotients.size() % 2 == 0) {
            rpq.add(quotients.get(0));
            rpq.add(quotients.get(1).minus());
        }
        else {
            rpq.add(quotients.get(0).minus());
            rpq.add(quotients.get(1));
        }

        return rpq;

    }
}


