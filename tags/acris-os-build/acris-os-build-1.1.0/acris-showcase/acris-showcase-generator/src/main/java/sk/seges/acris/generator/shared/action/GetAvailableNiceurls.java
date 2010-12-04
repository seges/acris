package sk.seges.acris.generator.shared.action;

import java.util.List;

import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

@GenDispatch
public class GetAvailableNiceurls {

	@In(1)
	String language;

	@In(2)
	String webId;

	@Out(1)
	List<String> tokens;
}
