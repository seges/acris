/**
 * 
 */
package sk.seges.corpis.ie.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import sk.seges.corpis.ie.server.domain.RowBasedHandlerContext;
import sk.seges.corpis.ie.shared.domain.ImportExportViolation;
import sk.seges.corpis.ie.shared.domain.ViolationConstants;

/**
 * @author ladislav.gazo
 */
public class TextParseSupport {
	private final String defaultLocale;
	private final String enumOtherName;
	private Map<String, ResourceBundle> bundleCache;
	
	public TextParseSupport(String defaultLocale, String enumOtherName) {
		super();
		this.defaultLocale = defaultLocale;
		this.enumOtherName = enumOtherName;
	}

	public <T extends Enum<?>> T findEnumForMessage(RowBasedHandlerContext context,
			List<ImportExportViolation> violations, Class<T> clz, String message) {
		return findEnumForMessage(context, violations, clz, message, false);
	}
	
	private ResourceBundle getBundle(String bundle) {
		if(bundleCache == null) {
			bundleCache = new HashMap<String, ResourceBundle>();
		}
		ResourceBundle resourceBundle = bundleCache.get(bundle);
		if(resourceBundle == null) {
			resourceBundle = ResourceBundle.getBundle(bundle, new Locale(defaultLocale));
			bundleCache.put(bundle, resourceBundle);
		}
		return resourceBundle;
	}
	
	public <T extends Enum<?>> T findEnumForMessage(RowBasedHandlerContext context,
			List<ImportExportViolation> violations, Class<T> clz, String message, boolean noMatchIsOK) {
		T[] enumConstants = clz.getEnumConstants();

		try {
			ResourceBundle bundle = getBundle(clz.getName());

			String key;
			for(int i = 0; i < enumConstants.length; i++) {
				key = enumConstants[i].name();
				if (message.equals(bundle.getString(key))) {
					return enumConstants[i];
				}
			}

		} catch (Exception e) {
			if (!noMatchIsOK) {
				violations.add(new ImportExportViolation(context.getRow(), ViolationConstants.AMBIGOUS_VALUE,
						message));
			} else {
				// look for OTHER constant
				for (T constant : enumConstants) {
					if (constant.name().equals(enumOtherName)) {
						return constant;
					}
				}
				violations.add(new ImportExportViolation(context.getRow(), ViolationConstants.MISSING,
						enumOtherName));
			}
		}
		return null;
	}
	
	public String findMessageForEnum(Enum<?> enumeration) {
		if(enumeration == null) {
			return null;
		}
		ResourceBundle bundle = getBundle(enumeration.getClass().getName());
		return bundle.getString(enumeration.name());
	}
}
