package sk.seges.crm.shared.domain.api;

import java.util.List;

import sk.seges.acris.scaffold.model.domain.DomainModel;

public interface CatalogModel<T> extends DomainModel {
	T id();
	String name();
	
	List<CatalogPagePlaceModel> pages();
}
