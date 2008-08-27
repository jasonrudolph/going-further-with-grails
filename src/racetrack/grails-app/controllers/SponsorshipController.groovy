class SponsorshipController {
                     
    def defaultAction = "create"
    
    def sponsorshipService

    def create = { SponsorshipRequest sponsorshipRequest ->
      render(view:"create", model:[sponsorship:sponsorshipRequest])
    }

    def save = { SponsorshipRequest sponsorshipRequest -> 
      if (sponsorshipRequest.hasErrors()) {
        render(view:"create", model:[sponsorship:sponsorshipRequest])
      }
      else {
        flash.message = "Thanks!  We'll be in touch."
        sponsorshipService.submitRequest(sponsorshipRequest)
        redirect(action:"create")
      }
    }
}
