package sk.seges.crm.shared.domain.api;

import java.util.List;

import sk.seges.acris.scaffold.model.domain.DomainModel;

public interface LeadModel<T> extends DomainModel {
	T id();
	
	String state();
	SalesmanModel responsible();
	List<LeadActivityModel> activities();
	CustomerModel customer();
	OfferModel offer();
}
