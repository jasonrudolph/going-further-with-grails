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

import java.util.Map;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsControllerClass;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.springframework.dao.DataAccessException;

/**
 * Simple User details 
 * @author T.Yamamoto
 *
 */
public class SimpleUserDetailsImpl extends GrailsWebApplicationObjectSupport implements UserDetailsService{
	private static final Log logger = LogFactory.getLog(SimpleUserDetailsImpl.class);
	
	private String controllerName;
	private String methodName;
	
	public UserDetails loadUserByUsername(String arg0) 
		throws UsernameNotFoundException, DataAccessException {
		GrailsUser user=null;
		//GrailsControllerClass controllerClass = getGrailsApplication().getController(controllerName);
		// for 0.5
		GrailsControllerClass controllerClass  = (GrailsControllerClass)getGrailsApplication().getArtefact("Controller", controllerName);
		Object controller = controllerClass.newInstance();
		
		Map obj = (Map)InvokerHelper.invokeMethod(controller, methodName, arg0);
		
		if(obj!=null){
			logger.debug("User found: "+obj);
			try {
				GrantedAuthority[] _authorities
				=new GrantedAuthorityImpl[]{new GrantedAuthorityImpl((String)obj.get("authority"))};
			user = new GrailsUser(
					(String)obj.get("username"), 
					(String)obj.get("pass"), true, true,true, true, _authorities );
			user.setDomainClass(obj);
			} catch (Exception e) {
				logger.debug("User not found: "+obj);
				throw new UsernameNotFoundException("User not found."+obj.get("username"));
			}

		}else{
			logger.error("User not found: " + arg0);
			throw new UsernameNotFoundException("User not found.");
		}
		
		return user;
	}

	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	
}
