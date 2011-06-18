package sk.seges.sesam.core.pap.test.superclass.typed;

import java.io.Serializable;

@SuperClassTest
public class TestBeanUpperBounds<T extends Serializable & Cloneable> {}