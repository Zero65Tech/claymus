package com.claymus.module.websitewidget.header;

import com.claymus.module.websitewidget.WebsiteWidgetHelper;
import com.claymus.module.websitewidget.header.gae.HeaderWidgetEntity;
import com.claymus.module.websitewidget.header.shared.HeaderWidgetData;

public class HeaderWidgetHelper extends WebsiteWidgetHelper<
		HeaderWidget,
		HeaderWidgetData,
		HeaderWidgetProcessor> {
	
	@Override
	public String getModuleName() {
		return "Header";
	}

	@Override
	public Double getModuleVersion() {
		return 4.0;
	}

	
	public static HeaderWidget newHeaderWidget() {
		return new HeaderWidgetEntity();
	}

}
