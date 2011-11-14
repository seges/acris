package sk.seges.sesam.pap.configuration.model.setting;

import sk.seges.sesam.core.configuration.annotation.Parameter;
import sk.seges.sesam.pap.configuration.model.parameter.ParameterContext;

public class SettingsContext extends ParameterContext {

	private Parameter parameter;
	private String prefix = "";

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

	public String getParameterName() {
		if (parameter == null) {
			return null;
		}
		return getPrefix() + parameter.name();
	}

	public String getParameterDescription() {
		if (parameter == null) {
			return null;
		}
		return parameter.description();
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return prefix;
	}
}