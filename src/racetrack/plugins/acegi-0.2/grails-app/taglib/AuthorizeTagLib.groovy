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

import org.acegisecurity.context.SecurityContextHolder as SCH

/**
 * Authorize Taglibs
 * rewrote to the Groovy from Java source of
 * org.acegisecurity.taglibs.authz.AuthorizeTag
 * 
 * @author T.Yamamoto
 *
 */
class AuthorizeTagLib  {
	AuthenticateService authenticateService
	/**
	 * 
	 */
	def ifAllGranted = {attrs, body ->
		def role = attrs['role']
		if(!authenticateService.ifAllGranted(role)){
			out << "";
		}else{
			out << body()
		}
	}

	/**
	 * 
	 */
	def ifNotGranted = {attrs, body ->
		def role = attrs['role']
		if(!authenticateService.ifNotGranted(role)){
			out << "";
		}else{
			out << body()
		}
	}

	/**
	 * 
	 */
	def ifAnyGranted = {attrs, body ->
		def role = attrs['role']
		if(!authenticateService.ifAnyGranted(role)){
			out << "";
		}else{
			out << body()
		}
	}

	/**
	 * 
	 */
	def loggedInUserInfo = {attrs,body->
		def authPrincipal = SCH?.context?.authentication?.principal
		if( authPrincipal!=null && authPrincipal!="anonymousUser"){
			out << authPrincipal?.domainClass?."${attrs.field}"
		}else{
			out << body()
		}
	}
	def isLoggedIn = {attrs, body ->
		def authPrincipal = SCH?.context?.authentication?.principal
		if( authPrincipal!=null && authPrincipal!="anonymousUser"){
			out << body()
		}
	}
	def isNotLoggedIn = {attrs, body ->
		def authPrincipal = SCH?.context?.authentication?.principal
		if( authPrincipal==null || authPrincipal=="anonymousUser"){
			out << body()
		}
	}

}