package com.aem.community.core.servlets;

import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@SuppressWarnings("serial")
@SlingServlet(resourceTypes = "sling/servlet/default", methods = "POST", selectors = { "register" }, extensions = { "json"}, metatype=true)
@Properties({
        @Property(name = "service.pid", value = "com.aem.community.core.servlets.RegistrationFormServlet", propertyPrivate = false),
        @Property(name = "service.vendor", value = "Fleetcor", propertyPrivate = false) })
public class RegistrationFormServlet extends SlingAllMethodsServlet {

	private static final Logger log = LoggerFactory.getLogger(RegistrationFormServlet.class);
	
    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
    
    @Override
    protected void doPost(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        
    	resp.setContentType("application/json");
    	PrintWriter out = resp.getWriter();
    	
    	String firstName = "";
    	String lastName = "";
    	
    	if(req.getParameter("firstName") != null){
    		firstName = req.getParameter("firstName");
    	}
    	
    	if(req.getParameter("lastName") != null){
    		lastName = req.getParameter("lastName");
    	}
    	
	    	
    	Map<String,String> dummyData = new HashMap<String,String>();
    	dummyData.put("name", firstName + " " + lastName);
    	dummyData.put("message", "Hello" + firstName + "! You are not eligible. Thanks.");
    	
    	Gson gson = new Gson();
	    String jsonString = gson.toJson(dummyData);    	
	    out.print(jsonString);
	    out.flush();
	    
	    resp.setStatus(200);
    }
}