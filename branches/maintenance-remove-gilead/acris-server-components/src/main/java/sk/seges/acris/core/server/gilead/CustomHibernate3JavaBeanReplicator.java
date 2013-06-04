package sk.seges.acris.core.server.gilead;

import net.jcip.annotations.ThreadSafe;
import net.sf.beanlib.hibernate3.Hibernate3JavaBeanReplicator;
import net.sf.beanlib.spi.BeanTransformerSpi;
import net.sf.beanlib.spi.replicator.BeanReplicatorSpi;

public class CustomHibernate3JavaBeanReplicator extends Hibernate3JavaBeanReplicator {

	@ThreadSafe
	public static class Factory implements BeanReplicatorSpi.Factory {

		private Factory() {
		}

		public Hibernate3JavaBeanReplicator newBeanReplicatable(BeanTransformerSpi beanTransformer) {
			return new CustomHibernate3JavaBeanReplicator(beanTransformer);
		}
	}

	protected CustomHibernate3JavaBeanReplicator(BeanTransformerSpi beanTransformer) {
		super(beanTransformer);
	}

	@Override
	protected <T> T createToInstance(Object from, Class<T> toClass) throws InstantiationException, IllegalAccessException, SecurityException,
			NoSuchMethodException {
		return newInstanceAsPrivileged(toClass);
	}
}