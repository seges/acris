package sk.seges.acris.generator.shared.action;

import sk.seges.acris.generator.shared.domain.GeneratorToken;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch
public class SaveContent {

	@In(1)
	GeneratorToken token;
	@In(2)
	String contentText;
	@Out(1)
	Boolean result;
}