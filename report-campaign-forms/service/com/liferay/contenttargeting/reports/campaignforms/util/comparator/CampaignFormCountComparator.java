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

package com.liferay.contenttargeting.reports.campaignforms.util.comparator;

import com.liferay.contenttargeting.reports.campaignforms.model.CampaignForm;
import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * @author Eduardo Garcia
 */
public class CampaignFormCountComparator extends OrderByComparator {

	public static final String ORDER_BY_ASC = "CampaignForm.count ASC";

	public static final String ORDER_BY_DESC = "CampaignForm.count DESC";

	public static final String[] ORDER_BY_FIELDS = {"count"};

	public CampaignFormCountComparator() {
		this(false);
	}

	public CampaignFormCountComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		CampaignForm campaignContent1 = (CampaignForm)obj1;
		CampaignForm campaignContent2 = (CampaignForm)obj2;

		int count1 = campaignContent1.getCount();
		int count2 = campaignContent2.getCount();

		int value = 0;

		if (count1 < count2) {
			value = -1;
		}
		else if (count1 > count2) {
			value = 1;
		}

		if (_ascending) {
			return value;
		}
		else {
			return -value;
		}
	}

	@Override
	public String getOrderBy() {
		if (_ascending) {
			return ORDER_BY_ASC;
		}
		else {
			return ORDER_BY_DESC;
		}
	}

	@Override
	public String[] getOrderByFields() {
		return ORDER_BY_FIELDS;
	}

	@Override
	public boolean isAscending() {
		return _ascending;
	}

	private boolean _ascending;

}