package sk.seges.corpis.shared.converter.utils;

import sk.seges.corpis.service.annotation.TransactionPropagation.PropagationTarget;
import sk.seges.corpis.service.annotation.TransactionPropagationModel;

public class ConverterUtils {

//	public static boolean hasTransaction(TransactionPropagationModel[] transactionPropagations) {
//		if (transactionPropagations != null && transactionPropagations.length == 1) {
//			return !transactionPropagations[0].getValue().equals(PropagationType.ISOLATE);
//		}
//		
//		return true;
//	}
	
	public static boolean convertResult(TransactionPropagationModel[] transactionPropagations, String field) {
		for (TransactionPropagationModel transactionPropagation: transactionPropagations) {
			for (PropagationTarget target: transactionPropagation.getTarget()) {
				if (target.equals(PropagationTarget.RETURN_VALUE)) {
					switch (transactionPropagation.getValue()) {
					case PROPAGATE:
						return (contains(transactionPropagation.getFields(), field));
					}
				}
			}
		}

		return false;
//		return convert(transactionPropagations, field, PropagationTarget.RETURN_VALUE);
	}

	public static boolean convertArg(TransactionPropagationModel[] transactionPropagations, String field) {
		return convert(transactionPropagations, field, PropagationTarget.ARGUMENTS);
	}
	
	private static boolean convert(TransactionPropagationModel[] transactionPropagations, String field, PropagationTarget propagationTarget) {
		for (TransactionPropagationModel transactionPropagation: transactionPropagations) {
			for (PropagationTarget target: transactionPropagation.getTarget()) {
				if (target.equals(propagationTarget)) {
					switch (transactionPropagation.getValue()) {
					case ISOLATE:
						if (transactionPropagation.getFields() == null || transactionPropagation.getFields().length == 0) {
							return false;
						}
						return !(contains(transactionPropagation.getFields(), field));
					case PROPAGATE:
						if (transactionPropagation.getFields() == null || transactionPropagation.getFields().length == 0) {
							return true;
						}
						return (contains(transactionPropagation.getFields(), field));
					}
				}
			}
		}

		return false;
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