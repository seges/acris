package sk.seges.sesam.core.pap.writer.api;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;


public interface DelayedPrintWriter {

	void setSerializer(ClassSerializer serializer);
	void serializeTypeParameters(boolean typed);
	
    void println(Object... x);

    void print(Object... x);

}
