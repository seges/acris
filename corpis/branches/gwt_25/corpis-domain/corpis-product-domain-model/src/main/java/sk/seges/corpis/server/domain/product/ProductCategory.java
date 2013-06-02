package sk.seges.corpis.server.domain.product;

import java.util.List;
import java.util.Set;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.HasWebId;
import sk.seges.corpis.server.domain.Name;
import sk.seges.corpis.shared.domain.product.EProductCategoryType;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface ProductCategory extends IMutableDomainObject<Long>, HasWebId {

    String extId();

    Integer level();

    Set<? extends ProductCategory> children();

    List<? extends Product> products();

    ProductCategory parent();

    List<? extends Name> names();

    String description();

    Integer precedency();
    
    EProductCategoryType productCategoryType();
    
	List<? extends Tag> tags();

	Boolean visuallyDecorated();

	Boolean visuallySeparated();

	Boolean visuallyInteractive();

	Boolean provideSummary();

	Boolean contentCategory();

	boolean loadTagsFromParent();

	Long productsCount();
}