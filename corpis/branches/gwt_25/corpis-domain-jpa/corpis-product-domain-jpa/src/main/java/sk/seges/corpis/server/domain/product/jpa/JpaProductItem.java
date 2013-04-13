package sk.seges.corpis.server.domain.product.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.product.server.model.base.ProductItemBase;
import sk.seges.corpis.server.domain.product.server.model.data.ProductData;

@Entity
@Table(name = "product_item")
@SequenceGenerator(name = JpaProductItem.SEQ_PRODUCT_ITEM, sequenceName = "seq_items", initialValue = 1)
@Inheritance(strategy = InheritanceType.JOINED)
public class JpaProductItem extends ProductItemBase {

	private static final long serialVersionUID = 2436634016675455418L;
	
	protected static final String SEQ_PRODUCT_ITEM = "seqItems";

	@Id
	@Override
	@GeneratedValue(generator = SEQ_PRODUCT_ITEM)
	public Long getId() {
		return super.getId();
	}

	@Override
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = JpaProduct.class)
	public ProductData getProduct() {
		return super.getProduct();
	}
	
	@Override
	@Column(name = "count")
	public Integer getCount() {
		return super.getCount();
	}
}