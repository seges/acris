package sk.seges.acris.site.server.hibernate.interceptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.seges.acris.site.server.cache.CacheHandler;
import sk.seges.sesam.domain.IDomainObject;

/**
 * Created by PeterSimun on 25.11.2014.
 */
public class EntityInterceptor extends EmptyInterceptor {

	private final ThreadPoolExecutor threadPool;
    private final List<CacheHandler> cacheHandlers;
    private boolean disabledInvalidation = false;
    private static final Logger LOG = LoggerFactory.getLogger(EntityInterceptor.class);

    public EntityInterceptor(CacheHandler... cacheHandlers) {
        this.cacheHandlers = new ArrayList<CacheHandler>();
        for (CacheHandler handler: cacheHandlers) {
            this.cacheHandlers.add(handler);
        }
        this.threadPool = new ThreadPoolExecutor(5, 200, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    	invalidate(entity);
        super.onDelete(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
    	invalidate(entity);
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    	invalidate(entity);
        return super.onSave(entity, id, state, propertyNames, types);
    }

	@Override
	public void onCollectionRemove(Object collection, Serializable key) throws CallbackException {
		invalidateCollection(collection);
		super.onCollectionRemove(collection, key);
	}

	@Override
	public void onCollectionRecreate(Object collection, Serializable key) throws CallbackException {
		invalidateCollection(collection);
		super.onCollectionRecreate(collection, key);
	}

	@Override
	public void onCollectionUpdate(Object collection, Serializable key) throws CallbackException {
		invalidateCollection(collection);
		super.onCollectionUpdate(collection, key);
	}

    private void invalidateCollection(Object collection) {
    	if(collection instanceof Iterable<?>){
    		for(Object entity : (Iterable<?>)collection){
    			invalidate(entity);
    		}
    	}
    }
    
    private void invalidate(final Object entity) {
    	LOG.debug("Invalidate entity : " + entity.getClass().getName());    	
    	if (!(entity instanceof IDomainObject) || ((IDomainObject<?>) entity).getId() == null || disabledInvalidation) {
    		LOG.debug("disabledInvalidation: " + disabledInvalidation);
    		LOG.debug("Cancel invalidation");
            //we cache only entities that implements IDomainObject
            return;
        }
    	LOG.debug("Invalidate entity with id: "+ ((IDomainObject<?>) entity).getId());
    	LOG.debug("cacheHandlers : " + cacheHandlers);
        for (final CacheHandler cacheHandler: cacheHandlers) {
        	LOG.debug("creating new runnable ..."); 
        	Runnable runnable = new Runnable() {
				@Override
				public void run() {
					LOG.debug("run thread to invalidate for cachehandler : " + cacheHandler);
					cacheHandler.invalidate(entity.getClass().getCanonicalName(), ((IDomainObject<?>) entity).getId().hashCode());
				}
        	};
        	LOG.debug("execute runnable : " + runnable);
        	LOG.debug("threadPool : " + threadPool.toString());
        	LOG.debug("threadPool.getActiveCount : " + threadPool.getActiveCount());
        	LOG.debug("threadPool.getCompletedTaskCount : " + threadPool.getCompletedTaskCount());
        	LOG.debug("threadPool.getQueue : " + threadPool.getQueue());
        	LOG.debug("threadPool.getRejectedExecutionHandler : " + threadPool.getRejectedExecutionHandler());
        	threadPool.execute(runnable);
        }
    }
    
    public void clearCache(String webId, String locale){
    	for (CacheHandler cacheHandler: cacheHandlers) {
    		cacheHandler.clearCache(webId, locale);
    	}
    }
    
    public boolean isDisabledInvalidation() {
		return disabledInvalidation;
	}

	public void setDisabledInvalidation(boolean disabledInvalidation) {
		this.disabledInvalidation = disabledInvalidation;
	}
}