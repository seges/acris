/**
 * 
 */
package sk.seges.acris.core.showcase.client;


public class Call {
	private final String orderName;
	private final Double price;
	
	public Call(String orderName, Double price) {
		super();
		this.orderName = orderName;
		this.price = price;
	}
	
	public String getOrderName() {
		return orderName;
	}
	
	public Double getPrice() {
		return price;
	}
}