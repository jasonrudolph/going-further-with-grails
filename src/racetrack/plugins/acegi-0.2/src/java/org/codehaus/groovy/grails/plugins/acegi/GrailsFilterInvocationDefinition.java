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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.SecurityConfig;
import org.acegisecurity.intercept.web.FilterInvocation;
import org.acegisecurity.intercept.web.FilterInvocationDefinitionSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.hibernate.collection.PersistentSet;

/**
 * 
 * GrailsFilterInvocationDefinition
 * @author  T.Yamamoto
 */
public class GrailsFilterInvocationDefinition extends
		GrailsWebApplicationObjectSupport implements FilterInvocationDefinitionSource {
	
	private static final Log logger = LogFactory.getLog(GrailsFilterInvocationDefinition.class);
	
	private List defaultRequestMap = new Vector();
	private List requestMap =null;
	private PathMatcher pathMatcher = new AntPathMatcher();
	private boolean convertUrlToLowercaseBeforeComparison = false;
	
	private String requestMapClass;
	private String requestMapPathFieldMethod;
	private String requestMapConfigAttributeFieldMethod;
	private String requestMapPathFieldName;
	
	
	public ConfigAttributeDefinition lookupAttributes(String url) {
		setUpSession();
		
		//set LowerCase compulsorily
		url = url.toLowerCase();
		
		int pos = url.indexOf("?");
		if(pos>0){
			url = url.substring(0, pos);
		}

		//TODO more better way
		//create query
		url=url.replaceAll("\"","");
		url=url.replaceAll("'","");

		//TODO more better way
		if (!url.contains(".") || url.indexOf(".gsp")>-1 || url.indexOf(".jsp")>-1) {
			StringTokenizer stn = new StringTokenizer(url,"/");
			String hql="from "+requestMapClass+" where "+requestMapPathFieldName+" = '/**' ";
			String path="/";
			while (stn.hasMoreTokens()) {
				String element = (String) stn.nextToken();
				path+=element+"/";
				hql+="or "+requestMapPathFieldName+" ='"+path+"**' ";
			}
			hql+="order by length("+requestMapPathFieldName+") desc";
			
			//find requestMap from DB by using GORM static method.
			GrailsDomainClass requestMapDomainClass = (GrailsDomainClass)getGrailsApplication().getArtefact("Domain", requestMapClass);
			List reqMap= 
				(List)InvokerHelper.invokeStaticMethod(requestMapDomainClass.getClazz(),"findAll", hql);

			if (reqMap != null) {
				Iterator iter = reqMap.iterator();
				while (iter.hasNext()) {
					GroovyObject gobj = (GroovyObject) iter.next();
					String _configAttribute = (String) InvokerHelper.invokeMethod(gobj,
							requestMapConfigAttributeFieldMethod, null);
					String _url = (String) InvokerHelper.invokeMethod(gobj, requestMapPathFieldMethod, null);
					_url = _url.toLowerCase();
					boolean matched = pathMatcher.match(_url, url);
					if (matched) {
						ConfigAttributeDefinition cad = new ConfigAttributeDefinition();
						String[] configAttrs = StringUtils.commaDelimitedListToStringArray(_configAttribute);
						for (int i = 0; i < configAttrs.length; i++) {
							String configAttribute = configAttrs[i];
							cad.addConfigAttribute(new SecurityConfig(configAttribute));
						}
						releaseSession();
						return cad;
					}
				}
			}
		}
		
		releaseSession();
		return null;
	}

	//@SuppressWarnings("unchecked")//comment out for 1.4
	public Iterator getConfigAttributeDefinitions() {
		Set set = new HashSet();
		if(requestMap==null){
			//TODO Maybe Load requestMap here (this method will load only once) use lookupAttributes when updated?
			// dummy 
			ConfigAttributeDefinition cad = new ConfigAttributeDefinition();
			cad.addConfigAttribute(new SecurityConfig("IS_AUTHENTICATED_ANONYMOUSLY"));
			defaultRequestMap.add(new EntryHolder("/*", cad));
			Iterator iter = defaultRequestMap.iterator();
			while (iter.hasNext()) {
				EntryHolder entryHolder = (EntryHolder) iter.next();
				set.add(entryHolder.getConfigAttributeDefinition());
			}	
		}else{
			Iterator iter = requestMap.iterator();
			while (iter.hasNext()) {
				EntryHolder entryHolder = (EntryHolder) iter.next();
				set.add(entryHolder.getConfigAttributeDefinition());
			}
		}
		return set.iterator();
	}



	public ConfigAttributeDefinition getAttributes(Object object) throws IllegalArgumentException {
		if ((object == null) || !this.supports(object.getClass())) {
			logger.error("Object must be a FilterInvocation");
			throw new IllegalArgumentException("Object must be a FilterInvocation");
		}

		String url = ((FilterInvocation) object).getRequestUrl();

		return this.lookupAttributes(url);
	}

	public boolean supports(Class clazz) {
		if (FilterInvocation.class.isAssignableFrom(clazz)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isConvertUrlToLowercaseBeforeComparison() {
		return convertUrlToLowercaseBeforeComparison;
	}

	public void setConvertUrlToLowercaseBeforeComparison(
			boolean convertUrlToLowercaseBeforeComparison) {
		this.convertUrlToLowercaseBeforeComparison = convertUrlToLowercaseBeforeComparison;
	}

	public String getRequestMapClass() {
		return requestMapClass;
	}

	public void setRequestMapClass(String requestMapClass) {
		this.requestMapClass = requestMapClass;
	}

	public String getRequestMapConfigAttributeFieldMethod() {
		return requestMapConfigAttributeFieldMethod;
	}

	public void setRequestMapConfigAttributeFieldMethod(
			String requestMapConfigAttributeFieldMethod) {
		this.requestMapConfigAttributeFieldMethod = requestMapConfigAttributeFieldMethod;
	}

	public String getRequestMapPathFieldMethod() {
		return requestMapPathFieldMethod;
	}

	public void setRequestMapPathFieldMethod(String requestMapPathFieldMethod) {
		this.requestMapPathFieldMethod = requestMapPathFieldMethod;
	}

	public String getRequestMapPathFieldName() {
		return requestMapPathFieldName;
	}

	public void setRequestMapPathFieldName(String requestMapPathFieldName) {
		this.requestMapPathFieldName = requestMapPathFieldName;
	}

	protected class EntryHolder {
		private ConfigAttributeDefinition configAttributeDefinition;
		private String antPath;

		public EntryHolder(String antPath, ConfigAttributeDefinition attr) {
			this.antPath = antPath;
			this.configAttributeDefinition = attr;
		}

		protected EntryHolder() {
			throw new IllegalArgumentException("Cannot use default constructor");
		}

		/**
		 * @return  antPath
		 * @uml.property  name="antPath"
		*/
		public String getAntPath() {
			return antPath;
		}

		/**
		 * @return  configAttributeDefinition
		 * @uml.property  name="configAttributeDefinition"
		*/
		public ConfigAttributeDefinition getConfigAttributeDefinition() {
			return configAttributeDefinition;
		}
	}

}
