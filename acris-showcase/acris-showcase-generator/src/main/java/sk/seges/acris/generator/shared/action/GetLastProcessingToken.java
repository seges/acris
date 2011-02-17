package sk.seges.acris.generator.shared.action;

import sk.seges.acris.generator.shared.domain.GeneratorToken;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch
public class GetLastProcessingToken {

	@Out(1)
	GeneratorToken token;
}