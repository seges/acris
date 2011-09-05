package sk.seges.acris.scaffold.model.domain;

import java.math.BigDecimal;

public interface OrderItemModel {
	Double amount();
	BigDecimal basePrice();
	Double vat();
}
