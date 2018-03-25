package com.aem.community.core.servlets;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aem.community.core.objects.Product;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@SuppressWarnings("serial")
@SlingServlet(resourceTypes = "com.aem.communitydemo/structure/page")
public class FetchProductDataServlet extends SlingAllMethodsServlet {

	private static final Logger log = LoggerFactory.getLogger(FetchProductDataServlet.class);
	
	@Reference
	private ResourceResolverFactory resolverFactory;
	 
	ResourceResolver resourceResolver;     	     
	
	private static final String PRODUCTS_FOLDER_PATH = "/etc/commerce/products";
	
	List<Product> sellableProductsList;
	
    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        
    	resp.setContentType("application/json");
    	PrintWriter out = resp.getWriter();
    	
    	resourceResolver = req.getResourceResolver();
    	
    	Resource productsResource = resourceResolver.getResource(PRODUCTS_FOLDER_PATH);
    	
    	if((productsResource != null) && (productsResource instanceof Resource)){
	    	Iterable<Resource> productTypes = productsResource.getChildren();
	    	
	    	sellableProductsList = new ArrayList<Product>();
	    	
	    	for(Resource productType : productTypes){
	    		List<Product> productList = getSellableProducts(productType);
	    		sellableProductsList.addAll(productList);
	    	}
	    	
	    	Gson gson = new Gson();
	    	String jsonString = gson.toJson(sellableProductsList);    	
	    	out.print(jsonString);
	    	out.flush();
    	}
    }
    
    private List<Product> getSellableProducts(Resource resource){
        List<Product> sellableProducts = new ArrayList<Product>();
        Product product = null;
        
        Iterable<Resource> productResources = resource.getChildren();
        for(Resource productResource : productResources){
            ValueMap productProperties = productResource.adaptTo(ValueMap.class);  
        	String availabilityStatus = productProperties.get("availabilityStatus","none");
        	
        	if(availabilityStatus.equalsIgnoreCase("sellable")) {
        		//Fetch other properties
        		
        		product = new Product();
        		//Set properties in Product Object
        		
        		sellableProducts.add(product);
        	}             
        }
          
        return sellableProducts;
    }
}