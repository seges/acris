package sk.seges.crm.shared.domain.api;

import java.util.Set;

import sk.seges.acris.scaffold.model.domain.DomainModel;

public interface CatalogPagePlaceModel extends DomainModel {
	PageSize size();
	
	Set<CatalogPagePlaceModel> pageParts();
}
