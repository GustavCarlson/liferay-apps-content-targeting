/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.content.targeting.portlet.lar;

import com.liferay.content.targeting.service.CampaignLocalService;
import com.liferay.content.targeting.service.UserSegmentLocalService;
import com.liferay.osgi.util.service.ServiceTrackerUtil;
import com.liferay.portal.kernel.lar.BasePortletDataHandler;
import com.liferay.portal.kernel.lar.DataLevel;
import com.liferay.portal.kernel.lar.PortletDataContext;

import javax.portlet.PortletPreferences;
import javax.portlet.UnavailableException;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Eduardo Garcia
 * @see    com.liferay.portal.kernel.lar.PortletDataHandler
 */
public class ContentTargetingPortletDataHandler extends BasePortletDataHandler {

	public ContentTargetingPortletDataHandler() {

		// Prevent displaying the Content Targeting as a stageable application

		setDataLevel(DataLevel.PORTLET_INSTANCE);
	}

	@Override
	protected PortletPreferences doDeleteData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences)
		throws Exception {

		if (portletDataContext.addPrimaryKey(
				ContentTargetingPortletDataHandler.class, "deleteData")) {

			return portletPreferences;
		}

		_initServices();

		_campaignLocalService.deleteCampaigns(
			portletDataContext.getScopeGroupId());
		_userSegmentLocalService.deleteUserSegments(
			portletDataContext.getScopeGroupId());

		return portletPreferences;
	}

	private void _initServices() throws UnavailableException {
		Bundle bundle = FrameworkUtil.getBundle(getClass());

		if (bundle == null) {
			throw new UnavailableException(
					"Can't find a reference to the OSGi bundle") {

				@Override
				public boolean isPermanent() {
					return true;
				}
			};
		}

		_campaignLocalService = ServiceTrackerUtil.getService(
			CampaignLocalService.class, bundle.getBundleContext());
		_userSegmentLocalService = ServiceTrackerUtil.getService(
			UserSegmentLocalService.class, bundle.getBundleContext());
	}

	private CampaignLocalService _campaignLocalService;
	private UserSegmentLocalService _userSegmentLocalService;

}