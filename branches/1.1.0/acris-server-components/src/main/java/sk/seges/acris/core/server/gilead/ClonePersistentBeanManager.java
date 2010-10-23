package sk.seges.acris.core.server.gilead;

import net.sf.gilead.core.store.stateless.StatelessProxyStore;

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
