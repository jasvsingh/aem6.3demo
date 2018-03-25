package com.aem.community.core.dam;

import com.day.cq.commons.jcr.JcrUtil;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Implementation of DAMUpload Service
 */
@Component(metatype = true, immediate = true, label = "DAM Upload Service")
@Service(DamUploadService.class)
@Properties({
    @Property(name = "service.pid", value = "com.dbpost.dam.DamUploadService", propertyPrivate = false),
    @Property(name = "service.vendor", value = "Demo", propertyPrivate = false) })
public class DamUploadServiceImpl implements DamUploadService {

    private static final Logger log = LoggerFactory.getLogger(DamUploadService.class);
    
    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    
    @Reference
    private SlingRepository slingRepository;

    ResourceResolver resourceResolver;
    Session session; 
   
    /**
	 * Identifier for Sling Folder.
	 */
	private static final String SLING_FOLDER = "sling:Folder";
    
    @Activate
    protected final void activate(final Map<Object, Object> config) {
        log.info("Activated DAMUploadServiceImpl");
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "damUploadService");
        try {
			resourceResolver = resourceResolverFactory.getServiceResourceResolver(paramMap);
			session = resourceResolver.adaptTo(Session.class);
			log.info("Got resourceresolver and session :: " + session.getUserID());
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Deactivate
    protected void deactivate(ComponentContext ctx) throws Exception {
        log.info("Deactivated CheckinServiceImpl");
    }
    
    @Override
    public boolean uploadAsset(String fileName, String contentType, String uploadPath, InputStream fileInputStream) throws RepositoryException, IOException {
        session.refresh(true);
    	boolean status = false;
    	
        Node uploadNode = null;
        Resource uploadNodeResource = null;
        uploadNode = JcrUtil.createPath(uploadPath, SLING_FOLDER, session);
        log.info("Created DAM Upload Path :: " + uploadPath);
        session.save();
        
        uploadNodeResource = resourceResolver.getResource(uploadNode.getPath()); 
        
        if((uploadNodeResource != null) && (uploadNodeResource instanceof Resource)){  
        	//Upload XML File to the respective Folder
        	String uploadedFilePath = writeToDam(uploadPath, fileInputStream, fileName, contentType);
        	session.save();
        	
        	//if(StringUtils.isNotBlank(uploadedFilePath)) {
        	if(uploadedFilePath != ""){
        		status = true;
        		log.info("File Successfully Uploaded to DAM at Path :: " + uploadedFilePath);
        	}
        }
        
        return status;
    }

    private String writeToDam(String filePath, InputStream is, String fileName, String mimeType) {
    	String uploadedFilePath = "";
    	try {
	        //Use AssetManager to place the file into the AEM DAM
	        com.day.cq.dam.api.AssetManager assetMgr = resourceResolver.adaptTo(com.day.cq.dam.api.AssetManager.class);
	        assetMgr.createAsset(filePath + "/" + fileName, is, mimeType, true);
	             
	        // Return the path to the file was stored
	        uploadedFilePath =  filePath + "/" + fileName;
    	} catch(Exception e) {
    		e.printStackTrace();
        }
    	return uploadedFilePath;
    }
}