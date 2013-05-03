package sk.seges.corpis.server.domain.payment.jpa;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.jpa.JpaWebIDAwareOrderItem;
import sk.seges.corpis.server.domain.payment.server.model.data.PadData;

@Entity
@Table(name = "pads_order_item")
public class JpaPadOrderItem extends JpaWebIDAwareOrderItem {
	private static final long serialVersionUID = -2311130659542638722L;
	
	private PadData pad;

	@ManyToOne(targetEntity = JpaPad.class)
	public PadData getPad() {
		return pad;
	}
	
	public void setPad(PadData pad) {
		this.pad = pad;
	}
	
	@Override
	public String toString() {
		return "PadOrderItem [pad=" + pad + ", toString()=" + super.toString() + "]";
	}
}
