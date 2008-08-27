  
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Show Requestmap</title>
  </head>
  <body>
    <div class="nav">
      <span class="menuButton"><a href="${createLinkTo(dir:'')}">Home</a></span>
      <span class="menuButton"><g:link action="list">Requestmap List</g:link></span>
      <span class="menuButton"><g:link action="create">New Requestmap</g:link></span>
    </div>
    <div class="body">
      <h1>Show Requestmap</h1>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <div class="dialog">
        <table>
      	  <tbody>
                   
            <tr class="prop">
              <td valign="top" class="name">Id:</td>
              <td valign="top" class="value">${requestmap.id}</td>
            </tr>
                   
            <tr class="prop">
              <td valign="top" class="name">URL:</td>
              <td valign="top" class="value">${requestmap.url}</td>
            </tr>
                   
            <tr class="prop">
              <td valign="top" class="name">Roles:</td>
              <td valign="top" class="value">
              	
              <%
                String[] configAttrs=
                        org.springframework.util.StringUtils.
                        commaDelimitedListToStringArray(requestmap.configAttribute);
              %>
			  <g:each var="role" in="${configAttrs}">
			    <g:if test="${role.startsWith('ROLE_')}">            
            	  ${role?.substring(5)?.toLowerCase()?.encodeAsHTML()},
            	</g:if>
            	<g:else>
            	  ${role?.encodeAsHTML()}
            	</g:else>
              </g:each>
              </td>
            </tr>
                   
          </tbody>
        </table>
      </div>
      <div class="buttons">
        <g:form controller="requestmap">
          <input type="hidden" name="id" value="${requestmap?.id}" />
          <span class="button"><g:actionSubmit value="Edit" /></span>
          <span class="button"><g:actionSubmit value="Delete" /></span>
        </g:form>
      </div>
    </div>
  </body>
</html>
