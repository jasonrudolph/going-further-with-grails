/**
 * ${personDomain} for user account
 * @author 
 */
class ${personDomain} {
	static transients = ["pass"]
	static hasMany=[authorities:${authorityDomain}]
	static belongsTo = ${authorityDomain}

	/** Username */
	String username
	/** User Real Name*/
	String userRealName
	/** MD5 Password */
	String passwd
	/** enabled */
	boolean enabled = false

	String email
	boolean email_show = false

	/** description */
	String description

	/** plain password to create a MD5 password */
	String pass="[secret]"

	static def constraints = {
		username(blank:false,unique:true)
		userRealName(blank:false)
		passwd(blank:false)
		enabled()
	}
}