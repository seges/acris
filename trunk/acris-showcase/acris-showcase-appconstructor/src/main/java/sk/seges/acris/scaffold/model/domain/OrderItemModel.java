package sk.seges.acris.scaffold.model.domain;

import java.math.BigDecimal;

public interface OrderItemModel extends DomainModel {
	Double amount();
	BigDecimal basePrice();
	Double vat();
}
