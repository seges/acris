package bromine.brunit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import com.thoughtworks.selenium.DefaultSelenium;

/*
 * @Author Jeppe Poss
 * @Date 12 june 2009
 * @Description replacement for the normal unittest. Updates the database to
 * reflect the correct assertments
 */
public abstract class BRUnit {

    private String host, browser, sitetotest, test_id;
    private int port;
    protected DefaultSelenium selenium;
    private String requesturl;
    private PostRequest pr;

    /*
     * Initiates the test script, used to elevate the logic from the actual
     * test script.
     * @param args - Array containing params passed to the test script
     * @param c - The class of the test script
     */
    public static void initiate(String[] args, String serverhost, int serverport, Class c) {
        
        try {
        	if (args[0].equals("bromine.local.seges.sk")) {
        		args[0] = "local.seges.sk/bromine";
        	}
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            String brows = args[2];
            String sitetotest = args[3];
            String test_id = args[4];
//            String brows2 = URLDecoder.decode(brows, "UTF-8");
//            brows2 += ';' + test_id;
            BRUnit obj = (BRUnit) c.newInstance();
            obj.setUp(host, port);
            obj.start(serverhost, serverport, brows, sitetotest, test_id);
            try {
                obj.runTests();
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                obj.customCommand("An Exception occured in the test", "failed", sw.toString(), "");
            }
            obj.tearDown();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /*
     * Setups up the default selenium object and creates the connection for the database
     * @param host - ip of the node
     * @param port - port which the nodes run on
     * @param browser - which browser to execute test in
     * @param sitetotest - the url of the test site
     * @param test_id - the tests id used by Bromine
     */
    public void setUp(String bromineHost, int brominePort) {
        this.host = bromineHost;
        this.port = brominePort;
        pr = new PostRequest();
    }
    
    public void start(String host, int port, String browser, String sitetotest, String test_id) {
        this.browser = browser;
        this.sitetotest = sitetotest;
        this.test_id = test_id;
        selenium = new DefaultSelenium(host, port, browser, sitetotest);
        selenium.start();
    }

    /*
     * Abstract method used for the test script tests. Method created since the
     * method name is hardcoded into the initiate method.
     */
    public abstract void runTests() throws Exception;

    /*
     * Teardown the selenium object
     */
    public void tearDown() throws Exception {
        selenium.stop();
    }

    public String toQueryString(Map<?, ?> data, String oldKey) throws UnsupportedEncodingException {
        StringBuffer queryString = new StringBuffer();
        for (Entry<?, ?> pair : data.entrySet()) {
            if (pair.getValue() instanceof Hashtable<?, ?>) {
                queryString.append(toQueryString((Hashtable<?, ?>) pair.getValue(), URLEncoder.encode((String) pair.getKey(), "UTF-8") + "%5B"));
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

    public String bromine_call(String function_name, Hashtable<String, String> function_params) throws Exception {
        String result = "";
        try {
            function_params.put("test_id", this.test_id);
            Hashtable<String, Object> data = new Hashtable<String, Object>();
            Enumeration e = function_params.keys();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                Object value = function_params.get(key);
                Hashtable nested = new Hashtable<String, Object>();
                String name = value.getClass().getCanonicalName();
                int typeIndex = name.lastIndexOf(".");
                String type = name.substring(typeIndex + 1);
                if(type.equals("Boolean")){
                    String b = "not";
                    if(((Boolean) value) == false){
                        b = "0";
                    } else{
                        b = "1";
                    }
                    nested.put("value", b);
                }else if(type.equals("String")){
                    nested.put("value", (String) value);
                }
                nested.put("type", type);
                
                data.put(key, nested);
            }
            String query = toQueryString(data, "");
            if (this.port != 443) {
            	requesturl = "http://" + this.host + ":" + this.port + "/brunit/" + function_name;
            } else {
            	requesturl = "https://" + this.host + "/brunit/" + function_name;
            }
            result = pr.cmd(query, requesturl);
            if (result.indexOf("OK") != 0) {
                this.tearDown();
                throw new Exception("BRUnit controller returned error:" + result + " in function " + function_name);
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            this.tearDown();
            throw new Exception("BRUnit controller returned error:" + result + " in function " + function_name);
        }
    }

    public void customCommand(String action, String status, String statement1, String statement2) throws Exception{
        customCommand(action, status, statement1, statement2, "");
    }

    public void customCommand(String action, String status, String statement1, String statement2, String comment) throws Exception {
        Hashtable params = new Hashtable();
        params.put("action", action);
        params.put("statement1", statement1);
        params.put("statement2", statement2);
        params.put("status", status);
        params.put("comment", comment);
        String result = this.bromine_call("customCommand", params);
    }

    private void assertNotFailed(String result) throws Exception {

        String[] result2 = result.split(",");
        if (result2[1].indexOf("failed") == 0) {
            this.customCommand("Assert failed", "failed", "Assertion failed. Test stopped!", "");
            this.tearDown();
            System.exit(-1);
        }
    }

    public void assertTrue(Boolean statement1) throws Exception{
        assertTrue(statement1, "");
    }

    /*
     * Assert the param is TRUE and updates command status and action in the database
     * @param $bool repression to verify
     */
    public void assertTrue(Boolean statement1, String comment) throws Exception {
        Hashtable params = new Hashtable();
        params.put("statement1", statement1);
        params.put("comment", comment);
        String result = this.bromine_call("assertTrue", params);
        this.assertNotFailed(result);

    }

     public void assertFalse(Boolean statement1) throws Exception{
         assertFalse(statement1, "");
     }

    /*
     * Assert the param is FALSE and updates command status and action in the database
     * @param $bool repression to verify
     */
    public void assertFalse(Boolean statement1, String comment) throws Exception {
        Hashtable params = new Hashtable();
        params.put("statement1", statement1);
        params.put("comment", comment);
        String result = this.bromine_call("assertFalse", params);
        this.assertNotFailed(result);
    }

    public void assertEquals(String statement1, String statement2) throws Exception{
        assertEquals(statement1, statement2, "");
    }

    /*
     * Assert the params is EQUAL and updates command status and action in the database
     * @param $var1 first param to verify
     * @param $var2 second param to verify
     */
    public void assertEquals(String statement1, String statement2, String comment) throws Exception {
        Hashtable params = new Hashtable();
        params.put("statement1", statement1);
        params.put("statement2", statement2);
        params.put("comment", comment);
        String result = this.bromine_call("assertEquals", params);
        this.assertNotFailed(result);
    }

    public void assertNotEquals(String statement1, String statement2) throws Exception{
        assertNotEquals(statement1, statement2, "");
    }

    /*
     * Assert the params is NOT EQUAL and updates command status and action in the database
     * @param $var1 first param to verify
     * @param $var2 second param to verify
     */
    public void assertNotEquals(String statement1, String statement2, String comment) throws Exception {
        Hashtable params = new Hashtable();
        params.put("statement1", statement1);
        params.put("statement2", statement2);
        params.put("comment", comment);
        String result = this.bromine_call("assertNotEquals", params);
        this.assertNotFailed(result);
    }

    public void verifyTrue(Boolean statement1) throws Exception{
        verifyTrue(statement1, "");
    }

    /*
     * Assert the param is TRUE and updates command status and action in the database
     * @param $bool repression to verify
     */
    public void verifyTrue(Boolean statement1, String comment) throws Exception {
        Hashtable params = new Hashtable();
        params.put("statement1", statement1);
        params.put("comment", comment);
        String result = this.bromine_call("verifyTrue", params);
    }

    public void verifyFalse(Boolean statement1) throws Exception{
        verifyFalse(statement1, "");
    }

    /*
     * Assert the param is FALSE and updates command status and action in the database
     * @param $bool repression to verify
     */
    public void verifyFalse(Boolean statement1, String comment) throws Exception {
        Hashtable params = new Hashtable();
        params.put("statement1", statement1);
        params.put("comment", comment);
        String result = this.bromine_call("verifyFalse", params);
    }

    public void verifyEquals(String statement1, String statement2) throws Exception{
        verifyEquals(statement1, statement2, "");
    }

    /*
     * Assert the params is EQUAL and updates command status and action in the database
     * @param $var1 first param to verify
     * @param $var2 second param to verify
     */
    public void verifyEquals(String statement1, String statement2, String comment) throws Exception {
        Hashtable params = new Hashtable();
        params.put("statement1", statement1);
        params.put("statement2", statement2);
        params.put("comment", comment);
        String result = this.bromine_call("verifyEquals", params);
    }

     public void verifyNotEquals(String statement1, String statement2) throws Exception{
         verifyNotEquals(statement1, statement2, "");
     }

    /*
     * Assert the params is NOT EQUAL and updates command status and action in the database
     * @param $var1 first param to verify
     * @param $var2 second param to verify
     */
    public void verifyNotEquals(String statement1, String statement2, String comment) throws Exception {
        Hashtable params = new Hashtable();
        params.put("statement1", statement1);
        params.put("statement2", statement2);
        params.put("comment", comment);
        String result = this.bromine_call("verifyNotEquals", params);
    }

    public void fail(String msg) throws Exception {
        throw new Exception(msg);
    }

    public void waiting() throws Exception {
        this.ignore();
    }

    public void ignore() throws Exception {
        Hashtable params = new Hashtable();
        params.put("action", "waiting");
        params.put("comment", "");
        String result = this.bromine_call("waiting", params);
    }
}
