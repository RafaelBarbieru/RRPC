package server.implementations;

import rrpcinterfaces.Adder;

public class AdderImpl implements Adder {
    @Override
    public int add(Integer n1, Integer n2) {
        return n1 + n2;
    }
}
