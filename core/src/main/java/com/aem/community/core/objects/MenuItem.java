package com.aem.community.core.objects;

import java.util.List;

public class MenuItem {
	
	private String title;
	private String url;
	private List<MenuItem> flyoutMenuList;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<MenuItem> getFlyoutMenuList() {
		return flyoutMenuList;
	}
	public void setFlyoutMenuList(List<MenuItem> flyoutMenuList) {
		this.flyoutMenuList = flyoutMenuList;
	}
}
