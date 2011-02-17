package sk.seges.acris.generator.shared.action;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch
public class GenerateOffline {

	@Out(1)
	boolean result;
}
