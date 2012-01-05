package sk.seges.sesam.pap.configuration.model.setting;

import sk.seges.sesam.core.configuration.annotation.Parameter;
import sk.seges.sesam.pap.configuration.model.parameter.ParameterContext;

public class SettingsContext extends ParameterContext {

	private Parameter parameter;

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

	public String getParameterName() {
		if (parameter == null) {
			return null;
		}
		return parameter.name();
	}

	public String getParameterDescription() {
		if (parameter == null) {
			return null;
		}
		return parameter.description();
	}
}