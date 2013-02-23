/**
 * 
 */
package sk.seges.corpis.domain.shared.pay.tatra;

import javax.persistence.Column;
import javax.validation.constraints.Size;

/**
 * @author ladislav.gazo
 */
public class CardPaySettings extends TatraPaySettings {
	private static final long serialVersionUID = -3994107409954978662L;
	
	@Size(max = 30)
	@Column(length = 30)
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public CardPaySettings() {
		setPt("CardPay");
	}
}
