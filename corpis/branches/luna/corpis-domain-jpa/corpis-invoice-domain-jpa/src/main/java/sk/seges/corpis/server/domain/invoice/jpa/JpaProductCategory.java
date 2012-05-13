package sk.seges.corpis.server.domain.invoice.jpa;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.corpis.server.domain.invoice.server.model.base.ProductCategoryBase;
import sk.seges.corpis.server.domain.invoice.server.model.data.ProductCategoryData;
import sk.seges.corpis.server.domain.invoice.server.model.data.ProductData;
import sk.seges.corpis.server.domain.server.model.data.NameData;
import sk.seges.corpis.shared.domain.invoice.EProductCategoryType;

@Entity
@Table (name = "product_category", uniqueConstraints = @UniqueConstraint(columnNames = { ProductCategoryData.WEB_ID, ProductCategoryData.EXT_ID }))
@SequenceGenerator(name = JpaProductCategory.SEQ_PRODUCT_CATEGORY, sequenceName = "seq_product_category", initialValue = 1)
public class JpaProductCategory extends ProductCategoryBase {

	private static final long serialVersionUID = 5291165456785090234L;

    protected static final String SEQ_PRODUCT_CATEGORY = "seqProductCategories";

	@Override
    @Id
    @GeneratedValue(generator = SEQ_PRODUCT_CATEGORY)
	public Long getId() {
		return super.getId();
	}

	@Override
    @JoinColumn
    @OneToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY, targetEntity = JpaProductCategory.class)
	public Set<ProductCategoryData> getChildren() {
		return super.getChildren();
	}

	@Column
	@Override
	public String getDescription() {
		return super.getDescription();
	}

	@Override
    @Column
	public String getExtId() {
		return super.getExtId();
	}

	@Override
    @Column
	public Integer getLevel() {
		return super.getLevel();
	}

	@Override
    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE }, targetEntity = JpaName.class)
	public List<NameData> getNames() {
		return super.getNames();
	}

	@Override
    @JoinColumn
    @ManyToOne (cascade=CascadeType.PERSIST, fetch=FetchType.LAZY, targetEntity = JpaProductCategory.class)
	public ProductCategoryData getParent() {
		return super.getParent();
	}

	@Column
	@Override
	public Integer getPrecedency() {
		return super.getPrecedency();
	}

	@Column
	@Override
	public EProductCategoryType getProductCategoryType() {
		return super.getProductCategoryType();
	}

	@Override
    @ManyToMany (cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
    @JoinTable(name = "product_category_product")
	public List<ProductData> getProducts() {
		return super.getProducts();
	}

	@Override
	@Column
	public String getWebId() {
		return super.getWebId();
	}
}