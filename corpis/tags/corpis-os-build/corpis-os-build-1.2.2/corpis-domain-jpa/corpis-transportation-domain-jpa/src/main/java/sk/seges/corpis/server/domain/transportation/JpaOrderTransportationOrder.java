package sk.seges.corpis.server.domain.transportation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.jpa.JpaOrder;
import sk.seges.corpis.server.domain.invoice.server.model.data.AccountableData;

@Entity
@Table(name = "order_transportation_order")
@SequenceGenerator(name = JpaOrderTransportationOrder.SEQ_ORDER_TRANSPORTATION_ORDER, sequenceName = "seq_order_transportation_order", initialValue = 1)
@SuppressWarnings("serial")
public class JpaOrderTransportationOrder extends JpaTransportationOrderBase {
	
	protected static final String SEQ_ORDER_TRANSPORTATION_ORDER = "seqOrderTransporationOrder";
	
	private Long id;

	private AccountableData accountable;
	
	@Override
	@Id
	@GeneratedValue(generator = SEQ_ORDER_TRANSPORTATION_ORDER)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@OneToOne(targetEntity = JpaOrder.class)
	public AccountableData getAccountable() {
		return accountable;
	}
	
	public void setAccountable(AccountableData accountable) {
		this.accountable = accountable;
	}
}
