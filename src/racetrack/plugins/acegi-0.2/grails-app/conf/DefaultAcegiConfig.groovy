acegi {
	/** load acegi or not */
	loadAcegi=false

	/** login user class fields (default user class = Person)*/
	loginUserDomainClass="Person"
	userName="username"
	password="passwd"
	enabled="enabled"
	relationalAuthorities = "authorities"

	/**
	 * Authority domain class authority field name 
	 * authorityFieldInList
	 */
	authorityDomainClass="Authority"
	authorityField="authority"

	/** authenticationProcessingFilter */
	authenticationFailureUrl = "/login/authfail?login_error=1"
	defaultTargetUrl = "/"
	filterProcessesUrl = "/j_acegi_security_check"

	/** anonymousProcessingFilter */
	key = "foo"
	userAttribute = "anonymousUser,ROLE_ANONYMOUS"

	/** authenticationEntryPoint */
	loginFormUrl="/login/auth"
	forceHttps="false"
	ajaxLoginFormUrl="/login/authAjax"

	/** accessDeniedHandler 
	 *  set errorPage to null, if you want to get error code 403 (FORBIDDEN).
	 */
	errorPage="/login/denied"
	ajaxErrorPage="/login/deniedAjax"
	ajaxHeader="X-Requested-With"

	/** passwordEncoder */
	//The digest algorithm to use.
	//Supports the named Message Digest Algorithms in the Java environment.
	//http://java.sun.com/j2se/1.4.2/docs/guide/security/CryptoSpec.html#AppA
	algorithm="SHA"  // Ex. MD5 SHA 
	//use Base64 text ( true or false )
	encodeHashAsBase64=false

	
	/** rememberMeServices */
	cookieName="grails_remember_me" 
	alwaysRemember=false
	tokenValiditySeconds=1209600 //14 days
	parameter="_acegi_security_remember_me"
	
	/** LoggerListener 
	 * ( add "log4j.logger.org.acegisecurity=info,stdout" to log4j.*.properties to see logs ) 
	 */
	useLogger = false

	/** use RequestMap from DomainClass */
	useRequestMapDomainClass = true

	/** Requestmap domain class (if useRequestMapDomainClass = true) */
	requestMapClass="Requestmap"
	requestMapPathField="url"
	requestMapConfigAttributeField="configAttribute"

	/** 
	 * if useRequestMapDomainClass is false, set request map pattern in string
	 * see example below
	 */
	requestMapString = """
	CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON
	PATTERN_TYPE_APACHE_ANT

	/login/**=IS_AUTHENTICATED_ANONYMOUSLY
	/admin/**=ROLE_USER
	/book/test/**=IS_AUTHENTICATED_FULLY
	/book/**=ROLE_SUPERVISOR
	/**=IS_AUTHENTICATED_ANONYMOUSLY
	"""


  /**use email notification while registration*/
  useMail = false
  mailHost = "localhost"
  mailUsername = "user@localhost"
  mailPassword = "sungod"
  mailProtocol = "smtp"
  mailFrom = "user@localhost"

  
  /** default user's role for user registration */
  defaultRole="ROLE_USER"
}

algorithmMethods {
  MD5="md5Hex"
  SHA="shaHex"
  SHA1="shaHex"
  SHA256="sha256Hex"
  SHA384="sha384Hex"
  SHA512="sha512Hex"
}
