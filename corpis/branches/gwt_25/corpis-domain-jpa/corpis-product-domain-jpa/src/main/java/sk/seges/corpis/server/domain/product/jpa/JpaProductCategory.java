package sk.seges.corpis.server.domain.product.jpa;

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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import sk.seges.corpis.server.domain.invoice.jpa.JpaName;
import sk.seges.corpis.server.domain.product.server.model.base.ProductCategoryBase;
import sk.seges.corpis.server.domain.product.server.model.data.ProductCategoryData;
import sk.seges.corpis.server.domain.product.server.model.data.ProductData;
import sk.seges.corpis.server.domain.product.server.model.data.TagData;
import sk.seges.corpis.server.domain.server.model.data.NameData;
import sk.seges.corpis.shared.domain.product.EProductCategoryType;

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
	@JoinColumn(name = "mapping")
    @OneToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY, targetEntity = JpaProductCategory.class)
	public Set<ProductCategoryData> getChildren() {
		return super.getChildren();
	}

	@Override
	@Column(name = "description")
	public String getDescription() {
		return super.getDescription();
	}

	@Override
	@Column(name = EXT_ID)
	public String getExtId() {
		return super.getExtId();
	}

	@Override
	@Column(name = "level")
	public Integer getLevel() {
		return super.getLevel();
	}

	@Override
    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE }, targetEntity = JpaName.class)
	public List<NameData> getNames() {
		return super.getNames();
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@OrderBy(value = TagData.PRIORITY)
	public List<TagData> getTags() {
		return super.getTags();
	};
	
	@Override
    @JoinColumn(name = "mapping")
    @ManyToOne (cascade=CascadeType.PERSIST, fetch=FetchType.LAZY, targetEntity = JpaProductCategory.class)
	public ProductCategoryData getParent() {
		return super.getParent();
	}

	@Override
	@Column(name = "precedency")
	public Integer getPrecedency() {
		return super.getPrecedency();
	}

	@Override
	@Column(name = "productCategoryType")
	public EProductCategoryType getProductCategoryType() {
		return super.getProductCategoryType();
	}

	@Override
    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY, targetEntity = JpaProduct.class)
    @JoinTable(name = "product_category_product")
	public List<ProductData> getProducts() {
		return super.getProducts();
	}

	@Override
	@Column(nullable = false)
	public String getWebId() {
		return super.getWebId();
	}
	
	@Column(name = "visually_decorated")
	public Boolean getVisuallyDecorated() {
		return super.getVisuallyDecorated();
	};

	@Column(name = "visually_separated")
	public Boolean getVisuallySeparated() {
		return super.getVisuallySeparated();
	}

	@Column(name = "visually_interactive")
	public Boolean getVisuallyInteractive() {
		return super.getVisuallyInteractive();
	}

	@Column(name = "provide_summary")
	public Boolean getProvideSummary() {
		return super.getProvideSummary();
	}

	@Override
	@Column
	public Boolean getContentCategory() {
		return super.getContentCategory();
	}

	@Override
	@Column(name = "load_tags_from_parent")
	public boolean isLoadTagsFromParent() {
		return super.isLoadTagsFromParent();
	}

	@Transient
	public Long getProductsCount() {
		return super.getProductsCount();
	}
}