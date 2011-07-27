package sk.seges.sesam.core.pap.test.superclass.typed;

import java.io.Serializable;
import java.util.List;

@SuperClassMarker
public class TestBeanNestedBounds<T extends List<Serializable>> {}