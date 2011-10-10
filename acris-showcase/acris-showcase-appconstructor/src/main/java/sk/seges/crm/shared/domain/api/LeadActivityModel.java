package sk.seges.crm.shared.domain.api;

import java.util.Date;

import sk.seges.acris.scaffold.model.domain.DomainModel;

public interface LeadActivityModel extends DomainModel {
	SalesmanModel executedBy();
	Date when();
	ActivityTypeModel type();
	String note();
	LeadModel<?> lead();
}
