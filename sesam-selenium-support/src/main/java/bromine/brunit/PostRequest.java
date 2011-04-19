/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bromine.brunit;

/**
 *
 * @author Jeppe Poss
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PostRequest {

    private String result;
    private URL url;
    private URLConnection urlConn;
    private DataOutputStream printout;
    private BufferedReader input;

    public String cmd(String data, String site) throws MalformedURLException, IOException {
        result = "";
        // Create connection
        url = new URL(site);
        urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        // Send POST output.
        printout = new DataOutputStream(urlConn.getOutputStream());
        printout.writeBytes(data);
        printout.flush();
        printout.close();
        // Get response data.
        input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        String str = "";
        while (null != ((str = input.readLine()))) {
            result += str + "\n\r";
        }
        input.close();
        return result;
    }
}

