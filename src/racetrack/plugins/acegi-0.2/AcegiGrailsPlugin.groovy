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
import org.springframework.core.annotation.AnnotationUtils
import org.acegisecurity.annotation.Secured
import org.acegisecurity.annotation.SecurityAnnotationAttributes
import org.acegisecurity.intercept.method.MethodDefinitionAttributes
//import org.codehaus.groovy.grails.plugins.acegi.QuietMethodSecurityInterceptor

/**
 * Grails Spring Security(Acegi Security) Plugin
 * 
 * @author T.Yamamoto
 * @auther Haotian Sun
 */
class AcegiGrailsPlugin {
	def version = "0.2"
	def author = "Tsuyoshi Yamamoto"
	def authorEmail = "tyama@xmldo.jp"
	def title = "Spring Security (Acegi Security) on Grails Plugin"
	def description = '''Plugin to use grails domain class from the Spring Security(Acegi Security) and secure your applications with Spring Security(Acegi Security) filters.
	'''
	def documentation ="http://docs.codehaus.org/display/GRAILS/AcegiSecurity+Plugin"

	def watchedResources = "**/grails-app/conf/AcegiConfig.groovy"

	def doWithSpring = {

    ClassLoader parent = getClass().getClassLoader()
    GroovyClassLoader loader = new GroovyClassLoader(parent)
    def ac = loader.loadClass("AcegiConfig")
    def dac = loader.loadClass("DefaultAcegiConfig")
    def _user_config = new ConfigSlurper().parse(ac)
    def _default_config = new ConfigSlurper().parse(dac)
    

		def config
		if(_user_config){
			log.info("using user AcegiConfig")
			config = _default_config.merge(_user_config)
		}else{
			log.info("using DefaultAcegiConfig")
			config = _default_config
		}

		def conf = config.acegi
		//println conf.loadAcegi
		if(conf && conf.loadAcegi){
			log.info("loading acegi config ...")
      def useMail = conf.useMail

			def makeItGetter = { field ->
				"get" + field[0].toUpperCase() + field.substring(1)	
			}
			
			/** filter list */
			def filters = 
				["httpSessionContextIntegrationFilter",
					"logoutFilter",
					"authenticationProcessingFilter",
					"securityContextHolderAwareRequestFilter",
					"rememberMeProcessingFilter",
					"anonymousProcessingFilter",
					"exceptionTranslationFilter",
					"filterInvocationInterceptor"]

			/** filterChainProxy */
			filterChainProxy(org.acegisecurity.util.FilterChainProxy){
				filterInvocationDefinitionSource="""
					CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON
					PATTERN_TYPE_APACHE_ANT
					/**=${filters.join(',')}
					"""
			}
			
			/** httpSessionContextIntegrationFilter */
			httpSessionContextIntegrationFilter(org.acegisecurity.context.HttpSessionContextIntegrationFilter){}

			/** logoutFilter */
			logoutFilter(org.codehaus.groovy.grails.plugins.acegi.GrailsLogoutFilter,"/",ref("rememberMeServices")){}

			/** authenticationProcessingFilter */
			authenticationProcessingFilter(org.acegisecurity.ui.webapp.AuthenticationProcessingFilter){
				authenticationManager = ref("authenticationManager")
				authenticationFailureUrl = conf.authenticationFailureUrl //"/login/authfail?login_error=1"
				defaultTargetUrl = conf.defaultTargetUrl // "/"
				filterProcessesUrl = conf.filterProcessesUrl //"/j_acegi_security_check"
				rememberMeServices = ref("rememberMeServices")
			}
			
			/** securityContextHolderAwareRequestFilter */
			securityContextHolderAwareRequestFilter(org.acegisecurity.wrapper.SecurityContextHolderAwareRequestFilter){}

			/** anonymousProcessingFilter */
			anonymousProcessingFilter(org.acegisecurity.providers.anonymous.AnonymousProcessingFilter){
				key = conf.key // "foo"
				userAttribute = conf.userAttribute //"anonymousUser,ROLE_ANONYMOUS"
			}
			
			/** rememberMeProcessingFilter */
			rememberMeProcessingFilter(org.acegisecurity.ui.rememberme.RememberMeProcessingFilter){
				authenticationManager=ref("authenticationManager")
				rememberMeServices=ref("rememberMeServices")
			}
			/** rememberMeServices */
			rememberMeServices(org.acegisecurity.ui.rememberme.TokenBasedRememberMeServices){
				userDetailsService=ref("userDetailsService")
				key="grailsRocks"
				cookieName=conf.cookieName
				alwaysRemember=conf.alwaysRemember
				tokenValiditySeconds=conf.tokenValiditySeconds
				parameter=conf.parameter
			}

			/** exceptionTranslationFilter */
			exceptionTranslationFilter(org.acegisecurity.ui.ExceptionTranslationFilter){
				authenticationEntryPoint=ref("authenticationEntryPoint")
				accessDeniedHandler=ref("accessDeniedHandler")
			}

			authenticationEntryPoint(org.codehaus.groovy.grails.plugins.acegi.WithAjaxAuthenticationProcessingFilterEntryPoint){
				loginFormUrl= conf.loginFormUrl // "/login/auth"
				forceHttps= conf.forceHttps // "false"
				ajaxLoginFormUrl=conf.ajaxLoginFormUrl // "/login/authAjax"
				if(conf.ajaxHeader) ajaxHeader=conf.ajaxHeader //default: X-Requested-With
			}
			accessDeniedHandler(org.codehaus.groovy.grails.plugins.acegi.GrailsAccessDeniedHandlerImpl){
				errorPage= conf.errorPage=="null"?null:conf.errorPage // "/login/denied" or 403
				ajaxErrorPage= conf.ajaxErrorPage
				if(conf.ajaxHeader) ajaxHeader=conf.ajaxHeader //default: X-Requested-With
			}

			/** filterInvocationInterceptor */
			filterInvocationInterceptor(org.acegisecurity.intercept.web.FilterSecurityInterceptor){
				authenticationManager=ref("authenticationManager")
				accessDecisionManager=ref("accessDecisionManager")
				if( conf.useRequestMapDomainClass ){
					objectDefinitionSource=ref("objectDefinitionSource")
				}else{
					objectDefinitionSource=conf.requestMapString
				}
			}
			
			/** accessDecisionManager */
			accessDecisionManager(org.acegisecurity.vote.AffirmativeBased){
				allowIfAllAbstainDecisions="false"
				decisionVoters=[
					ref("roleVoter"),
					ref("authenticatedVoter")]
			}
			roleVoter(org.acegisecurity.vote.RoleVoter){}
			authenticatedVoter(org.acegisecurity.vote.AuthenticatedVoter){}

			if( conf.useRequestMapDomainClass ){
				/** objectDefinitionSource */
				objectDefinitionSource(org.codehaus.groovy.grails.plugins.acegi.GrailsFilterInvocationDefinition){
					requestMapClass= conf.requestMapClass // "Requestmap"
					requestMapPathFieldMethod= makeItGetter(conf.requestMapPathField) // "getUrl"
					requestMapConfigAttributeFieldMethod= makeItGetter(conf.requestMapConfigAttributeField) // "getConfig_attribute"
					requestMapPathFieldName= conf.requestMapPathField // "url"
				}
			}

			/** ProviderManager */
			authenticationManager(org.acegisecurity.providers.ProviderManager){
				providers=[
					ref("daoAuthenticationProvider"),
					ref("anonymousAuthenticationProvider"),
					ref("rememberMeAuthenticationProvider")]
			}
			
			
			/** daoAuthenticationProvider */
			daoAuthenticationProvider(org.acegisecurity.providers.dao.DaoAuthenticationProvider){
				userDetailsService=ref("userDetailsService")
				passwordEncoder=ref("passwordEncoder")
				userCache=ref("userCache")
			}

			/** userCache */
			userCache(org.acegisecurity.providers.dao.cache.EhCacheBasedUserCache){
				cache=ref("cache")
			}
			cache(org.springframework.cache.ehcache.EhCacheFactoryBean){
				cacheManager=ref("cacheManager")
				cacheName="userCache"
			}
			cacheManager(org.springframework.cache.ehcache.EhCacheManagerFactoryBean){}

			/** anonymousAuthenticationProvider */
			anonymousAuthenticationProvider(org.acegisecurity.providers.anonymous.AnonymousAuthenticationProvider){
				key= conf.key // "foo"
			}
			/** rememberMeAuthenticationProvider */
			rememberMeAuthenticationProvider(org.acegisecurity.providers.rememberme.RememberMeAuthenticationProvider){
				key="grailsRocks"
			}
			
			/** passwordEncoder */
			passwordEncoder(org.acegisecurity.providers.encoding.MessageDigestPasswordEncoder,conf.algorithm){
				if(conf.encodeHashAsBase64) encodeHashAsBase64=true
			}

			/** DetailsService */
			userDetailsService(org.codehaus.groovy.grails.plugins.acegi.GrailsDaoImpl){
					
				userName= conf.userName[0].toUpperCase() + conf.userName.substring(1)	// "findByUsername"
				password= makeItGetter(conf.password) // "getPasswd"
				enabled= makeItGetter(conf.enabled) // "getEnabled"

				authority= makeItGetter(conf.authorityField) // "getAuthority"
				loginUserDomainClass=conf.loginUserDomainClass //"Person"
				relationalAuthorities=makeItGetter(conf.relationalAuthorities)
			}

			/** LoggerListener ( log4j.logger.org.acegisecurity=info,stdout ) */
			if(conf.useLogger) loggerListener(org.acegisecurity.event.authentication.LoggerListener){}

			/** experiment on Annotation and MethodSecurityInterceptor .
			 *  for secure services
			 */
			serviceSecureAnnotation(SecurityAnnotationAttributes){}
			serviceSecureAnnotationODS(MethodDefinitionAttributes){
				attributes=ref("serviceSecureAnnotation")
			}
			/** securityInteceptor */
			securityInteceptor(org.codehaus.groovy.grails.plugins.acegi.QuietMethodSecurityInterceptor){
				validateConfigAttributes=false
				authenticationManager=ref("authenticationManager")
				accessDecisionManager=ref("accessDecisionManager")
				objectDefinitionSource=ref("serviceSecureAnnotationODS")
				throwException=true
			}

			/** check Annotation */
			def checkAnnotations ={cls->
				def result=false
				cls.methods.each{method->
					def annotations = method.getAnnotations()
					if(annotations.size()>0){
						annotations.each{annotation->
							if(annotation instanceof Secured){
								//println annotation
								result=true
							}
						}
					}
				}
				return result
			}
			//load Services which has Annotations
			application.serviceClasses.each { serviceClass ->
				if(checkAnnotations(serviceClass.clazz)){
					"${serviceClass.propertyName}Sec"(org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator){
						beanNames="${serviceClass.propertyName}"
						interceptorNames=["securityInteceptor"]
						proxyTargetClass=true
					}
				}
			}
      //load simple java mail settings
      if (useMail) {
        mailSender(org.springframework.mail.javamail.JavaMailSenderImpl) {
          host = conf.mailHost
          username = conf.mailUsername
          password = conf.mailPassword
          protocol = conf.mailProtocol
        }
        mailMessage(org.springframework.mail.SimpleMailMessage) {
          from = conf.mailFrom
        }
      }
		}else{
			log.info("[loadAcegi=false] Acegi Security will not loaded")
		}

	}
  def doWithApplicationContext = { applicationContext ->
    // TODO Implement post initialization spring config (optional)		
  }
  def doWithWebDescriptor = { xml ->

    ClassLoader parent = getClass().getClassLoader()
    GroovyClassLoader loader = new GroovyClassLoader(parent)
    def config
    def _user_config
    try {
      def ac = loader.loadClass("AcegiConfig")
      _user_config = new ConfigSlurper().parse(ac)
    }catch(Exception e) {
      println "AcegiConfig not found"
    }
    
    def dac = loader.loadClass("DefaultAcegiConfig")
    if(_user_config){
      log.info("using user AcegiConfig")
      def _default_config = new ConfigSlurper().parse(dac)
      config = _default_config.merge(_user_config)
    }else{
      log.info("using DefaultAcegiConfig")
      config = new ConfigSlurper().parse(dac)
    }

    def conf = config.acegi
    //println "***** ${conf.loadAcegi}"
    if(conf && conf.loadAcegi){
      def contextParam = xml."context-param"
      contextParam[contextParam.size()-1]+{
        'filter' {
          //'filter-name'('acegiAuthenticationProcessingFilter')
          //'filter-class'('org.acegisecurity.util.FilterToBeanProxy')
          'filter-name'('filterChainProxy')
          'filter-class'('org.springframework.web.filter.DelegatingFilterProxy')
          'init-param'{
            'param-name'('targetClass')
            'param-value'('org.acegisecurity.util.FilterChainProxy')
          }
        }
      }

      def filter = xml."filter"
      filter[filter.size()-1]+{
        'filter-mapping'{
          //'filter-name'('acegiAuthenticationProcessingFilter')
          'filter-name'('filterChainProxy')
          'url-pattern'("/*")
        }
      }
    }
  }

  def doWithDynamicMethods = { ctx ->
  }	
  def onChange = { event ->
  }                                                                                  
  def onApplicationChange = { event ->
  }
}
