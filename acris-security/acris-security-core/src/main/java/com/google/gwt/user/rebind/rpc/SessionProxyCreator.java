/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.user.rebind.rpc;

import com.google.gwt.core.ext.typeinfo.*;
import com.google.gwt.dev.generator.NameFactory;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.FailedRequest;
import com.google.gwt.user.client.rpc.impl.FailingRequestBuilder;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter;
import com.google.gwt.user.rebind.SourceWriter;
import sk.seges.acris.security.client.session.SessionEnabledRemoteServiceProxy;

import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import sk.seges.acris.security.shared.MethodType;
import sk.seges.acris.security.shared.annotation.Method;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates a client-side proxy for a
 * {@link com.google.gwt.user.client.rpc.RemoteService RemoteService} interface
 * as well as the necessary type and field serializers.
 */
public class SessionProxyCreator extends ProxyCreator {

	public SessionProxyCreator(JClassType serviceIntf) {
		super(serviceIntf);
	}

	protected Class<? extends RemoteServiceProxy> getProxySupertype() {
		return SessionEnabledRemoteServiceProxy.class;
	}

	protected void generateProxyMethod(SourceWriter w, SerializableTypeOracle serializableTypeOracle,
									   TypeOracle typeOracle, JMethod syncMethod, JMethod asyncMethod) {

		w.println();

		// Write the method signature
		JType asyncReturnType = asyncMethod.getReturnType().getErasedType();
		w.print("public ");
		w.print(asyncReturnType.getQualifiedSourceName());
		w.print(" ");
		w.print(asyncMethod.getName() + "(");

		boolean needsComma = false;
		NameFactory nameFactory = new NameFactory();
		JParameter[] asyncParams = asyncMethod.getParameters();
		for (int i = 0; i < asyncParams.length; ++i) {
			JParameter param = asyncParams[i];

			if (needsComma) {
				w.print(", ");
			} else {
				needsComma = true;
			}

      /*
       * Ignoring the AsyncCallback parameter, if any method requires a call to
       * SerializationStreamWriter.writeObject we need a try catch block
       */
			JType paramType = param.getType();
			paramType = paramType.getErasedType();

			w.print(paramType.getQualifiedSourceName());
			w.print(" ");

			String paramName = param.getName();
			nameFactory.addName(paramName);
			w.print(paramName);
		}

		w.println(") {");
		w.indent();

		String helperName = nameFactory.createName("helper");
		String helperClassName = RemoteServiceProxy.ServiceHelper.class.getCanonicalName();
		w.println("%s %s = new %s(\"%s\", \"%s\");", helperClassName, helperName, helperClassName,
				getProxySimpleName(), syncMethod.getName());

		w.println("try {");
		w.indent();

		// Write the parameter count followed by the parameter values
		JParameter[] syncParams = syncMethod.getParameters();

		String streamWriterName = nameFactory.createName("streamWriter");
		w.println("%s %s = %s.start(REMOTE_SERVICE_INTERFACE_NAME, %s);",
				SerializationStreamWriter.class.getSimpleName(), streamWriterName, helperName,
				syncParams.length);

		for (JParameter param : syncParams) {
			JType paramType = param.getType().getErasedType();
			String typeNameExpression = computeTypeNameExpression(paramType);
			assert typeNameExpression != null : "Could not compute a type name for "
					+ paramType.getQualifiedSourceName();
			w.println(streamWriterName + ".writeString(" + typeNameExpression + ");");
		}

		// Encode all of the arguments to the asynchronous method, but exclude the
		// last argument which is the callback instance.
		//
		for (int i = 0; i < asyncParams.length - 1; ++i) {
			JParameter asyncParam = asyncParams[i];
			w.print(streamWriterName + ".");
			w.print(Shared.getStreamWriteMethodNameFor(asyncParam.getType()));
			w.println("(" + asyncParam.getName() + ");");
		}

    /*
     * Depending on the return type for the async method, return a
     * RequestBuilder, a Request, or nothing at all.
     */
		JParameter callbackParam = asyncParams[asyncParams.length - 1];
		JType returnType = syncMethod.getReturnType();
		String callbackName = callbackParam.getName();

		Method methodAnnotation = syncMethod.getAnnotation(Method.class);
		MethodType methodType = methodAnnotation == null ? MethodType.POST : methodAnnotation.value();

		w.println("setRpcRequestBuilder(%sBuilder);", methodType.name().toLowerCase());

		if (asyncReturnType == JPrimitiveType.VOID) {
			w.println("%s.finish(%s, ResponseReader.%s);", helperName, callbackName,
					getResponseReaderFor(returnType).name());
		} else if (asyncReturnType.getQualifiedSourceName().equals(RequestBuilder.class.getName())) {
			w.println("return %s.finishForRequestBuilder(%s, ResponseReader.%s);", helperName,
					callbackName, getResponseReaderFor(returnType).name());
		} else if (asyncReturnType.getQualifiedSourceName().equals(Request.class.getName())) {
			w.println("return %s.finish(%s, ResponseReader.%s);", helperName, callbackName,
					getResponseReaderFor(returnType).name());
		} else {
			// This method should have been caught by RemoteServiceAsyncValidator
			throw new RuntimeException("Unhandled return type "
					+ asyncReturnType.getQualifiedSourceName());
		}

		w.outdent();
		w.print("} catch (SerializationException ");
		String exceptionName = nameFactory.createName("ex");
		w.println(exceptionName + ") {");
		w.indent();
		if (!asyncReturnType.getQualifiedSourceName().equals(RequestBuilder.class.getName())) {
      /*
       * If the method returns void or Request, signal the serialization error
       * immediately. If the method returns RequestBuilder, the error will be
       * signaled whenever RequestBuilder.send() is invoked.
       */
			w.println(callbackName + ".onFailure(" + exceptionName + ");");
		}
		if (asyncReturnType.getQualifiedSourceName().equals(RequestBuilder.class.getName())) {
			w.println("return new " + FailingRequestBuilder.class.getName() + "(" + exceptionName + ", "
					+ callbackName + ");");
		} else if (asyncReturnType.getQualifiedSourceName().equals(Request.class.getName())) {
			w.println("return new " + FailedRequest.class.getName() + "();");
		} else {
			assert asyncReturnType == JPrimitiveType.VOID;
		}
		w.outdent();
		w.println("}");

		w.outdent();
		w.println("}");
	}

	private static final Map<JPrimitiveType, RequestCallbackAdapter.ResponseReader> JPRIMITIVETYPE_TO_RESPONSEREADER =
			new HashMap<JPrimitiveType, RequestCallbackAdapter.ResponseReader>();
	static {
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.BOOLEAN, RequestCallbackAdapter.ResponseReader.BOOLEAN);
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.BYTE, RequestCallbackAdapter.ResponseReader.BYTE);
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.CHAR, RequestCallbackAdapter.ResponseReader.CHAR);
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.DOUBLE, RequestCallbackAdapter.ResponseReader.DOUBLE);
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.FLOAT, RequestCallbackAdapter.ResponseReader.FLOAT);
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.INT, RequestCallbackAdapter.ResponseReader.INT);
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.LONG, RequestCallbackAdapter.ResponseReader.LONG);
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.SHORT, RequestCallbackAdapter.ResponseReader.SHORT);
		JPRIMITIVETYPE_TO_RESPONSEREADER.put(JPrimitiveType.VOID, RequestCallbackAdapter.ResponseReader.VOID);
	}

	private RequestCallbackAdapter.ResponseReader getResponseReaderFor(JType returnType) {
		if (returnType.isPrimitive() != null) {
			return JPRIMITIVETYPE_TO_RESPONSEREADER.get(returnType.isPrimitive());
		}

		if (returnType.getQualifiedSourceName().equals(String.class.getCanonicalName())) {
			return RequestCallbackAdapter.ResponseReader.STRING;
		}

		return RequestCallbackAdapter.ResponseReader.OBJECT;
	}
}
