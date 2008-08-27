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
 * generate view files and controller files for acegi user management
 * @author Tsuyoshi Yamamoto
 * @auther Haotian Sun
 */

import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU

grailsHome = Ant.project.properties."environment.GRAILS_HOME"
pluginVersion="0.2"

includeTargets << new File ( "${grailsHome}/scripts/Init.groovy" )  
includeTargets << new File ( "${basedir}/plugins/acegi-${pluginVersion}/scripts/AcegiInit.groovy" )  

pluginTemplatePath="plugins/acegi-${pluginVersion}/src/templates/manager"

overwrite=false

target('default': "generate view and controller for acegi user manage") {
	ClassLoader parent = getClass().getClassLoader()
	GroovyClassLoader loader = new GroovyClassLoader(parent)
	Class clazz = loader.parseClass(new File("${basedir}/${confPath}/AcegiConfig.groovy"))
	def acegiConfig = new ConfigSlurper().parse(clazz)
	println "Login user domain class: ${acegiConfig.acegi.loginUserDomainClass}"
	println "Authority domain class: ${acegiConfig.acegi.authorityDomainClass}"
	personDomainClassName=acegiConfig.acegi.loginUserDomainClass
	authorityDomainClassName=acegiConfig.acegi.authorityDomainClass
	requestmapDomainClassName=acegiConfig.acegi.requestMapClass
	
	generateManager("user")
	generateManager("role")
	generateManager("requestmap")
}

generateManager = {name->
	def uname = name[0].toUpperCase() + name.substring(1)
	
	def outFile = new File("${basedir}/${controllerPath}/${uname}Controller.groovy")

	if(outFile.exists()){
		Ant.input(addProperty:"overwrite",message:"Do you want to overwrite? y/n")
		def ovw = Ant.antProject.properties."overwrite"
		if(ovw=="y"){
			overwrite=true
		}
	}else{
		overwrite=true
	}
	
	println "generating files for ${uname} ......."
	def bind=[
		personDomain:"$personDomainClassName",
		authorityDomain:"$authorityDomainClassName",
		requestmapDomain:"$requestmapDomainClassName"]

	//generate UserController.groovy
	println "generating file ${basedir}/${controllerPath}/${uname}Controller.groovy"
	generateFile(
		"${basedir}/${pluginTemplatePath}/controllers/_${uname}Controller.groovy",
		bind,
		"${basedir}/${controllerPath}/${uname}Controller.groovy")

	//generate views for UserController
	println "generating view files - ${basedir}/${viewPath}/${name}/* "
	Ant.mkdir(dir:"${basedir}/${viewPath}/${name}")
	generateFile(
		"${basedir}/${pluginTemplatePath}/views/${name}/list.gsp",
		bind,
		"${basedir}/${viewPath}/${name}/list.gsp")
	generateFile(
		"${basedir}/${pluginTemplatePath}/views/${name}/edit.gsp",
		bind,
		"${basedir}/${viewPath}/${name}/edit.gsp")
	generateFile(
		"${basedir}/${pluginTemplatePath}/views/${name}/create.gsp",
		bind,
		"${basedir}/${viewPath}/${name}/create.gsp")
	generateFile(
		"${basedir}/${pluginTemplatePath}/views/${name}/show.gsp",
		bind,
		"${basedir}/${viewPath}/${name}/show.gsp")
}

generateFile={templateFile,binding,outputPath->
	def engine = new groovy.text.SimpleTemplateEngine()
	def templateF=new File(templateFile)
	def templateText = templateF.getText()
	def outFile = new File(outputPath)
	if(templateF.exists()){
		if(overwrite){
			def template = engine.createTemplate(templateText)
			//println template.make(binding).toString()
			outFile.withWriter { w ->
				template.make(binding).writeTo(w)
			}
			println "file generated at ${outFile.absolutePath}"
		}else{
			println "file *not* generated.: ${outFile.absolutePath} "
		}

	}else{
		println "${templateF} not exists"
	}
}
