package sk.seges.corpis.shared.domain.finance.api;

public enum ECurrency {
	// thanks for not changing order
	SKK(30.1260, "SKK"), EUR(1.0, "€"), USD(21.921, "USD");
	
	private final double course;
	private final String symbol;
	
	ECurrency(double course, String symbol) {
		this.course = course;
		this.symbol = symbol;
	}
	
	public double getCourse() {
		return course;
	}
	
	public String getSymbol() {
		return symbol;
	}
}
