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

package com.liferay.content.targeting.rule.organization.member;

import com.liferay.content.targeting.anonymous.users.model.AnonymousUser;
import com.liferay.content.targeting.api.model.BaseRule;
import com.liferay.content.targeting.api.model.Rule;
import com.liferay.content.targeting.model.RuleInstance;
import com.liferay.content.targeting.rule.categories.UserAttributesRuleCategory;
import com.liferay.content.targeting.util.ContentTargetingContextUtil;
import com.liferay.content.targeting.util.PortletKeys;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.OrganizationConstants;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Eudaldo Alonso
 */
@Component(immediate = true, service = Rule.class)
public class OrganizationMemberRule extends BaseRule {

	@Activate
	@Override
	public void activate() {
		super.activate();
	}

	@Deactivate
	@Override
	public void deActivate() {
		super.deActivate();
	}

	@Override
	public boolean evaluate(
			HttpServletRequest request, RuleInstance ruleInstance,
			AnonymousUser anonymousUser)
		throws Exception {

		long organizationId = GetterUtil.getLong(
			ruleInstance.getTypeSettings());

		return UserLocalServiceUtil.hasOrganizationUser(
			organizationId, anonymousUser.getUserId());
	}

	@Override
	public String getIcon() {
		return "icon-globe";
	}

	@Override
	public String getRuleCategoryKey() {
		return UserAttributesRuleCategory.KEY;
	}

	@Override
	public String getSummary(RuleInstance ruleInstance, Locale locale) {
		try {
			long organizationId = GetterUtil.getLong(
				ruleInstance.getTypeSettings());

			Organization organization =
				OrganizationLocalServiceUtil.fetchOrganization(organizationId);

			if (organization == null) {
				return StringPool.BLANK;
			}

			return organization.getName();
		}
		catch (SystemException e) {
		}

		return StringPool.BLANK;
	}

	@Override
	public String processRule(
		PortletRequest request, PortletResponse response, String id,
		Map<String, String> values) {

		return values.get("organizationId");
	}

	@Override
	protected void populateContext(
		RuleInstance ruleInstance, Map<String, Object> context,
		Map<String, String> values) {

		long organizationId = 0;

		if (!values.isEmpty()) {
			organizationId = GetterUtil.getLong(values.get("organizationId"));
		}
		else if (ruleInstance != null) {
			organizationId = GetterUtil.getLong(ruleInstance.getTypeSettings());
		}

		context.put("organizationId", organizationId);

		Company company = (Company)context.get("company");

		List<Organization> organizations = new ArrayList<Organization>();

		try {

			// See LPS-50218

			organizations = OrganizationLocalServiceUtil.getOrganizations(
				company.getCompanyId(),
				OrganizationConstants.ANY_PARENT_ORGANIZATION_ID);
		}
		catch (SystemException e) {
		}

		context.put("organizations", organizations);

		if ((organizations == null) || organizations.isEmpty()) {
			boolean hasUsersAdminViewPermission =
				ContentTargetingContextUtil.
					hasControlPanelPortletViewPermission(
						context, PortletKeys.USERS_ADMIN);

			if (hasUsersAdminViewPermission) {
				context.put(
					"usersAdminURL",
					ContentTargetingContextUtil.getControlPanelPortletURL(
						context, PortletKeys.USERS_ADMIN, null));
			}
		}
	}

}