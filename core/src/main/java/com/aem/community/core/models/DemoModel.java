package com.aem.community.core.models;

import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUse;
import com.adobe.cq.sightly.WCMUsePojo;

public class DemoModel extends WCMUsePojo {

    private static final Logger LOG = LoggerFactory.getLogger(DemoModel.class);

    private ValueMap properties;
	private String title;
	private String description;
	private String color;
	private String richText;
	
    public String getTitle() {
		return title;
	}
    
    public String getDescription() {
		return description;
	}
    
    public String getColor() {
		return color;
	}
    
    public String getRichText() {
		return richText;
	}
    
	@Override
	public void activate() throws Exception {
		properties = getProperties();
		title = properties.get("title", "Default Title");
		description = properties.get("description", "Default Description");
		color = properties.get("color","black");
		richText = properties.get("richtext","<p>hello demo</p>");
		
		title = title.toUpperCase();
		description = description.toUpperCase();
	}
}
