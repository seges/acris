package sk.seges.sesam.core.test.bromine.request;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import sk.seges.sesam.core.test.bromine.BromineCommand;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSettings;

public class BromineRequest {

	private SeleniumSettings seleniumEnvironment;
	
	public BromineRequest(SeleniumSettings seleniumEnvironment) {
		this.seleniumEnvironment = seleniumEnvironment;
	}

	private String getBromineHost() {
		if (seleniumEnvironment.getBrominePort() != 443) {
			return "http://" + seleniumEnvironment.getBromineServer() + ":" + seleniumEnvironment.getBrominePort();
		}

		return "https://" + seleniumEnvironment.getBromineServer();
	}
	
    public String send(BromineCommand command, String data) throws MalformedURLException, IOException {

		String requestUrl = getBromineHost();
		if (command != null) {
			requestUrl += "/brunit/" + command.getName();
		}

    	// Create connection
        URL url = new URL(requestUrl);
        URLConnection urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        
        // Send POST output.
        DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
        if (data != null) {
        	printout.writeBytes(data);
        }
        printout.flush();
        printout.close();
        
        // Get response data.
        BufferedReader input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        
        String str = "";        
        String result = "";
        
        while (null != ((str = input.readLine()))) {
            result += str + "\n\r";
        }
        
        input.close();
        
        return result;
    }
}