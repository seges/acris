package sk.seges.sesam.core.pap.test.cases.model;

import java.io.Serializable;

import sk.seges.sesam.core.pap.test.cases.annotation.SuperclassTestAnnotation;

@SuperclassTestAnnotation
public class MultipleBoundsModel<T extends Serializable & Cloneable> {}