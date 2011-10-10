package sk.seges.crm.shared.domain.api;

import java.util.Set;

public interface CatalogPagePlaceModel {
	PageSize size();
	
	Set<CatalogPagePlaceModel> pageParts();
}
