/**
 * 
 */
package sk.seges.corpis.pay;

import java.util.Map;
import java.util.Map.Entry;

import sk.seges.corpis.domain.pay.PaymentMethodRequest;

/**
 * @author ladislav.gazo
 */
public abstract class AbstractPaymentRequestBuilder<T extends PaymentMethodRequest> implements ElectronicPaymentRequestBuilder<T> {

	protected void fill(Map<String, String> map, String key, Object value) {
		if(value == null) {
			return;
		}
		
		map.put(key, value.toString());
	}
	
	protected void add(StringBuilder builder, Map<String, String> map) {
		boolean first = true;
		
		for(Entry<String, String> entry : map.entrySet()) {
			if(first) {
				first = false;
			} else {
				builder.append("&");
			}
			
			if(entry.getValue() != null || entry.getValue().length() > 0) {
				builder.append(entry.getKey() + "=" + entry.getValue());
			}
		}
	}

}
