package sk.seges.corpis.shared.converter.utils;

import sk.seges.corpis.service.annotation.TransactionPropagation.PropagationType;

public class ConverterUtils {

	public static boolean convert(PropagationType propagationType, String[] fields, String field) {
		switch (propagationType) {
		case ISOLATE:
			if (fields == null || fields.length == 0) {
				return false;
			}
			return !(contains(fields, field));
		case PROPAGATE:
			if (fields == null || fields.length == 0) {
				return true;
			}
			return (contains(fields, field));
		}
		
		throw new RuntimeException("Unsupported propagation type: " + propagationType);
	}
	
	private static boolean contains(String[] fields, String field) {
		for (String f: fields) {
			if (f.equals(field)) {
				return true;
			}
		}
		
		return false;
	}

}