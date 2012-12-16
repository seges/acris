package com.saf.remote.exception;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;


/**
 * Helper class to resolve MQSeries reason codes
 * (c) 2005, Eugene Kuleshov. All rights reserved
 */
public class MQReasonCodeResolver {

    private static final Logger logger =
            Logger.getLogger(MQReasonCodeResolver.class.toString());
    public static final String MQ_EXCEPTION_CLASS =
            "com.ibm.mq.MQException";
    public static final String RC_FIELD_PREFIX = "MQRC_";
    public static final String RC_VALUE_FIELD = "reasonCode";

    /**
     * Resolve reson code from MQException
     * 
     * @param mqex MQException to resolve
     * @return symbolic name for reason code from MQException
     */
    public static String resolve(Exception mqex) {
        Class<?> c = mqex.getClass();
        try {
            Field f = c.getField(RC_VALUE_FIELD);
            Object key = f.get(mqex);
            Field[] fields = c.getFields();
            for (int i = 0; i < fields.length; i++) {
                Field ff = fields[i];
                String name = ff.getName();
                if (name.startsWith(RC_FIELD_PREFIX) &&
                        ff.get(null).equals(key)) {
                    return name;
                }
            }
        } catch (Exception ex) {
            logger.warn("Unable to resolve reason code; ", ex);
        }
        return "unknown";
    }

    /**
     * Verify if reason code can be resolved
     * 
     * @param mqex MQException to resolve
     * @return symbolic name for reason code from MQException
     */
    public static boolean canResolve(Exception exx) {
        Class<?> c = exx.getClass();
        while (Exception.class.isAssignableFrom(c)) {
            if (MQ_EXCEPTION_CLASS.equals(c.getName())) {
                return true;
            }
            c = c.getSuperclass();
        }
        return false;
    }
}