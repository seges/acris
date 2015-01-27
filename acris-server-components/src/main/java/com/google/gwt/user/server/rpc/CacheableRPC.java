package com.google.gwt.user.server.rpc;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.impl.CacheableServerSerializationStreamWriter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PeterSimun on 22.11.2014.
 *
 * Copied methods from RPC and updated encode request methods to use CacheableServerSerializationStreamWriter
 */
public class CacheableRPC {

    /**
     * Maps primitive wrapper classes to their corresponding primitive class.
     */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS =
            new HashMap<Class<?>, Class<?>>();

    static {
        PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.put(Boolean.class, Boolean.TYPE);
        PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.put(Byte.class, Byte.TYPE);
        PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.put(Character.class, Character.TYPE);
        PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.put(Double.class, Double.TYPE);
        PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.put(Float.class, Float.TYPE);
        PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.put(Integer.class, Integer.TYPE);
        PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.put(Long.class, Long.TYPE);
        PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.put(Short.class, Short.TYPE);
    }

    public static String encodeResponseForFailure(Method serviceMethod, Throwable cause,
                                                  SerializationPolicy serializationPolicy, int flags) throws SerializationException {
        if (cause == null) {
            throw new NullPointerException("cause cannot be null");
        }

        if (serializationPolicy == null) {
            throw new NullPointerException("serializationPolicy");
        }

        if (serviceMethod != null && !RPCServletUtils.isExpectedException(serviceMethod, cause)) {
            throw new UnexpectedException("Service method '" + getSourceRepresentation(serviceMethod)
                    + "' threw an unexpected exception: " + cause.toString(), cause);
        }

        return encodeResponse(cause.getClass(), cause, true, flags, serializationPolicy);
    }

    public static String encodeResponseForSuccess(Method serviceMethod, Object object,
                                                  SerializationPolicy serializationPolicy, int flags) throws SerializationException {
        if (serviceMethod == null) {
            throw new NullPointerException("serviceMethod cannot be null");
        }

        if (serializationPolicy == null) {
            throw new NullPointerException("serializationPolicy");
        }

        Class<?> methodReturnType = serviceMethod.getReturnType();
        if (methodReturnType != void.class && object != null) {
            Class<?> actualReturnType;
            if (methodReturnType.isPrimitive()) {
                actualReturnType = getPrimitiveClassFromWrapper(object.getClass());
            } else {
                actualReturnType = object.getClass();
            }

            if (actualReturnType == null || !methodReturnType.isAssignableFrom(actualReturnType)) {
                throw new IllegalArgumentException("Type '" + printTypeName(object.getClass())
                        + "' does not match the return type in the method's signature: '"
                        + getSourceRepresentation(serviceMethod) + "'");
            }
        }

        return encodeResponse(methodReturnType, object, false, flags, serializationPolicy);
    }

    /**
     * Returns a string that encodes the results of an RPC call. Private overload
     * that takes a flag signaling the preamble of the response payload.
     *
     * @param object the object that we wish to send back to the client
     * @param wasThrown if true, the object being returned was an exception thrown
     *          by the service method; if false, it was the result of the service
     *          method's invocation
     * @return a string that encodes the response from a service method
     * @throws SerializationException if the object cannot be serialized
     */
    private static String encodeResponse(Class<?> responseClass, Object object, boolean wasThrown,
                                         int flags, SerializationPolicy serializationPolicy) throws SerializationException {

        CacheableServerSerializationStreamWriter stream =
                new CacheableServerSerializationStreamWriter(serializationPolicy);
        stream.setFlags(flags);

        stream.prepareToWrite();
        if (responseClass != void.class) {
            stream.serializeValue(object, responseClass);
        }

        return (wasThrown ? "//EX" : "//OK") + stream.toString();
    }

    /**
     * Returns the {@link java.lang.Class Class} for a primitive type given its
     * corresponding wrapper {@link java.lang.Class Class}.
     *
     * @param wrapperClass primitive wrapper class
     * @return primitive class
     */
    private static Class<?> getPrimitiveClassFromWrapper(Class<?> wrapperClass) {
        return PRIMITIVE_WRAPPER_CLASS_TO_PRIMITIVE_CLASS.get(wrapperClass);
    }

    /**
     * Returns the source representation for a method signature.
     *
     * @param method method to get the source signature for
     * @return source representation for a method signature
     */
    private static String getSourceRepresentation(Method method) {
        return method.toString().replace('$', '.');
    }

    private static String printTypeName(Class<?> type) {
        // Primitives
        //
        if (type.equals(Integer.TYPE)) {
            return "int";
        } else if (type.equals(Long.TYPE)) {
            return "long";
        } else if (type.equals(Short.TYPE)) {
            return "short";
        } else if (type.equals(Byte.TYPE)) {
            return "byte";
        } else if (type.equals(Character.TYPE)) {
            return "char";
        } else if (type.equals(Boolean.TYPE)) {
            return "boolean";
        } else if (type.equals(Float.TYPE)) {
            return "float";
        } else if (type.equals(Double.TYPE)) {
            return "double";
        }

        // Arrays
        //
        if (type.isArray()) {
            Class<?> componentType = type.getComponentType();
            return printTypeName(componentType) + "[]";
        }

        // Everything else
        //
        return type.getName().replace('$', '.');
    }

    /**
     * Static classes have no constructability.
     */
    private CacheableRPC() {
        // Not instantiable
    }
}