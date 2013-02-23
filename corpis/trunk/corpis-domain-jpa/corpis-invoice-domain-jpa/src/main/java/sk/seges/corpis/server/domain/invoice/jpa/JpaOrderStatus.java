package sk.seges.corpis.server.domain.invoice.jpa;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.corpis.server.domain.invoice.server.model.base.OrderStatusBase;
import sk.seges.corpis.server.domain.server.model.data.NameData;

@Entity
@SequenceGenerator(name = JpaOrderStatus.SEQ_ORDER_STATUSES, sequenceName = "seq_order_statuses", initialValue = 1)
@Table(name = "order_statuses", uniqueConstraints = @UniqueConstraint(columnNames = { "webid", "index" }))
public class JpaOrderStatus extends OrderStatusBase {

	private static final long serialVersionUID = 3777383895224806354L;
	
	protected static final String SEQ_ORDER_STATUSES = "seqOrderStatuses";

	@Override
	public Integer getIndex() {
		return super.getIndex();
	}
		
	@Override
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE }, targetEntity = JpaName.class)
	public List<NameData> getNames() {
		return super.getNames();
	}

	@Override
	public String getWebId() {
		return super.getWebId();
	}

	@Override
	@Id
	@GeneratedValue(generator = SEQ_ORDER_STATUSES)
	public Long getId() {
		return super.getId();
	}
	
}
