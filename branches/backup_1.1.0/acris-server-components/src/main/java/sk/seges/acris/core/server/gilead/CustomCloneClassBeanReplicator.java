package sk.seges.acris.core.server.gilead;

import java.lang.reflect.Modifier;

import net.sf.beanlib.hibernate.UnEnhancer;
import net.sf.beanlib.spi.BeanTransformerSpi;
import net.sf.beanlib.spi.replicator.BeanReplicatorSpi;
import net.sf.gilead.core.beanlib.clone.CloneClassBeanReplicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomCloneClassBeanReplicator extends CloneClassBeanReplicator {

	private static Logger _log = LoggerFactory.getLogger(CloneClassBeanReplicator.class);

	public static Factory factory = new Factory();

	public static class Factory implements BeanReplicatorSpi.Factory {

		private Factory() {
		}

		public CustomCloneClassBeanReplicator newBeanReplicatable(BeanTransformerSpi beanTransformer) {
			return new CustomCloneClassBeanReplicator(beanTransformer);
		}
	}

	public static CustomCloneClassBeanReplicator newBeanReplicatable(BeanTransformerSpi beanTransformer) {
		return factory.newBeanReplicatable(beanTransformer);
	}

	protected CustomCloneClassBeanReplicator(BeanTransformerSpi beanTransformer) {
		super(beanTransformer);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T extends Object> T createToInstance(Object from, java.lang.Class<T> toClass) throws InstantiationException, IllegalAccessException,
			SecurityException, NoSuchMethodException {
		//	Class mapper indirection
		//
		if (getClassMapper() != null) {
			//	Get target class
			//
			Class<T> targetClass = (Class<T>) getClassMapper().getTargetClass(from.getClass());

			//	Keep target class only if not null
			//
			if (targetClass != null) {
				if (_log.isDebugEnabled()) {
					_log.debug("Class mapper : from " + from.getClass() + " to " + targetClass);
				}
				toClass = targetClass;
			} else {
				if (_log.isDebugEnabled()) {
					_log.debug("Class mapper : no target class for " + from.getClass());
				}
			}

		}

        Class<T> actualClass = UnEnhancer.getActualClass(from);
        return super.newInstanceAsPrivileged(getTargetClass(actualClass, toClass));
	}

    protected final <T> Class<T> getTargetClass(Class<?> fromClass, Class<T> toClass) {
        if (Modifier.isAbstract(toClass.getModifiers())) {
            @SuppressWarnings("unchecked") Class<T> ret = (Class<T>)fromClass;
            return ret;
        }
        
        return toClass;
    }
}