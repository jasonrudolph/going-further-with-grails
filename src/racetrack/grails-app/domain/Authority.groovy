/**
 * Authority class for Authority
 * @author 
 */
class Authority {

	static hasMany=[people:Person]

	/** description */
	String description
	/** ROLE String */
	String authority="ROLE_"

	static def constraints = {
		authority(blank:false)
		description()
	}
}