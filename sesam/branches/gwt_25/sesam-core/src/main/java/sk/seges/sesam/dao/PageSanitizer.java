/**
 * 
 */
package sk.seges.sesam.dao;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author ladislav.gazo
 */
public class PageSanitizer {
	private final Page valid;

	private Set<String> allowedProperties;

	public PageSanitizer(Page valid) {
		super();
		this.valid = valid;
	}

	public void process(Page request) {
		processFilterable(request);
	}

	private void processFilterable(Page request) {
		Criterion filterable = valid.getFilterable();
		if (filterable == null) {
			request.setFilterable(null);
			return;
		}

		if (request.getFilterable() == null) {
			return;
		}

		Set<String> allowedProperties = getAllowedProperties();
		Criterion requestFilterable = request.getFilterable();
		if (requestFilterable instanceof HasCriterionProperty) {
			HasCriterionProperty propertyAware = (HasCriterionProperty) requestFilterable;
			boolean contains = allowedProperties.contains(propertyAware
					.getProperty());
			if (!contains) {
				request.setFilterable(null);
			}
			return;
		} else if (requestFilterable instanceof Junction) {
			Junction junction = (Junction) requestFilterable;
			visitJunction(allowedProperties, junction);
		}
	}

	private void visitJunction(Set<String> allowedProperties, Junction junction) {
		Iterator<Criterion> iterator = junction.getJunctions().iterator();
		while (iterator.hasNext()) {
			Criterion criterion = iterator.next();
			if (criterion instanceof HasCriterionProperty) {
				HasCriterionProperty propertyAware = (HasCriterionProperty) criterion;
				boolean contains = allowedProperties.contains(propertyAware
						.getProperty());
				if (!contains) {
					iterator.remove();
				}
			} else if(criterion instanceof Junction) {
				Junction junction2 = (Junction)criterion;
				visitJunction(allowedProperties, junction2);
				if(junction2.getJunctions().size() == 0) {
					iterator.remove();
				}
			}
		}
	}

	private Set<String> getAllowedProperties() {
		if (allowedProperties != null) {
			return allowedProperties;
		}
		allowedProperties = new HashSet<String>();

		Conjunction conjunction = (Conjunction) valid.getFilterable();
		for (Criterion criterion : conjunction.getJunctions()) {
			SimpleExpression<?> property = (SimpleExpression<?>) criterion;
			allowedProperties.add(property.getProperty());
		}
		return allowedProperties;
	}
}
