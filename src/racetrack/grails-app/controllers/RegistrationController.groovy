            
class RegistrationController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!params.max) params.max = 10
        [ registrationList: Registration.list( params ) ]
    }

    def show = {
        def registration = Registration.get( params.id )

        if(!registration) {
            flash.message = "Registration not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ registration : registration ] }
    }

    def delete = {
        def registration = Registration.get( params.id )
        if(registration) {
            registration.delete()
            flash.message = "Registration ${params.id} deleted"
            redirect(action:list)
        }
        else {
            flash.message = "Registration not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def registration = Registration.get( params.id )

        if(!registration) {
            flash.message = "Registration not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ registration : registration ]
        }
    }

    def update = {
        def registration = Registration.get( params.id )
        if(registration) {
            registration.properties = params
            if(!registration.hasErrors() && registration.save()) {
                flash.message = "Registration ${params.id} updated"
                redirect(action:show,id:registration.id)
            }
            else {
                render(view:'edit',model:[registration:registration])
            }
        }
        else {
            flash.message = "Registration not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def registration = new Registration()
        registration.properties = params
        return ['registration':registration]
    }

    def save = {
        def registration = new Registration(params)
        if(!registration.hasErrors() && registration.save()) {
            flash.message = "Registration ${registration.id} created"
            redirect(action:show,id:registration.id)
        }
        else {
            render(view:'create',model:[registration:registration])
        }
    }
}