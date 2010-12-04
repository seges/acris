package sk.seges.acris.generator.shared.action;

import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

@GenDispatch
public class ReadHtmlBodyFromFile {

	@In(1)
	String filename;
	@Out(1)
	String header;
	@Out(2)
	String body;
}
