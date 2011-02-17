package sk.seges.acris.generator.shared.action;

import sk.seges.acris.generator.shared.domain.GeneratorToken;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch
public class GetOfflineContentHtml {
	@In(1)
	String entryPointFileName;
	@In(2)
	String header;
	@In(3)
	String contentWrapper;
	@In(4)
	String content;
	@In(5)
	GeneratorToken token;
	@In(6)
	String currentServerURL;
	@Out(1)
	String result;
}