package sk.seges.acris.site.server.hibernate.interceptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import sk.seges.acris.site.server.cache.CacheHandler;
import sk.seges.sesam.domain.IDomainObject;

/**
 * Created by PeterSimun on 25.11.2014.
 */
public class EntityInterceptor extends EmptyInterceptor {

    private final List<CacheHandler> cacheHandlers;

    public EntityInterceptor(CacheHandler... cacheHandlers) {
        this.cacheHandlers = new ArrayList<CacheHandler>();
        for (CacheHandler handler: cacheHandlers) {
            this.cacheHandlers.add(handler);
        }
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

    private void invalidate(Object entity) {

        if (!(entity instanceof IDomainObject) || ((IDomainObject<?>) entity).getId() == null) {
            //we cache only entities that implements IDomainObject
            return;
        }

        for (CacheHandler cacheHandler: cacheHandlers) {
            cacheHandler.invalidate(entity.getClass().getCanonicalName(), ((IDomainObject<?>) entity).getId().hashCode());
        }
    }
}