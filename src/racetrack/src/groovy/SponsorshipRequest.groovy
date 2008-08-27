class SponsorshipRequest {
  String companyName
  String phoneNumber
  
  static constraints = {
    companyName(blank:false)
  }
}