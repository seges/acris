/**
 * 
 */
package sk.seges.corpis.server.domain.invoice;

public enum TransportType {
	NOT_SPECIFIC("TransportType.not_specific"), //$NON-NLS-1$
	POSTAL_SHIPMENT("TransportType.postal_shipment"), //$NON-NLS-1$
	PERSONAL("TransportType.personal"),  //$NON-NLS-1$
	CASH_ON_DELIVERY("TransportType.cash_on_delivery"); //$NON-NLS-1$
	
	private String labelKey;

	private TransportType(String labelKey) {
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