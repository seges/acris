package sk.seges.acris.asmant.server.rest;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import sk.seges.acris.asmant.server.domain.twig.TwigAlarmRecipient;
import sk.seges.acris.asmant.server.domain.twig.TwigSite;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

/**
 * @author ladislav.gazo
 */
@Path("/site")
public class SiteResource {
	private static final Logger log = Logger.getLogger(SiteResource.class.getName());

	private ObjectDatastore dataStore = new AnnotationObjectDatastore();
	
	@Path("/")
	@POST
	public void addSite(@FormParam("name") String name, @FormParam("url") String url, @FormParam("method") String method, @FormParam("headers") String headers, @FormParam("payload") String payload) {
		TwigSite site = new TwigSite();
		site.setName(name);
		site.setUrl(url);
		if(method !=null && !method.isEmpty()) {
			site.setMethod(method.toUpperCase());
		} else {
			site.setMethod(HTTPMethod.GET.name());
		}
		if(payload !=null && !payload.isEmpty()) {
			site.setPayload(payload);
		}
		if(headers !=null && !headers.isEmpty()) {
			site.setHeaders(headers);
		}
		
		Key store = dataStore.store(site);
		log.fine("Site saved = " + site + ", key = " + store);
	}
	
	@Path("/recipient")
	@POST
	public void addRecipient(@FormParam("name") String name, @FormParam("email") String email) {
		TwigAlarmRecipient recipient = new TwigAlarmRecipient();
		recipient.setEmail(email);
		recipient.setName(name);
		
		Key store = dataStore.store(recipient);
		log.fine("Recipient saved = " + recipient + ", key = " + store);
	}
	
	@Path("/check")
	@GET
	public String check() {
		return doStatus(new StatusAction() {
			
			@Override
			public void execute(TwigSite site, HTTPResponse response) {
				int responseCode = response.getResponseCode();
				if(responseCode == 404) {
					send(site, response);
					log.info("Site is DOWN = " + site + ", response = " + response.getResponseCode());
				} else {
					log.info("Site is UP = " + site + ", response = " + response.getResponseCode());
				}
			}
		});
	}
	
	@Path("/status")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<String> status() {
		final List<String> statuses = new ArrayList<String>();
		doStatus(new StatusAction() {
			@Override
			public void execute(TwigSite site, HTTPResponse response) {
				statuses.add("[" + site.getName() + "] " + site.getUrl() + " | " + response.getResponseCode());
			}
		});
		return statuses;
	}
	
	public String doStatus(StatusAction action) {
		QueryResultIterator<TwigSite> iterator = dataStore.find().type(TwigSite.class).returnResultsNow();
		while(iterator.hasNext()) {
			TwigSite site = iterator.next();
			URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
			try {
				HTTPResponse response;
				URL url = new URL(site.getUrl());
				String headers = site.getHeaders();
				if(headers != null || site.getPayload() != null) {
					HTTPRequest request = new HTTPRequest(url, HTTPMethod.valueOf(site.getMethod()));
					if(site.getPayload() != null) {
						request.setPayload(site.getPayload().getBytes());
					}
					if(headers != null) {
						String[] split = headers.split("\\|\\|");
						for(int i = 0; i < split.length; i++) {
							HTTPHeader header = new HTTPHeader(split[i], split[++i]);
							request.addHeader(header);
						}
					}
					response = fetcher.fetch(request);
				} else {
					response = fetcher.fetch(url);
				}

//				byte[] content = response.getContent();
				// if redirects are followed, this returns the final URL we are redirected to
//				URL finalUrl = response.getFinalUrl();

				// 200, 404, 500, etc
				action.execute(site, response);
			} catch (IOException e) {
				log.log(Level.SEVERE, "Error while connecting to site  = " + site, e);
			}
		}
		return null;
	}
	
	private void send(TwigSite site, HTTPResponse response) {
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = "Site " + site.toString() + " is down because:\n" + response.getResponseCode();

        try {
            Message msg = new MimeMessage(session);
//            msg.setFrom(new InternetAddress("ladislav.gazo@asmant-monitor.appspotmail.com", "Asmant"));
            msg.setFrom(new InternetAddress("devel@seges.sk", "Asmant"));
            QueryResultIterator<TwigAlarmRecipient> iterator = dataStore.find().type(TwigAlarmRecipient.class).returnResultsNow();
            while(iterator.hasNext()) {
            	TwigAlarmRecipient recipient = iterator.next();
            	msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress(recipient.getEmail(), recipient.getName()));
            }
            msg.setSubject("[asmant] Site is down - " + site.getName());
            msg.setText(msgBody);
            Transport.send(msg);
    
        } catch (Exception e) {
			log.log(Level.SEVERE, "Error while sending email for to site  = " + site + ", response = " + response.getResponseCode(), e);
        }
	}
	
	private interface StatusAction {
		void execute(TwigSite site, HTTPResponse response);
	}
}
