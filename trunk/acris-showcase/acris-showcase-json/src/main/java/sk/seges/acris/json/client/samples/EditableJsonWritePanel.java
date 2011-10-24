package sk.seges.acris.json.client.samples;

public class EditableJsonWritePanel extends WritePanel {
	
	private String JSON_VALUE_DELIM = "\"";
	
	private String REPLACE = "%%";
	
	private String json = "";
	
	public void setJson(String json) {
		
		int startIndex = 0;
		
		while (true) {
			int start = json.indexOf(":" + JSON_VALUE_DELIM, startIndex);
			
			if (start != -1) {
				int end = json.indexOf(JSON_VALUE_DELIM, start + 2);
				
				if (end != -1) {
					String temp = json.substring(startIndex, start + 1);
					this.json += temp + "\"";
					write(temp).writeb("\"");
					temp = json.substring(start + 2, end);
					this.json += REPLACE + temp + REPLACE + "\"";
					addWidgetb(temp).writeb("\"");
					startIndex = end + 1;
				} else {
					break;
				}
			} else {
				break;
			}
		}
		
		String temp = json.substring(startIndex);
		this.json += temp;
		write(temp);
	}
	
	public String getJson() {
		int startIndex = 0;

		String result = "";
		
		while (true) {
			int start = json.indexOf(REPLACE, startIndex);
			
			if (start != -1) {
				int end = json.indexOf(REPLACE, start + REPLACE.length());

				if (end != -1) {
					String temp = json.substring(startIndex, start);
					result += temp;
					temp = json.substring(start + REPLACE.length(), end);
					result += getWidgetText(temp);
					
					startIndex = end + REPLACE.length();
				} else {
					break;
				}
			} else {
				break;
			}
		}

		String temp = json.substring(startIndex);
		result += temp;

		return result;
	}
}