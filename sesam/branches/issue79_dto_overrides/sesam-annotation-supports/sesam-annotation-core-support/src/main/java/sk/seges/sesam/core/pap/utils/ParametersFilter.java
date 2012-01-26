package sk.seges.sesam.core.pap.utils;

import java.util.ArrayList;
import java.util.List;

import sk.seges.sesam.core.pap.model.ParameterElement;

public enum ParametersFilter {
	PROPAGATED {
		@Override
		public ParameterElement[] filterParameters(ParameterElement[] elements) {
			return filterByPropagationParameters(elements, true);
		}
	},
	NOT_PROPAGATED {
		@Override
		public ParameterElement[] filterParameters(ParameterElement[] elements) {
			return filterByPropagationParameters(elements, false);
		}
	};

	public abstract ParameterElement[] filterParameters(ParameterElement[] elements);

	private static ParameterElement[] filterByPropagationParameters(ParameterElement[] elements, boolean propagated) {
		List<ParameterElement> result = new ArrayList<ParameterElement>();
		
		for (ParameterElement element: elements) {
			if (element.isPropagated() == propagated) {
				result.add(element);
			}
		}
		return result.toArray(new ParameterElement[] {});
	}
}
