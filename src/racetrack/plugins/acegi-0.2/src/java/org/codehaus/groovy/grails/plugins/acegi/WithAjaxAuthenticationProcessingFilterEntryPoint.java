/*
 * Copyright 2007 the original author or authors.
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

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilterEntryPoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AuthenticationProcessingFilterEntryPoint with Ajax login form option
 * if Method Access is denied returns null
 * @author T.Yamamoto
 * @since 2007/09/14
 */
public class WithAjaxAuthenticationProcessingFilterEntryPoint extends AuthenticationProcessingFilterEntryPoint {
	private static final Log logger = LogFactory.getLog(WithAjaxAuthenticationProcessingFilterEntryPoint.class);
	public static final String AJAX_HEADER = "X-Requested-With";
	private String ajaxLoginFormUrl;
	private String ajaxHeader=AJAX_HEADER;
	
	protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) {
		
		if(request.getHeader(ajaxHeader)!=null && this.ajaxLoginFormUrl!=null){
			return this.ajaxLoginFormUrl;
		}
		return getLoginFormUrl();
	}
	public void setAjaxLoginFormUrl(String ajaxLoginFormUrl) {
		if ((ajaxLoginFormUrl != null) && !ajaxLoginFormUrl.startsWith("/")) {
			throw new IllegalArgumentException("ajaxLoginFormUrl must begin with '/'");
		}
		this.ajaxLoginFormUrl = ajaxLoginFormUrl;
	}
	public void setAjaxHeader(String ajaxHeader) {
		this.ajaxHeader = ajaxHeader;
	}
}
