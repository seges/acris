package sk.seges.sesam.core.test.bromine.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

public class QueryHelper {

    @SuppressWarnings("unchecked")
	public static String toQueryString(Map<String, ?> data, String oldKey) throws UnsupportedEncodingException {
        StringBuffer queryString = new StringBuffer();

        for (Entry<String, ?> pair : data.entrySet()) {
            if (pair.getValue() instanceof Hashtable) {
                queryString.append(toQueryString((Hashtable<String, ?>) pair.getValue(), URLEncoder.encode((String) pair.getKey(), "UTF-8") + "%5B"));
                queryString.append("&");
            } else if (!oldKey.isEmpty()) {
                queryString.append(oldKey);
                queryString.append(URLEncoder.encode((String) pair.getKey(), "UTF-8") + "%5D=");
                queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8") + "&");
            } else {
                queryString.append(URLEncoder.encode((String) pair.getKey(), "UTF-8") + "=");
                queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8") + "&");
            }
        }

        if (queryString.substring(queryString.length() - 1).equals("&")) {
            queryString.deleteCharAt(queryString.length() - 1);
        }

        return queryString.toString();
    }
}