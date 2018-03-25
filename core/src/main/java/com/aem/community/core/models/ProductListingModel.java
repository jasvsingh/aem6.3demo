package com.aem.community.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.cq.sightly.WCMUsePojo;
import com.aem.community.core.objects.Product;
import com.day.cq.wcm.api.Page;

public class ProductListingModel extends WCMUsePojo {

    private static final Logger log = LoggerFactory.getLogger(ProductListingModel.class);

    private ValueMap properties;
    private String sectionTitle;
    private String parentPagePath;
    private String descriptionShow;
	private List<Product> productList;
	
    public List<Product> getProductList() {
		return productList;
	}
    
    public String getSectionTitle() {
		return sectionTitle;
	}
    public String getDescriptionShow() {
		return descriptionShow;
	}
    
	@Override
	public void activate() throws Exception {
		try {
			String mode = get("mode", String.class);
			
			properties = getProperties();
			sectionTitle = properties.get("sectionTitle", "Default Title");
			descriptionShow = properties.get("descriptionShow", "Default description");
			parentPagePath = properties.get("parentPagePath", getCurrentPage().getPath());
			
			productList = new ArrayList<Product>();
			
			if(mode.equalsIgnoreCase("auto")) {
				log.info("Inside AUTO");
				Resource parentPageResource = getResourceResolver().getResource(parentPagePath);
				
				Iterable<Resource> childResources = parentPageResource.getChildren();
				
				for (Resource childResource : childResources) {
					Page childPage = childResource.adaptTo(Page.class);
					if(childPage != null){
						ValueMap pageProperties = childPage.getProperties();
						String pageTitle = pageProperties.get("jcr:title","Default Title");
						String pageDesc = pageProperties.get("jcr:description","Default Description");
						String url = childResource.getPath();
						String subTitle = pageProperties.get("subtitle","Default Subtitle");
						String price = pageProperties.get("price","0.00");
						String image = pageProperties.get("image","default image");
						
						Product productObj = new Product();
						productObj.setTitle(pageTitle);
						productObj.setDescription(pageDesc);
						productObj.setUrl(url);
						productObj.setSubtitle(subTitle);
						productObj.setPrice(price);
						productObj.setImage(image);
								
						productList.add(productObj);
					}
				}
			} else if(mode.equalsIgnoreCase("manual")) {
				Resource componentResource = getResource();
				Resource productsResource = componentResource.getChild("products");
				
				if((productsResource != null) && productsResource instanceof Resource) {
					Iterable<Resource> productsChildren = productsResource.getChildren();
					for(Resource productResource : productsChildren) {
						ValueMap productProperties = productResource.getValueMap();
					
						if(productProperties != null) {
							String pageTitle = productProperties.get("title","Default Title");
							String pageDesc = productProperties.get("description","Default Description");
							String url = productProperties.get("url","/content/dam");
							String subTitle = productProperties.get("subtitle","Default Subtitle");
							String price = productProperties.get("price","0.00");
							String image = productProperties.get("image","/content/dam");							
							
							Product productObj = new Product();
							productObj.setTitle(pageTitle);
							productObj.setDescription(pageDesc);
							productObj.setUrl(url);
							productObj.setSubtitle(subTitle);
							productObj.setPrice(price);
							productObj.setImage(image);
							
							productList.add(productObj);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Exception in ProductListingModel :: " + e.getMessage());
		}
	}
}
