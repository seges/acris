package sk.seges.crm.shared.domain.api;

import java.util.Date;

public interface LeadActivityModel {
	SalesmanModel executedBy();
	Date when();
	ActivityTypeModel type();
	String note();
	LeadModel<?> lead();
}
