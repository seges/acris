package sk.seges.corpis.server.domain.invoice;

import java.util.List;
import java.util.Set;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.Name;
import sk.seges.corpis.shared.domain.invoice.EProductCategoryType;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface ProductCategory extends IDomainObject<Long> {

    String extId();

    Integer level();

    Set<ProductCategory> children();

    List<Product> products();

    ProductCategory parent();

    List<Name> names();

    String description();

    Integer precedency();
    
    String webId();
   
    EProductCategoryType productCategoryType();
}