package sk.seges.crm.shared.domain.api;

import java.util.List;

public interface CatalogModel<T> {
	T id();
	String name();
	
	List<CatalogPagePlaceModel> pages();
}
