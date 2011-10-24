package sk.seges.crm.shared.domain.vto;

import sk.seges.crm.shared.domain.api.CatalogPagePlaceModel;
import sk.seges.crm.shared.domain.api.CustomerModel;
import sk.seges.crm.shared.domain.api.SalesmanModel;

public interface PageUtilizationModel {
	CatalogPagePlaceModel place();
	CustomerModel orderedBy();
	SalesmanModel responsible();
	Boolean graphics();
	Boolean coupon();
	Boolean discount();
	Boolean paid();
	Double price();
}
