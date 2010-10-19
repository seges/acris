package sk.seges.acris.rpc;

import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import sk.seges.acris.gilead.CloneLazyKiller;

public class ClonePersistentBeanManager extends PersistentBeanManager {

	/**
	 * Empty Constructor
	 */
	public ClonePersistentBeanManager() {
		//	Default parameters
		//
		_proxyStore = new StatelessProxyStore();

		_lazyKiller = new CloneLazyKiller();
		_lazyKiller.setProxyStore(_proxyStore);
	}

	@Override
	public Object clone(Object object, boolean assignable) {
		return super.clone(object, false);
	}
	
	@Override
	public Object merge(Object object, boolean assignable) {
		return super.merge(object, false);
	}
}
