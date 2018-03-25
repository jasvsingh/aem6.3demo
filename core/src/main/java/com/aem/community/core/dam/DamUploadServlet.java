package com.aem.community.core.dam;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SlingServlet(paths="/bin/dam/uploadAsset", methods = "POST", metatype=true)
@Properties({
        @Property(name = "service.pid", value = "com.dbpost.dam.DAMUploadServlet", propertyPrivate = false),
        @Property(name = "service.vendor", value = "Demo", propertyPrivate = false) })
public class DamUploadServlet extends SlingAllMethodsServlet{

	private static final long serialVersionUID = 2598426537366789515L;
    
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Identifier for Request Parameter File.
	 */
	private static final String REQUEST_PARAMETER_FILE = "file";
	/**
	 * Identifier for Request Parameter Folder Path.
	 */
	private static final String REQUEST_UPLOAD_PATH = "uploadPath";
	
	//@Reference
	//private DamUploadService damUploadService;
	
    @Reference
    private SlingRepository repository;
    
    public void bindRepository(SlingRepository repository) {
        this.repository = repository; 
    }
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

    }
    
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
    	
    	try {
    		log.info("Inside DO POST of Servlet");
    		//Fetch DAM Upload Path from Request
    		String uploadPath = "";
    		if(request.getParameter(REQUEST_UPLOAD_PATH) != null) {
    			uploadPath = request.getParameter(REQUEST_UPLOAD_PATH);
    		}
    		
    		//Fetch File from Request
    		RequestParameter rp = request.getRequestParameter("file");
    		
    		if (rp == null || rp.getSize()==0 || rp.isFormField()) {
                log.error("Unable to proceed with file upload.  Please specify the file with the parameter '{}' or the file has a size of 0.  Aborting file upload", REQUEST_PARAMETER_FILE);
                response.setStatus(400);
                response.getWriter().println("Unable to proceed with file upload.  " + 
                		"Please specify the file with the parameter "
                		+ REQUEST_PARAMETER_FILE + 
                		" or the file has a size of 0.  Aborting file upload");
                return;
            } else {
            	response.getWriter().println("Found file...");
            }

    		boolean status = false;
    		String fileName = rp.getFileName();
    		//status = damUploadService.uploadAsset(fileName, rp.getContentType(), uploadPath, rp.getInputStream());
            
    		if (status) {
            	response.setStatus(200);
                response.getWriter().println("File Upload Successfull");
            } else {
            	log.info("Unable to upload file: {} ", rp.getFileName());
                response.setStatus(400);
                response.getWriter().println("File Upload Unsuccessfull");
            }
        } catch (Exception e) {
    		response.getWriter().println("ERROR MESSAGE : " + e.getMessage());
    	}
    }
}