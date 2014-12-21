package com.google.gwt.user.server.rpc.mock;

import java.util.HashMap;

/**
 * Created by PeterSimun on 1.12.2014.
 */
public class PositiveSerializationMap extends HashMap<Class<?>, Boolean> {

    @Override
    public Boolean get(Object key) {
        return true;
    }
}
