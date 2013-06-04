package sk.seges.corpis.server.domain.product;

import java.util.Date;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

@DomainInterface
@BaseObject
public interface SalesPriceCondition extends PriceCondition {

	Date validFrom();
	Date validTo();
	String salesName();
	String color();
	Boolean active();
	Boolean activeForWeb();
	Boolean deleted();
	Long extId();
	String productExtId();

}