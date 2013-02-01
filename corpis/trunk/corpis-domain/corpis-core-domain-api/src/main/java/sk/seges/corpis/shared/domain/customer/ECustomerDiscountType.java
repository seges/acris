package sk.seges.corpis.shared.domain.customer;

/**
 * 
 * @author psenicka
 *
 */
public enum ECustomerDiscountType {

	DIRECT_SALES("discount.directSales", EDiscountType.PRODUCT),
	COMMISSION_SALES("discount.commissionSales", EDiscountType.PRODUCT),
	CASH("discount.cash", EDiscountType.ORDER),
	COD("discount.cod", EDiscountType.ORDER);

	
	public enum EDiscountType {
		PRODUCT,ORDER;
	}
	
	private String i18key;
	private EDiscountType discountType;
	
	private ECustomerDiscountType(String i18key, EDiscountType discountType) {
		this.i18key = i18key;
	}
	
	public String getI18key() {
		return i18key;
	}
	
	public EDiscountType getDiscountType() {
		return discountType;
	}
}
