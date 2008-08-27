/* Copyright 2006-2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package org.codehaus.groovy.grails.plugins.acegi;


import groovy.lang.GroovyObject;

import java.util.Iterator;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.hibernate.collection.PersistentSet;
import org.springframework.dao.DataAccessException;

/**
 * UserDetailsService with
 * GrailsDomainClass Data Access Object
 * @author  T.Yamamoto
 */
public class GrailsDaoImpl extends GrailsWebApplicationObjectSupport implements UserDetailsService {

	private static final Log logger = LogFactory.getLog(GrailsDaoImpl.class);
	
	/** LoginUser Domain Class */
	private String loginUserDomainClass;
	/** username field name */
	private String userName;
	/** password field name */
	private String password;
	/** enabled field name */
	private String enabled;
	/** LoginUser getAuthorities field */
	private String relationalAuthorities;
	/** authority field name */
	private String authority;
	
	/**
	 * Load login user by Username
	 */
	public UserDetails loadUserByUsername(String arg0)
			throws UsernameNotFoundException, DataAccessException {
		GrailsUser grailsUser=null;
		setUpSession();

		// get LoginUser
		//LoginUser Domain Class
		GrailsDomainClass userdc = (GrailsDomainClass)getGrailsApplication().getArtefact("Domain", loginUserDomainClass);
		//find user from DB by using GORM static method.
		GroovyObject user= 
			(GroovyObject)InvokerHelper.invokeStaticMethod(
				userdc.getClazz(),"findBy"+userName, arg0);
		
		if(user!=null){
			logger.debug("User found: "+user); 
			
			
			String _userName=(String)InvokerHelper.invokeMethod(user, "get"+userName, null);
			String _password=(String)InvokerHelper.invokeMethod(user, password, null);
			Boolean _enabled=(Boolean)InvokerHelper.invokeMethod(user, enabled, null);
			
			//TODO if authority is single String field in LoginUser class
			// if(relationalAuthorities==null){}
			
			//get authorities from LoginUser [LoginUser]--N:N--[Authority]
			PersistentSet authorities = 
				(PersistentSet)InvokerHelper.invokeMethod(user, relationalAuthorities, null);
			if ( authorities==null || (authorities!=null && authorities.size() == 0) ) {
				logger.error("User ["+_userName+"] has no GrantedAuthority");
				throw new UsernameNotFoundException("User has no GrantedAuthority");
			}
			GrantedAuthority[] _authorities=new GrantedAuthorityImpl[authorities.size()];
			int num=0;
			for (Iterator iter = authorities.iterator(); iter.hasNext();) {
				GroovyObject item = (GroovyObject) iter.next();
				String _authority = (String)InvokerHelper.invokeMethod(item, authority, null);
				_authorities[num]=new GrantedAuthorityImpl(_authority);
				num++;
			}
			
			//set properties to GrailsUser extends org.acegisecurity.userdetails.User
			grailsUser = new GrailsUser(_userName, _password, _enabled
					.booleanValue(), true, true, true, _authorities);
			//and set LoginUser domain class object
			grailsUser.setDomainClass(user);
			
		}else{
			logger.error("User not found: " + arg0);
			releaseSession();
			throw new UsernameNotFoundException("User not found.",arg0);
		}
		
		releaseSession();
		return grailsUser;
	}




	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginUserDomainClass() {
		return loginUserDomainClass;
	}

	public void setLoginUserDomainClass(String loginUserDomainClass) {
		this.loginUserDomainClass = loginUserDomainClass;
	}

	public String getRelationalAuthorities() {
		return relationalAuthorities;
	}

	public void setRelationalAuthorities(String relationalAuthorities) {
		this.relationalAuthorities = relationalAuthorities;
	}

}
