package com.google.gwt.dev.javac.typemodel;


public class JMethodHelper extends JMethod {

	private JMethod method;

	public JMethodHelper(JMethod method) {
		super(method.getEnclosingType(), method);
		this.method = method;
	}

	public String getReadableDeclaration(String returnType, boolean noAccess, boolean noNative, boolean noStatic, boolean noFinal, boolean noAbstract) {
		int bits = getModifierBits();
		if (noAccess) {
			bits &= ~(TypeOracle.MOD_PUBLIC | TypeOracle.MOD_PRIVATE | TypeOracle.MOD_PROTECTED);
		}
		if (noNative) {
			bits &= ~TypeOracle.MOD_NATIVE;
		}
		if (noStatic) {
			bits &= ~TypeOracle.MOD_STATIC;
		}
		if (noFinal) {
			bits &= ~TypeOracle.MOD_FINAL;
		}
		if (noAbstract) {
			bits &= ~TypeOracle.MOD_ABSTRACT;
		}
		return getReadableDeclarationForBits(returnType, bits);
	}

	public String getReadableDeclaration(String returnType) {
		return getReadableDeclarationForBits(returnType, getModifierBits());
	}
	
	public String getReadableDeclarationForBits(String returnType, int bits) {
		String[] names = TypeOracle.modifierBitsToNames(bits);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < names.length; i++) {
			sb.append(names[i]);
			sb.append(" ");
		}
		if (method.getTypeParameters().length > 0) {
			method.toStringTypeParams(sb);
			sb.append(" ");
		}
		sb.append(returnType);
		sb.append(" ");
		sb.append(method.getName());

		method.toStringParamsAndThrows(sb);

		return sb.toString();
	}

	protected void toStringParamsAndThrows(StringBuilder sb, String paramType, int index) {
		sb.append("(");
		boolean needComma = false;
		for (int i = 0, c = method.getParameters().length; i < c; ++i) {
			JParameter param = method.getParameters()[i];
			if (needComma) {
				sb.append(", ");
			} else {
				needComma = true;
			}

			if (index == i) {
				sb.append(paramType);
			} else {
				if (isVarArgs() && i == c - 1) {
					com.google.gwt.core.ext.typeinfo.JArrayType arrayType = param.getType().isArray();
					assert (arrayType != null);
					sb.append(arrayType.getComponentType().getParameterizedQualifiedSourceName());
					sb.append("...");
				} else {
					sb.append(param.getType().getParameterizedQualifiedSourceName());
				}
			}
			sb.append(" ");
			sb.append(param.getName());
		}
		sb.append(")");

		if (method.getThrows().length > 0) {
			sb.append(" throws ");
			needComma = false;
			for (JClassType thrownType : method.getThrows()) {
				if (needComma) {
					sb.append(", ");
				} else {
					needComma = true;
				}
				sb.append(thrownType.getParameterizedQualifiedSourceName());
			}
		}
	}

	public String getReadableDeclaration(String paramType, int index) {
		String[] names = TypeOracle.modifierBitsToNames(getModifierBits());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < names.length; i++) {
			sb.append(names[i]);
			sb.append(" ");
		}
		if (method.getTypeParameters().length > 0) {
			method.toStringTypeParams(sb);
			sb.append(" ");
		}
		sb.append(method.getReturnType().getParameterizedQualifiedSourceName());
		sb.append(" ");
		sb.append(method.getName());

		toStringParamsAndThrows(sb, paramType, index);

		return sb.toString();
	}

}
