import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU

grailsHome = Ant.project.properties."environment.GRAILS_HOME"

pluginVersion="0.2"
personDomainClassName="Person"
authorityDomainClassName="Authority"
requestmapDomainClassName="Requestmap"

pluginTemplatePath="plugins/acegi-${pluginVersion}/src/templates"
domainClassPath = "grails-app/domain"
confPath = "grails-app/conf"
controllerPath = "grails-app/controllers"
viewPath = "grails-app/views"
confPath = "grails-app/conf"
servicePath = "grails-app/services"

target ('default': "default params for acegi-plugin scripts") {
  println "this command is not  setup command for the plugin."
  println "this script contains default params for the other script only."
  println "please hit a command below for setting up plugin"
  println "# grails create-auth-domains"
}
