package sk.seges.sesam.core.pap.test.cases.model;

import java.io.Serializable;
import java.util.List;

import sk.seges.sesam.core.pap.test.cases.annotation.SuperclassTestAnnotation;

@SuperclassTestAnnotation
public class NestedBoundsModel<T extends List<Serializable>> {}