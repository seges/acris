package sk.seges.acris.gilead;

import net.sf.beanlib.hibernate3.Hibernate3BeanTransformer;
import net.sf.beanlib.hibernate3.Hibernate3BlobReplicator;
import net.sf.beanlib.hibernate3.Hibernate3CollectionReplicator;
import net.sf.beanlib.hibernate3.Hibernate3MapReplicator;
import net.sf.beanlib.provider.BeanPopulator;
import net.sf.beanlib.spi.BeanPopulatorSpi;


public class CustomHibernate3BeanTransformer extends Hibernate3BeanTransformer {

    public CustomHibernate3BeanTransformer() {
        this(BeanPopulator.factory);
    }
    
    public CustomHibernate3BeanTransformer(BeanPopulatorSpi.Factory beanPopulatorFactory) {
        super(beanPopulatorFactory);
        this.initCollectionReplicatableFactory(
                Hibernate3CollectionReplicator.getFactory());
        this.initMapReplicatableFactory(
                Hibernate3MapReplicator.getFactory());
        this.initBlobReplicatableFactory(
                Hibernate3BlobReplicator.getFactory());
        this.initBeanReplicatableFactory(
                CustomHibernate3JavaBeanReplicator.getFactory());
    }
}
