package sk.seges.corpis.shared.domain.invoice.api;

public interface HasPrice {
	
	PriceData getPrice();

	void setPrice(PriceData price);

}