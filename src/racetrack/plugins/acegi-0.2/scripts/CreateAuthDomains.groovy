/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Create/Copy Domains, auth.gsp, Controllers for acegi-plugin.
 * 
 * @author Tsuyoshi Yamamoto
 */
 
import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU

grailsHome = Ant.project.properties."environment.GRAILS_HOME"
pluginVersion="0.2"

includeTargets << new File ( "${grailsHome}/scripts/Init.groovy" )  
includeTargets << new File ( "${basedir}/plugins/acegi-${pluginVersion}/scripts/AcegiInit.groovy" )  


target("default":"create domain classes for acegi-plugin") {

	def procIt = false
	if(args){
		def _args = args.split("\n")
		//println _args.size()
		if(_args.size()==1){
			println "Login user domain class: ${_args[0]}"
			personDomainClassName=_args[0]
			//prompt for authority name
			Ant.input(addProperty:"authority.name",message:"authority domain class name not specified. Please enter:")
			def _authorityDCName = Ant.antProject.properties."authority.name"
			if(_authorityDCName){
				def aDC = _authorityDCName.split("\n")
				if(aDC.size()==1){
					authorityDomainClassName=aDC[0]
					println "Authority domain class: ${aDC[0]}"
				}else{usage()}
			}else{usage()}
			procIt=true
		}else if(_args.size()==2){
			println "Login user domain class: ${_args[0]}"
			println "Authority domain class: ${_args[1]}"
			personDomainClassName=_args[0]
			authorityDomainClassName=_args[1]
			procIt=true
		}else{
			usage()
		}
	}else{
		println "Create domain classes with default name"
		procIt=true
	}
	
	if(procIt){
		createDomains()
		copyViewAndControlls()
	}else{
		usage()
	}

}

target(createDomains:""){

  def bind=[personDomain:"$personDomainClassName",
    authorityDomain:"$authorityDomainClassName",requestmapDomain:"$requestmapDomainClassName"]
  //create Person domain class
  generateFile(bind,
    "${basedir}/${pluginTemplatePath}/_Person.groovy",
    "${basedir}/${domainClassPath}/${personDomainClassName}.groovy")
  //create Authority domain class
  generateFile(bind,
    "${basedir}/${pluginTemplatePath}/_Authority.groovy",
    "${basedir}/${domainClassPath}/${authorityDomainClassName}.groovy")
  //copy Requestmap domain class
  println "copying Requestmap domain class. "
  Ant.copy(
    file:"${basedir}/${pluginTemplatePath}/_Requestmap.groovy",
    tofile:"${basedir}/${domainClassPath}/Requestmap.groovy",overwrite:true)
  //create AcegiConfig
  generateFile(bind,
    "${basedir}/${pluginTemplatePath}/_AcegiConfig.groovy",
    "${basedir}/${confPath}/AcegiConfig.groovy")
}

target(copyViewAndControlls:""){
  //copy login.gsp and Login/Logout Controller example.
  println "copying login.gsp and Login/Logout Controller example. "
  Ant.mkdir(dir: "${basedir}/${viewPath}/login")
  Ant.copy(
    file:"${basedir}/${pluginTemplatePath}/views/login/auth.gsp",
    tofile:"${basedir}/${viewPath}/login/auth.gsp",overwrite:true)
  Ant.copy(
    file:"${basedir}/${pluginTemplatePath}/controllers/LoginController.groovy",
    tofile:"${basedir}/${controllerPath}/LoginController.groovy",overwrite:true)
  Ant.copy(
    file:"${basedir}/${pluginTemplatePath}/controllers/LogoutController.groovy",
    tofile:"${basedir}/${controllerPath}/LogoutController.groovy",overwrite:true)
  
    //log4j.logger.org.acegisecurity="off,stdout"
    Ant.input(addProperty:"addLogConfig",message:"Do you want add log config to Config.groovy? y/n")
    def addC = Ant.antProject.properties."addLogConfig"
    if(addC=="y"){
      def configFile = new File("${basedir}/grails-app/conf/Config.groovy")
      if(configFile.exists())configFile.append("\n\nlog4j.logger.org.acegisecurity=\"off,stdout\"")
    }
}

target(usage:"usage"){
  println "usage: grails create-auth-domains person authority"
  System.exit(1)
}

generateFile={binding,templateFile,outputPath->
  def engine = new groovy.text.SimpleTemplateEngine()
  def templateF=new File(templateFile)
  def templateText = templateF.getText()
  def outFile = new File(outputPath)
  if(templateF.exists()){
    if(outFile.exists()){
      println "${outFile} exists"
    }else{
      def template = engine.createTemplate(templateText)
      //println template.make(binding).toString()
      outFile.withWriter { w ->
        template.make(binding).writeTo(w)
      }
      println "file generated at ${outFile.absolutePath}"
    }
  }else{
    println "${templateText} not exists"
  }
}
