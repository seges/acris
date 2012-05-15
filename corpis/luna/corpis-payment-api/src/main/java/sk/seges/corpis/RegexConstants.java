package sk.seges.corpis;

/**
 * Gathered regular expressions for common use cases.
 * 
 * @author ladislav.gazo
 */
public class RegexConstants {
	/** Based on http://www.regular-expressions.info/email.html */
	public static final String OPTIMAL_EMAIL = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	
	/** Based on http://flanders.co.nz/2009/11/08/a-good-url-regular-expression-repost */
	public static final String WEB_URL_WITHOUT_QUERY_PART = "^(?:(?:ht|f)tp(?:s?)\\:\\/\\/|~\\/|\\/)?(?:\\w+:\\w+@)?(?:(?:[-\\w]+\\.)+(?:com|org|net|gov|mil|biz|info|mobi|name|aero|jobs|museum|travel|[a-z]{2}))(?::[\\d]{1,5})?(?:(?:(?:\\/(?:[-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?(?:#(?:[-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?$";
	
	/** Based on http://flanders.co.nz/2009/11/08/a-good-url-regular-expression-repost */
	public static final String WEB_URL = "^(?:(?:ht|f)tp(?:s?)\\:\\/\\/|~\\/|\\/)?(?:\\w+:\\w+@)?(?:(?:[-\\w]+\\.)+(?:com|org|net|gov|mil|biz|info|mobi|name|aero|jobs|museum|travel|[a-z]{2}))(?::[\\d]{1,5})?(?:(?:(?:\\/(?:[-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?(?:(?:\\?(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?(?:[-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)(?:&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?(?:[-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*(?:#(?:[-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?$";
	
	/** Based on http://www.regular-expressions.info/examples.html */
	public static final String IP = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b";
}
