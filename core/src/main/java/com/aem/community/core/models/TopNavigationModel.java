package com.aem.community.core.models;

import java.util.ArrayList;
import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.cq.sightly.WCMUsePojo;
import com.aem.community.core.objects.MenuItem;
import com.day.cq.wcm.api.Page;

public class TopNavigationModel  extends WCMUsePojo {

	private static final Logger log = LoggerFactory.getLogger(TopNavigationModel.class);
	
	private ValueMap properties;
	private List<MenuItem> menuList;
	
	public List<MenuItem> getMenuList() {
		return menuList;
	}
	
	@Override
	public void activate() throws Exception {
		
		String mode = get("mode", String.class);
		
		menuList = new ArrayList<MenuItem>();
		properties = getProperties();
		
		//Fetch MenuItems from Multifield
		Resource componentResource = getResource();
		Resource menuItemsResource = componentResource.getChild(mode);
		
		if((menuItemsResource != null) && menuItemsResource instanceof Resource) {
			Iterable<Resource> menuItemsChildren = menuItemsResource.getChildren();
			for(Resource menuItemResource : menuItemsChildren) {
				ValueMap menuItemProperties = menuItemResource.getValueMap();
				
				if(menuItemProperties != null) {
					String title = menuItemProperties.get("title","Dummy");					
					String url = menuItemProperties.get("url","");
					String flyoutPagePath = menuItemProperties.get("flyoutPagePath","");
					
					MenuItem menuItemObj = new MenuItem();
					menuItemObj.setTitle(title);
					menuItemObj.setUrl(url);
					
					List<MenuItem> flyoutMenuList = new ArrayList<MenuItem>();
					
					//Fetch Flyout Menu Details				
					Resource flyoutPageResource = getResourceResolver().getResource(flyoutPagePath);
					
					if((flyoutPageResource != null) && (flyoutPageResource instanceof Resource)) {
						Iterable<Resource> flyoutMenuItems = flyoutPageResource.getChildren();
						for(Resource flyoutMenuItem : flyoutMenuItems) {	
							if(!flyoutMenuItem.getName().equalsIgnoreCase("jcr:content")) {
								Page childPage = flyoutMenuItem.adaptTo(Page.class);
								ValueMap pageProperties = childPage.getProperties();
								String pageTitle = pageProperties.get("jcr:title","");
								String pageUrl = childPage.getPath();
								
								MenuItem flyoutMenuItemObj = new MenuItem();
								flyoutMenuItemObj.setTitle(pageTitle);
								flyoutMenuItemObj.setUrl(pageUrl);
								
								//We can fetch further submenu's for each item
								
								flyoutMenuList.add(flyoutMenuItemObj);
							}
						}
					}
					
					menuItemObj.setFlyoutMenuList(flyoutMenuList);
					
					menuList.add(menuItemObj);
				} 
			}
		}
	}
}