package sk.seges.corpis.server.domain.manufacture;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.product.Product;

@DomainInterface
@BaseObject
public interface ManufactureProduct extends Product {

	float productionRate();
}
