package sk.seges.acris.generator.shared.action;

import sk.seges.acris.generator.shared.domain.GeneratorToken;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch
public class GetDefaultGeneratorToken {

	@In(1)
	String language;

	@In(2)
	String webId;

	@Out(1)
	GeneratorToken token;
}