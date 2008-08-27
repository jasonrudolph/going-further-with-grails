import org.acegisecurity.providers.UsernamePasswordAuthenticationToken as UPAuth
import org.acegisecurity.context.SecurityContextHolder as SCH
import org.springframework.mock.web.*
import javax.servlet.*
import javax.servlet.http.*

class AcegiSecurityTests extends GroovyTestCase {
	def user
	def auth
	def authenticationProcessingFilter //injected by spring
	def authenticationManager // injected by spring
	def daoAuthenticationProvider // injected by spring
	def logoutFilter
	def authenticateService
	
	void setUp() {
		user = new User(username:"tester1", password: 'tester1'.encodeAsSHA256().encodeAsBase64(), email:"tester1@cliqup.com")
		user.password = 'tester1'.encodeAsSHA256().encodeAsBase64()
		user.save()
		auth = new UPAuth("tester1", "tester1")
		assert daoAuthenticationProvider //make sure this is available
	}
	
	void testAuthenticationProcessingFilterParams() {
		assertEquals "/cliqup_security_check", authenticationProcessingFilter.filterProcessesUrl
		assertEquals "/", authenticationProcessingFilter.defaultTargetUrl
		assertEquals "/security/loginretry", authenticationProcessingFilter.authenticationFailureUrl
	}
	
	void testAuthenticationWithNoAuthority() {
		shouldFail {
			daoAuthenticationProvider.authenticate(auth)
		}				
	}
	
	void testAuthenticationWithAuthority() {
		def authority = new Authority(authority:'ROLE_USER')
		authority.addToUsers(user)
		authority.save()
		def authtoken = daoAuthenticationProvider.authenticate(auth)
		assert authtoken
		def userdetails = authtoken.principal
		assertEquals 'tester1', userdetails.username
		assertEquals 'tester1', authtoken.credentials
		assert userdetails.authorities.size() == 1
		assertToString userdetails.authorities[0], 'ROLE_USER'
	}
	
	void testNoSuchUserFailure() {
		shouldFail {
			daoAuthenticationProvider.authenticate(new UPAuth('nosuchuser','nosuchuser'))
		}
	}
	
	void testAuthenticationFailure() {
		shouldFail {
			daoAuthenticationProvider.authenticate(new UPAuth('tester1','badpass'))
		}
	}
	
	void testLogoutSuccess() {
		SCH.context.authentication = auth
		def request = new MockHttpServletRequest()
		request.requestURI = '/j_acegi_logout'
		def response = new MockHttpServletResponse()
		def chain = new MockFilterChain()
		logoutFilter.doFilter((HttpServletRequest) request, (HttpServletResponse) response, (FilterChain) chain)
		assertNull SCH.context.authentication
	}
}