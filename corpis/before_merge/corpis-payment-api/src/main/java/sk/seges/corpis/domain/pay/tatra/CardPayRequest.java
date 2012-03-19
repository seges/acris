/**
 * 
 */
package sk.seges.corpis.domain.pay.tatra;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import sk.seges.corpis.RegexConstants;

/**
 * @author ladislav.gazo
 */
public class CardPayRequest extends TatraPayRequest {
	private static final long serialVersionUID = 6943083740090611290L;
	
	@Size(min = 7, max = 15)
	@Column(length = 15)
	@Pattern(regexp = RegexConstants.IP)
	@NotNull
	private String ipc;
	
	public String getIpc() {
		return ipc;
	}
	
	public void setIpc(String ipc) {
		this.ipc = ipc;
	}
	
	public CardPaySettings getSettings() {
		return (CardPaySettings)super.getSettings();
	}
}
