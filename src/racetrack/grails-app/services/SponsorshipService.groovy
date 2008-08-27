import org.apache.commons.mail.SimpleEmail

class SponsorshipService {

    boolean transactional = false

    def submitRequest(SponsorshipRequest sponsorship) {
      def email = new SimpleEmail(
        hostName : 'localhost',
        from : 'racetrack@example.com',
        subject: 'SponsorshipRequest',
        msg: "Please contact ${sponsorship.companyName} at ${sponsorship.phoneNumber}"
      )
      email.addTo("admin@example.com")
      email.setDebug(true)
      email.send()
    }
}
