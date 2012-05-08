/**
 * 
 */
package sk.seges.corpis.shared.domain.invoice;

public enum RemittanceType {
	ACCOUNT_TRANSFER("RemittanceType.account_transfer"),  //$NON-NLS-1$
	POST_TRANSFER("RemittanceType.post_transfer"),  //$NON-NLS-1$
	CASH("RemittanceType.cash"),  //$NON-NLS-1$
	CASH_ON_DELIVERY("RemittanceType.cash_on_delivery");  //$NON-NLS-1$

	private String labelKey;

	private RemittanceType(String labelKey) {
		this.labelKey = labelKey;
	}

	public String getLabelKey() {
		return labelKey;
	}

	@Override
	public String toString() {
		return labelKey;
	}
}