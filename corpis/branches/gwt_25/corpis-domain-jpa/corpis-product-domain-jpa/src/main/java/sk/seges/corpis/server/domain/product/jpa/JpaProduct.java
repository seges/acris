package sk.seges.corpis.server.domain.product.jpa;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import sk.seges.corpis.server.domain.customer.jpa.JpaCustomerCore;
import sk.seges.corpis.server.domain.customer.server.model.data.CustomerCoreData;
import sk.seges.corpis.server.domain.invoice.jpa.JpaDescription;
import sk.seges.corpis.server.domain.invoice.jpa.JpaName;
import sk.seges.corpis.server.domain.invoice.jpa.JpaVat;
import sk.seges.corpis.server.domain.product.server.model.base.ProductBase;
import sk.seges.corpis.server.domain.product.server.model.data.ProductCategoryData;
import sk.seges.corpis.server.domain.product.server.model.data.ProductData;
import sk.seges.corpis.server.domain.product.server.model.data.ProductPriceData;
import sk.seges.corpis.server.domain.product.server.model.data.TagData;
import sk.seges.corpis.server.domain.search.jpa.JpaSupValue;
import sk.seges.corpis.server.domain.search.server.model.data.SupValueData;
import sk.seges.corpis.server.domain.server.model.data.DescriptionData;
import sk.seges.corpis.server.domain.server.model.data.NameData;
import sk.seges.corpis.server.domain.server.model.data.VatData;

@SuppressWarnings("serial")
@Entity
@Table(name = "product")
@SequenceGenerator(name = JpaProduct.SEQ_PRODUCT, sequenceName = "seq_products", initialValue = 1)
public class JpaProduct extends ProductBase {

	protected static final String SEQ_PRODUCT = "seqProduct";

	protected static final int EXT_ID_MIN_LENGTH = 1;
	protected static final int EXT_ID_MAX_LENGTH = 30;

	@Override
	@Id
	@GeneratedValue(generator = SEQ_PRODUCT)
	public Long getId() {
		return super.getId();
	}

	@OneToMany(targetEntity = JpaProduct.class, mappedBy = "variant", cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE })
	public List<ProductData> getChildVariants() {
		return super.getChildVariants();
	}

	@Override
	@NotNull
	@Size(min = EXT_ID_MIN_LENGTH, max = EXT_ID_MAX_LENGTH)
	@Column(name = ProductData.EXT_ID, nullable = false, length = EXT_ID_MAX_LENGTH)
	public String getExtId() {
		return super.getExtId();
	}

	@Override
	@Size(min = EXT_ID_MIN_LENGTH, max = EXT_ID_MAX_LENGTH)
	@Column(name = ProductData.EXTERNAL_ID, nullable = true, length = EXT_ID_MAX_LENGTH)
	public String getExternalId() {
		return super.getExternalId();
	}
	
	@Override
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE }, targetEntity = JpaName.class)
	public List<NameData> getNames() {
		return super.getNames();
	}

	@Override
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE }, targetEntity = JpaDescription.class)
	public List<DescriptionData> getDescriptions() {
		return super.getDescriptions();
	}
	
	@Override
	@Lob
	@Column(columnDefinition = "text", name = "description")
	public String getDescription() {
		return super.getDescription();
	}

	@Override
	@Column(name = "weight")
	public Float getWeight() {
		return super.getWeight();
	}

	@Override
	@Column(nullable = false)
	public String getWebId() {
		return super.getWebId();
	}
	
	@Override
	@Column(name = IMPORTED_DATE)
	public Date getImportedDate() {
		return super.getImportedDate();
	}
	
	@Override
	@Column(name = "units_per_package")
	public Integer getUnitsPerPackage() {
		return super.getUnitsPerPackage();
	}

	@Override
	@OneToMany(cascade = { CascadeType.ALL }, targetEntity = JpaProductPrice.class)
	@OrderBy(ProductPriceData.PRIORITY)
	@JoinTable(name = "product_product_prices")
	public List<ProductPriceData> getPrices() {
		return super.getPrices();
	}

	@Override
	@OneToMany(cascade = { CascadeType.ALL }, targetEntity = JpaProductPrice.class)
	@OrderBy(value = ProductPriceData.PRIORITY)
	@JoinTable(name = "product_product_fees")
	public Set<ProductPriceData> getFees() {
		return super.getFees();
	}

	@Override
	@NotNull
	@ManyToOne(cascade = { CascadeType.MERGE }, targetEntity = JpaVat.class)
	public VatData getVat() {
		return super.getVat();
	}

	@Override
	@Column(name = "count")
	public Integer getCount() {
		return super.getCount();
	}

	@Override
	@Column
	public Boolean getDeleted() {
		return super.getDeleted();
	}
	
	@Override
	@Column
	public Long getPriority() {
		return super.getPriority();
	}
	
	@Override
	@Transient
	public List<ProductData> getRelatedProducts() {
		return super.getRelatedProducts();
	}
	
	@Override
	@ManyToMany(mappedBy = "products", targetEntity = JpaProductCategory.class)
	public List<ProductCategoryData> getCategories() {
		return super.getCategories();
	}

	@Override
	@Column(name = "thumbnail_path")
	public String getThumbnailPath() {
		return super.getThumbnailPath();
	}
	
	@Override
	@ManyToMany(cascade = { CascadeType.MERGE}, targetEntity = JpaTag.class)
	public Set<TagData> getTags() {
		return super.getTags();
	}

	@OneToMany(cascade = { CascadeType.ALL }, targetEntity = JpaSupValue.class, orphanRemoval=true)
	@JoinColumn(name="product_id")
	public Set<SupValueData> getSups() {
		return super.getSups();
	};

	@Override
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = JpaCustomerCore.class)
	public CustomerCoreData getManufacturer() {
		return super.getManufacturer();
	}

	@Override
	@ManyToOne(targetEntity = JpaCustomerCore.class)
	public CustomerCoreData getSeller() {
		return super.getSeller();
	}

	@Override
	@ManyToOne(targetEntity = JpaProduct.class)
	public ProductData getVariant() {
		return super.getVariant();
	}
}