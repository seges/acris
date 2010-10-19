package sk.seges.acris.gilead;

import net.sf.beanlib.hibernate.HibernateBeanReplicator;
import net.sf.gilead.core.LazyKiller;
import net.sf.gilead.core.beanlib.IClassMapper;

public class CloneLazyKiller extends LazyKiller {

	protected IClassMapper classMapper;
	
	public void setClassMapper(IClassMapper mapper) {
		super.setClassMapper(mapper);
		this.classMapper = mapper;
	}

	protected Object clone(Object hibernatePojo, Class<?> cloneClass) {
		HibernateBeanReplicator replicator = new CustomCloneBeanReplicator(classMapper, getPersistenceUtil(), getProxyStore());

		return replicator.copy(hibernatePojo, cloneClass);
	}
}
