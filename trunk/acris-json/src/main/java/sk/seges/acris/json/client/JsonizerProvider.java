package sk.seges.acris.json.client;

import org.gwttime.time.DateTime;

import sk.seges.acris.json.client.adapter.DateTimeAdapter;
import sk.seges.acris.json.client.adapter.StringAdapter;
import sk.seges.acris.json.client.provider.JsonDataAdapterProvider;

public class JsonizerProvider {

	public static void init() {
		JsonDataAdapterProvider.registerAdapter(String.class, new StringAdapter());
		JsonDataAdapterProvider.registerAdapter(DateTime.class, new DateTimeAdapter());
	}
}
