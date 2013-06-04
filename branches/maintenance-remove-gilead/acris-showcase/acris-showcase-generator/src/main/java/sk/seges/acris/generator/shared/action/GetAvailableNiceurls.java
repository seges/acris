package sk.seges.acris.generator.shared.action;

import java.util.ArrayList;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch
public class GetAvailableNiceurls {

	@In(1)
	String language;

	@In(2)
	String webId;

	@Out(1)
	ArrayList<String> tokens;
}
