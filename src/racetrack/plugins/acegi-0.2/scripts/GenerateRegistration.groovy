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
 * 
 * @auther Haotian Sun
 * @auther Tsuyoshi Yamamoto
 */
 
import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU

grailsHome = Ant.project.properties."environment.GRAILS_HOME"
pluginVersion="0.2"

includeTargets << new File ( "${grailsHome}/scripts/Init.groovy" )  
includeTargets << new File ( "${basedir}/plugins/acegi-${pluginVersion}/scripts/AcegiInit.groovy" )  

pluginTemplatePath = "plugins/acegi-${pluginVersion}/src/templates/manager"

overwrite = false

target('default': "generate user registration views and controllers") {
  ClassLoader parent = getClass().getClassLoader()
  GroovyClassLoader loader = new GroovyClassLoader(parent)
  Class clazz = loader.parseClass(new File("${basedir}/${confPath}/AcegiConfig.groovy"))
  def acegiConfig = new ConfigSlurper().parse(clazz)
  println "Login user domain class: ${acegiConfig.acegi.loginUserDomainClass}"
  println "Authority domain class: ${acegiConfig.acegi.authorityDomainClass}"
  personDomainClassName = acegiConfig.acegi.loginUserDomainClass
  authorityDomainClassName = acegiConfig.acegi.authorityDomainClass
  requestmapDomainClassName = acegiConfig.acegi.requestMapClass
  Ant.sequential {
    def mailjar = new File("${basedir}/lib/mail-1.4.jar")
    if(!mailjar.exists()){
      println "Downloading mail.jar ..."
      get(dest: "${basedir}/lib/mail-1.4.jar",
              src: "http://repo1.maven.org/maven2/javax/mail/mail/1.4/mail-1.4.jar",
              verbose: true,
              usetimestamp: true)
    }
    def activationjar = new File("${basedir}/lib/activation-1.1.jar")
    if(!activationjar.exists()){
      println "Downloading activation.jar ..."
      get(dest: "${basedir}/lib/activation-1.1.jar",
              src: "http://repo1.maven.org/maven2/javax/activation/activation/1.1/activation-1.1.jar",
              verbose: true,
              usetimestamp: true)
    }
  }
  generateRegistration("register")
}

generateRegistration = {name ->
  def uname = name[0].toUpperCase() + name.substring(1)

  def outFile = new File("${basedir}/${controllerPath}/${uname}Controller.groovy")

  if (outFile.exists()) {
    Ant.input(addProperty: "overwrite", message: "Do you want to overwrite? y/n")
    def ovw = Ant.antProject.properties."overwrite"
    if (ovw == "y") {
      overwrite = true
    }
  } else {
    overwrite = true
  }

  println "generating files for ${uname} ......."
  def bind = [
          personDomain: "$personDomainClassName",
          authorityDomain: "$authorityDomainClassName",
          requestmapDomain: "$requestmapDomainClassName"]

  //copy the CaptchaController
  println "copying CaptchaController.groovy to - ${basedir}/${controllerPath}/CaptchaController.groovy "
  Ant.copy(
          file: "${basedir}/${pluginTemplatePath}/controllers/_CaptchaController.groovy",
          tofile: "${basedir}/${controllerPath}/CaptchaController.groovy", overwrite: overwrite)

  //copy the EmailerService
  println "copying EmailerService.groovy to - ${basedir}/${servicePath}/EmailerService.groovy "
  Ant.copy(
          file: "${basedir}/${pluginTemplatePath}/services/_EmailerService.groovy",
          tofile: "${basedir}/${servicePath}/EmailerService.groovy", overwrite: overwrite)
  //generate RegisterController.groovy
  println "generating file ${basedir}/${controllerPath}/${uname}Controller.groovy"
  generateFile(
          "${basedir}/${pluginTemplatePath}/controllers/_${uname}Controller.groovy",
          bind,
          "${basedir}/${controllerPath}/${uname}Controller.groovy")

  //generate views for RegisterController
  println "copying view files to - ${basedir}/${viewPath}/${name}/* "
  Ant.mkdir(dir: "${basedir}/${viewPath}/${name}")
  Ant.copy(
          file:"${basedir}/${pluginTemplatePath}/views/${name}/edit.gsp",
          tofile:"${basedir}/${viewPath}/${name}/edit.gsp", overwrite: overwrite)
  Ant.copy(
          file:"${basedir}/${pluginTemplatePath}/views/${name}/index.gsp",
          tofile:"${basedir}/${viewPath}/${name}/index.gsp", overwrite: overwrite)
  Ant.copy(
          file:"${basedir}/${pluginTemplatePath}/views/${name}/show.gsp",
          tofile:"${basedir}/${viewPath}/${name}/show.gsp", overwrite: overwrite)
}

generateFile = {templateFile, binding, outputPath ->
  def engine = new groovy.text.SimpleTemplateEngine()
  def templateF = new File(templateFile)
  def templateText = templateF.getText()
  def outFile = new File(outputPath)
  if (templateF.exists()) {
    if (overwrite) {
      def template = engine.createTemplate(templateText)
      //println template.make(binding).toString()
      outFile.withWriter {w ->
        template.make(binding).writeTo(w)
      }
      println "file generated at ${outFile.absolutePath}"
    } else {
      println "file *not* generated.: ${outFile.absolutePath} "
    }

  } else {
    println "${templateF} not exists"
  }
}
