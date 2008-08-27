class Registration {
    String name
    String email
    Date dateCreated
    
    Race race
    
    static belongsTo = [Race]
}
