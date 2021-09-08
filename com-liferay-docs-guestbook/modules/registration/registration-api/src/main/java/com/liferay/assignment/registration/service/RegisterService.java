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

package com.liferay.assignment.registration.service;

import com.liferay.assignment.registration.model.Register;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.security.access.control.AccessControlled;
import com.liferay.portal.kernel.service.BaseService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Transactional;

import java.text.ParseException;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Provides the remote service interface for Register. Methods of this
 * service are expected to have security checks based on the propagated JAAS
 * credentials because this service can be accessed remotely.
 *
 * @author tarun
 * @see RegisterServiceUtil
 * @generated
 */
@AccessControlled
@JSONWebService
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface RegisterService extends BaseService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add custom service methods to <code>com.liferay.assignment.registration.service.impl.RegisterServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface. Consume the register remote service via injection or a <code>org.osgi.util.tracker.ServiceTracker</code>. Use {@link RegisterServiceUtil} if injection and service tracking are not available.
	 */
	public Register addRegister(
			long groupId, String first_name, String last_name,
			String email_address, String user_name, String birthday,
			String gender, String password2, long home_phone, Long mobile_phone,
			String address, String address2, String city, String state,
			String zip, String security_question, String security_answer,
			boolean accepted_tou, ServiceContext serviceContext)
		throws PortalException, ParseException;

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public String getOSGiServiceIdentifier();

}