package sk.seges.acris.util;

import java.io.Serializable;

public class Tuple<A extends Serializable, B extends Serializable> extends Pair<A, B> {

	private static final long serialVersionUID = 7706657435241892508L;

    public Tuple() {}

    public Tuple(A first, B second) {
        this.first = first;
        this.second = second;
    }
}
