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

package com.liferay.assignment.registration.service.impl;

import com.liferay.assignment.registration.exception.RegisterEntryAddressException;
import com.liferay.assignment.registration.exception.RegisterEntryCityException;
import com.liferay.assignment.registration.exception.RegisterEntryEmailException;
import com.liferay.assignment.registration.exception.RegisterEntryFirstNameException;
import com.liferay.assignment.registration.exception.RegisterEntryLastNameException;
import com.liferay.assignment.registration.exception.RegisterEntryPasswordException;
import com.liferay.assignment.registration.exception.RegisterEntryPhoneException;
import com.liferay.assignment.registration.exception.RegisterEntryStateException;
import com.liferay.assignment.registration.exception.RegisterEntryUsernameException;
import com.liferay.assignment.registration.exception.RegisterEntryZipException;
import com.liferay.assignment.registration.exception.RegisterEntrysecurityAnswerException;
import com.liferay.assignment.registration.model.Register;
import com.liferay.assignment.registration.service.base.RegisterLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.osgi.service.component.annotations.Component;

/**
 * The implementation of the register local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.assignment.registration.service.RegisterLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author tarun
 * @see RegisterLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.assignment.registration.model.Register",
	service = AopService.class
)
public class RegisterLocalServiceImpl extends RegisterLocalServiceBaseImpl {

	
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use <code>com.liferay.assignment.registration.service.RegisterLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.assignment.registration.service.RegisterLocalServiceUtil</code>.
	 */
	
	public Register addRegister(long groupId, String first_name, String last_name, String email_address, String user_name,
			String birthday, String gender, String password2, long home_phone, Long mobile_phone, String address, String address2,
			String city, String state, String zip, String security_question, String security_answer, boolean accepted_tou, ServiceContext serviceContext) throws PortalException, ParseException {
		
		
		validate(first_name, last_name, email_address, user_name, birthday, gender, password2, home_phone, mobile_phone, 
				address, address2, city, state, zip, security_question, security_answer, accepted_tou);
		
		//Group group = groupLocalService.getGroup(groupId);
		
		Group group= serviceContext.getScopeGroup();
		long userId = serviceContext.getUserId();
		
		User user = userLocalService.getUser(userId);
		
		// Generate a primary key for the new register
		long registerId = counterLocalService.increment(Register.class.getName());
		
		// Create the assignment using the new primary key
		Register register = createRegister(registerId);
		
		
		
		// Populate field values to the new register object
		
					register.setCompanyId(group.getCompanyId());
					register.setCreateDate(serviceContext.getCreateDate(new Date()));
					register.setGroupId(groupId);
					register.setModifiedDate(serviceContext.getModifiedDate(new Date()));
					register.setUserId(userId);
					register.setUserName(user.getScreenName());
					
		//			custom fields
					register.setFirst_name(first_name);
					register.setLast_name(last_name);
					register.setEmail_address(email_address);
					register.setUser_name(user_name);
					register.setBirthday(new Date());
					register.setGender(gender);
					register.setPassword2(password2);
					register.setHome_phone(home_phone);
					register.setMobile_phone(mobile_phone);
					register.setAddress(address);
					register.setAddress2(address2);
					register.setCity(city);
					register.setState(state);
					register.setZip(zip);
					register.setSecurity_question(security_question);
					register.setSecurity_answer(security_answer);
					register.setAccepted_tou(accepted_tou);
					
					
				
				// Persist it to database
				register= super.addRegister(register);
		
		
		return register;
		
		
	}
	protected boolean ageCalculator(String birthdate) throws ParseException {
		
		
		  SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
		  Date d = sdf.parse(birthdate);
		  Calendar c = Calendar.getInstance();
		  c.setTime(d);
		  int year = c.get(Calendar.YEAR);
		  int month = c.get(Calendar.MONTH) + 1;
		  int date = c.get(Calendar.DATE);
		  LocalDate l1 = LocalDate.of(year, month, date);
		  LocalDate now1 = LocalDate.now();
		  Period diff1 = Period.between(l1, now1);
		  
		  System.out.println("age:" + diff1.getYears() + "years");
		  if(diff1.getYears()<13)
			  return true;
		  else
			  return false;	
	}
	
	protected void validate(String first_name, String last_name, String email_address, String user_name,
			String birthday, String gender, String password2, long home_phone1, Long mobile_phone1, String address, 
			String address2,String city, String state, String zip, String security_question, String security_answer, 
			boolean accepted_tou) throws PortalException, ParseException {
		
			   String home_phone= String.valueOf(home_phone1);
			   
			   String mobile_phone= String.valueOf(mobile_phone1);
		
				String regex = "^[a-zA-Z0-9]+$";
				 
				Pattern pattern = Pattern.compile(regex);		
				
					
				//Validation for First Name
				if(Validator.isNull(first_name)) {
					
					throw new RegisterEntryFirstNameException("First Name is empty, Please enter the First Name ");
				}		
					else if (first_name.length()>50){
						throw new RegisterEntryFirstNameException("First Name length exceeds than 50 ");
					}
		
					else if(pattern.matcher(first_name).matches()){
						throw new RegisterEntryFirstNameException(" Please enter alphabet values only ");
						
				}
				
				//Validation for Last Name
				if(Validator.isNull(last_name)) {
							
							throw new RegisterEntryLastNameException("Last Name is empty, Please enter the Last Name ");
						}		
							else if (last_name.length()>50){
								throw new RegisterEntryLastNameException("Last Name length exceeds than 50 ");
							}
				
							else if(pattern.matcher(last_name).matches()){
								throw new RegisterEntryLastNameException(" Please enter alphabet values only ");
								
						}
				
				//Validation for Email Address
				if(Validator.isNull(email_address)) {
						
						throw new RegisterEntryEmailException("Email address is empty, Please enter the Email address ");
					}		
						else if (email_address.length()>255){
							throw new RegisterEntryEmailException("Email address length exceeds than 255 ");
						}
			
						else if(Validator.isEmailAddress(email_address)){
							throw new RegisterEntryEmailException(" Please enter valid email address ");
							
					}
					
				////Validation for Username
				if(Validator.isNull(user_name)) {
							
							throw new RegisterEntryUsernameException("Username is empty, Please enter the Username ");
						}		
							else if (user_name.length()>3 && user_name.length()<17){
								throw new RegisterEntryUsernameException("Username length lies between 4 and 16 ");
							}
				
							else if(Validator.isAlphanumericName(user_name)){
								throw new RegisterEntryUsernameException(" Please enter valid username ");
								
						}
				
				//Validate for Birthday
				//dd/mm/yyyy format

				regex = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((?:19|20)[0-9][0-9])";
				pattern = Pattern.compile(regex);
				
				if (Validator.isNull(birthday)) {
					throw new RegisterEntryPasswordException("Birthdate is empty, Please enter the Birthdate ");
				}
				else if (pattern.matcher(birthday).matches()) {
					throw new RegisterEntryPasswordException(" Please enter date in dd/mm/yyyy format ");
		
				}
				else if (ageCalculator(birthday)){
					throw new RegisterEntryPasswordException("You must be at least 13 years old for registration ");
				}
				
				
				//Validate for Password
				regex = "(/^(?=.*\\d)(?=.*[!@#$%^&*])(?=.*[A-Z]).{6,}$/";
				pattern =Pattern.compile(regex);
				
				if(Validator.isNull(password2)) {
					
					throw new RegisterEntryPasswordException("Password is empty, Please enter the Password ");
				}		
				else if (password2.length()<6){
						throw new RegisterEntryPasswordException("Password length should be greater than 5 ");
					}
				else if(pattern.matcher(password2).matches()){
						throw new RegisterEntryPasswordException(" Password Policy:- Minimum 6 characters, must contain one uppercase, one number, one special character. Please enter the valid password ");
						
				}
		
				//Validation for Home Number
				if (Validator.isPhoneNumber(home_phone)){
								throw new RegisterEntryPhoneException("Please enter valid Home Phone Number");
							}
				
				else if(home_phone.length()<11){
								throw new RegisterEntryPhoneException(" Please enter 10 digit Home Phone Number");
								
						}
				
				
				//Validation for Mobile Number
				if (Validator.isPhoneNumber(mobile_phone)){
								throw new RegisterEntryPhoneException("Please enter valid Mobile Phone Number");
						}
				
				else if(mobile_phone.length()<11){
								throw new RegisterEntryPhoneException(" Please enter 10 digit Mobile Phone Number");
								
						}
				
				//Validation for Address
				if(Validator.isNull(address)) {
						
						throw new RegisterEntryEmailException("Email address is empty, Please enter the address ");
					}		
						else if (address.length()>255){
							throw new RegisterEntryAddressException("Address length exceeds than 255 ");
						}
			
						else if(Validator.isAlphanumericName(address)){
							throw new RegisterEntryAddressException(" Please enter valid address ");
							
					}
				//Validation for Address2
				if (address2.length()>255){
							throw new RegisterEntryAddressException("Address length exceeds than 255 ");
						}
			
						else if(Validator.isAlphanumericName(address2)){
							throw new RegisterEntryAddressException(" Please enter valid address ");
							
					}
				//Validation for Address
				if(Validator.isNull(city)) {
						
						throw new RegisterEntryCityException("City is empty, Please enter the city ");
					}		
						else if (city.length()>255){
							throw new RegisterEntryCityException("City length exceeds than 255 ");
						}
			
						else if(Validator.isAlphanumericName(city)){
							throw new RegisterEntryCityException(" Please enter valid  City ");
							
					}
				
				//Validation for state
				if(Validator.isNull(state)) {
					
					throw new RegisterEntryStateException("state is empty, Please enter the state ");
				}		
					else if (state.length()>255){
						throw new RegisterEntryStateException("State length exceeds than 255 ");
					}
		
					else if(Validator.isAlphanumericName(state)){
						throw new RegisterEntryStateException(" Please enter valid  State ");
						
				}
				
				//Validation for state
				if(Validator.isNull(state)) {
					
					throw new RegisterEntryStateException("state is empty, Please enter the state ");
				}		
					else if (state.length()>255){
						throw new RegisterEntryStateException("State length exceeds than 255 ");
					}
		
					else if(Validator.isAlphanumericName(state)){
						throw new RegisterEntryStateException(" Please enter valid  State ");
						
				}
				
				//Validation for zip code
				if(Validator.isNull(zip)) {
					
					throw new RegisterEntryZipException("Zip code is empty, Please enter the zip code");
				}		
					else if (Validator.isPhoneNumber(zip)){
							throw new RegisterEntryZipException("Please enter valid zip code");
					}
			
					else if(zip.length()<5){
							throw new RegisterEntryZipException(" Please enter 5 digit zip code");
							
					}
				
				//Validation for answer
					if(Validator.isNull(security_answer)) {
					
					throw new RegisterEntrysecurityAnswerException("Answer is empty, Please enter the Answer ");
				}		
					else if (security_answer.length()>255){
						throw new RegisterEntrysecurityAnswerException("Answer length exceeds than 255 ");
					}
		
					else if(Validator.isAlphanumericName(security_answer)){
						throw new RegisterEntrysecurityAnswerException(" Please enter valid  Answer ");
						
					}
				
	}
	
	
	
}