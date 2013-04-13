package sk.seges.corpis.server.domain.product.jpa;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.corpis.server.domain.product.server.model.base.RelatedProductBase;
import sk.seges.corpis.server.domain.product.server.model.data.ProductData;

@Entity
@Table(name = "related_product", uniqueConstraints = { @UniqueConstraint(columnNames = {"product", "related_product" }) })
@SequenceGenerator(name = JpaRelatedProduct.SEQ_RELATED_PRODUCTS, sequenceName = "seq_related_products", initialValue = 1)
public class JpaRelatedProduct extends RelatedProductBase {
	private static final long serialVersionUID = -5614882896518185987L;

	protected static final String SEQ_RELATED_PRODUCTS = "seqRelatedProducts";

	@Override
	@Id
	@GeneratedValue(generator = SEQ_RELATED_PRODUCTS)
	public Long getId() {
		return super.getId();
	}
	
	@Override
	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "product")
	public ProductData getProduct() {
		return super.getProduct();
	}

	@Override
	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "related_product")
	public ProductData getRelatedProduct() {
		return super.getRelatedProduct();
	}
}