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

import org.acegisecurity.intercept.InterceptorStatusToken;
import org.aopalliance.intercept.MethodInvocation;
import org.acegisecurity.intercept.method.aopalliance.MethodSecurityInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * MethodSecurityInterceptor without throws Exceptions
 * if Method Access is denied returns null
 * @author T.Yamamoto
 */
public class QuietMethodSecurityInterceptor extends MethodSecurityInterceptor {
	private static final Log logger = LogFactory.getLog(QuietMethodSecurityInterceptor.class);
	boolean throwException=false;

	public Object invoke(MethodInvocation mi) throws Throwable {
		Object result = null;
		InterceptorStatusToken token = null;
		try {
			token = super.beforeInvocation(mi);
		}catch(Exception e) {
			if(throwException) throw e;
			logger.error(e.getMessage());
		}

		try {
			result = mi.proceed();
		}catch(Exception e) {
			if(throwException) throw e;
			logger.error(e.getMessage());
		}

		try {
			result = super.afterInvocation(token, result);
		}catch(Exception e) {
			if(throwException) throw e;
			logger.error(e.getMessage());
		}

		return result;
	}
	
	public void setThrowException(boolean throwException) {
		this.throwException = throwException;
	}
}
