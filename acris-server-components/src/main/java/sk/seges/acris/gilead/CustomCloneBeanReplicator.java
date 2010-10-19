package sk.seges.acris.gilead;

import net.sf.beanlib.hibernate.HibernateBeanReplicator;
import net.sf.beanlib.hibernate3.Hibernate3BeanTransformer;
import net.sf.beanlib.hibernate3.Hibernate3BlobReplicator;
import net.sf.beanlib.hibernate3.Hibernate3MapReplicator;
import net.sf.beanlib.spi.BeanTransformerSpi;
import net.sf.beanlib.spi.CustomBeanTransformerSpi;
import net.sf.gilead.core.IPersistenceUtil;
import net.sf.gilead.core.beanlib.IClassMapper;
import net.sf.gilead.core.beanlib.clone.CloneClassBeanReplicator;
import net.sf.gilead.core.beanlib.clone.CloneCollectionReplicator;
import net.sf.gilead.core.beanlib.clone.ClonePropertyFilter;
import net.sf.gilead.core.beanlib.finder.FastPrivateReaderMethodFinder;
import net.sf.gilead.core.beanlib.finder.FastPrivateSetterMethodCollector;
import net.sf.gilead.core.beanlib.transformer.CustomTransformersFactory;
import net.sf.gilead.core.store.IProxyStore;

public class CustomCloneBeanReplicator extends HibernateBeanReplicator {

	public CustomCloneBeanReplicator(IClassMapper classMapper, IPersistenceUtil persistenceUtil, IProxyStore proxyStore) {
		super(newBeanTransformer(classMapper, persistenceUtil, proxyStore));
	}

	private static Hibernate3BeanTransformer newBeanTransformer(IClassMapper classMapper, IPersistenceUtil persistenceUtil, IProxyStore proxyStore) {
		CustomHibernate3BeanTransformer transformer = new CustomHibernate3BeanTransformer();

		// Custom collection replicator
		transformer.initCollectionReplicatableFactory(CloneCollectionReplicator.factory);

		// Set associated PersistenceUtil
		((CloneCollectionReplicator) transformer.getCollectionReplicatable()).setPersistenceUtil(persistenceUtil);

		transformer.initMapReplicatableFactory(Hibernate3MapReplicator.getFactory());
		transformer.initBlobReplicatableFactory(Hibernate3BlobReplicator.getFactory());

		// Custom bean replicatable
		transformer.initBeanReplicatableFactory(CustomCloneClassBeanReplicator.factory);

		// Set the associated class mapper
		((CloneClassBeanReplicator) transformer.getBeanReplicatable()).setClassMapper(classMapper);
		((CloneClassBeanReplicator) transformer.getBeanReplicatable()).setPersistenceUtil(persistenceUtil);

		//  Custom transformers (timestamp handling)
		//
		transformer.initCustomTransformerFactory(new CustomBeanTransformerSpi.Factory() {

			public CustomBeanTransformerSpi newCustomBeanTransformer(final BeanTransformerSpi beanTransformer) {
				return CustomTransformersFactory.getInstance().createUnionCustomBeanTransformerForClone(beanTransformer);
			}
		});

		//	Lazy properties handling
		//
		transformer.initDetailedPropertyFilter(new ClonePropertyFilter(persistenceUtil, proxyStore));

		//	Protected and private setter collection
		//
		transformer.initSetterMethodCollector(new FastPrivateSetterMethodCollector());
		transformer.initReaderMethodFinder(new FastPrivateReaderMethodFinder());

		return transformer;
	}

}
