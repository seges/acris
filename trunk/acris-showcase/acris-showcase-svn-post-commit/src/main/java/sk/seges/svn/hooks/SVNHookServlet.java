package sk.seges.svn.hooks;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;

import sk.seges.svn.ProjectSettings;
import sk.seges.svn.json.reader.PayloadJSONReader;
import sk.seges.svn.mail.SVNMailSender;

public class SVNHookServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(SVNHookServlet.class.getName());

	private static final long serialVersionUID = -7084142257979312491L;

	// Stores the contents of the JSON object sent in the message from Google
	// Code
	private String payload;

	// Stores the hash value sent in the request headers from Google Code
	private String expectedHash;

	// The javax.crypto.SecretKey used to seed the HMAC-MD5 message
	private SecretKey secret;

	// Configuration properties from settings.properties
	private ProjectSettings projectConfiguration;
	
	public SVNHookServlet() throws IOException {
		super();
		// Assumption: Your Google Code project's secret key is stored in the
		// file referenced in the File() constructor below
		payload = "";
		expectedHash = null;
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {


		if (secret == null) {
			projectConfiguration = new ProjectSettings(getServletContext());
			secret = new GoogleCodeSecretKey(projectConfiguration.getAuthentication());
		}
		
		BufferedReader r = request.getReader();
		String line;
		while ((line = r.readLine()) != null)
			payload = payload.concat(line + "\n");
		r.close();
		payload = payload.trim();

		expectedHash = request
			.getHeader("Google-Code-Project-Hosting-Hook-Hmac");

//		payload = "{\"repository_path\":\"http://acris.googlecode.com/svn/\",\"project_name\":\"acris\",\"revisions\":[{\"added\":[],\"author\":\"simun@seges.sk\",\"url\":\"http://acris.googlecode.com/svn-history/r34/\",\"timestamp\":1259923581,\"modified\":[\"/test/test.file\"],\"path_count\":1,\"message\":\"test\",\"removed\":[],\"revision\":34}],\"revision_count\":1}";
		
		// The expected hash result, as sent in the request headers by Google
		expectedHash = request
				.getHeader("Google-Code-Project-Hosting-Hook-Hmac");

		// Validate the request and write the results; you'll want to do
		// something
		// much more exciting based on the results of the validate() method
		String result;
		if (validate()) {			
			PayloadJSONReader payloadJSONReader = new PayloadJSONReader();
			SVNMailSender mailSender = new SVNMailSender(projectConfiguration, payloadJSONReader.readFromJSON(payload)); 
			mailSender.sendMail();

			result = "Message authenticated successfully!";
		} else {
			log.info("Authentication failed. Remote host: " + request.getRemoteHost());
            result = "Message authentication failed!";
		}
		response.getWriter().write(result);
		response.getWriter().close();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	/**
	 * Compute the payload's HMAC-MD5 hash value and compare it to the expected
	 * value received in request headers from Google
	 * 
	 * @return True if the payload's hash value is equal to the expected value
	 *         stored as a member object of this instance
	 */
	private boolean validate() {
		if (expectedHash == null)
			return false;

		try {
			Mac mac = Mac.getInstance("HmacMD5");
			mac.init(secret);
			// I'm using Apache Commons codec lib to do the hex encoding; use
			// whatever hex encoder lib you like
			return expectedHash.equals(new String(Hex.encodeHex(mac.doFinal(payload.getBytes(Charset.forName("UTF-8"))))));
		} catch (KeyException e) {
			e.printStackTrace(); // Presumably, this stack trace will be dumped
									// to your J2EE container's log files
			return false;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
	}
}
