/**
 * ${authorityDomain} class for Authority
 * @author 
 */
class ${authorityDomain} {

	static hasMany=[people:${personDomain}]

	/** description */
	String description
	/** ROLE String */
	String authority="ROLE_"

	static def constraints = {
		authority(blank:false)
		description()
	}
}