package sk.seges.sesam.pap.service.utils;

import java.util.List;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;

public class TypeUtils {

	public static boolean containsSameType(List<MutableTypeMirror> types, MutableTypeMirror type) {
		for (MutableTypeMirror listType: types) {
			if (listType.isSameType(type)) {
				return true;
			}
		}
		
		return false;
	}

}
