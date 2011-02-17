package sk.seges.acris.generator.shared.action;

import sk.seges.acris.generator.shared.domain.GeneratorToken;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch
public class WriteTextToFile {

	@In(1)
	String content;

	@In(2)
	GeneratorToken token;
}
