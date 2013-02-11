package sk.seges.sesam.core.pap.model.mutable.api;


public interface MutableArrayTypeValue extends MutableTypeValue {

	MutableArrayType asType();
	
	MutableTypeValue[] getValue();

}
