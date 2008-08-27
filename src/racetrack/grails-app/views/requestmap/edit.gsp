  
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Edit Requestmap</title>
  </head>
  <body>
    <div class="nav">
      <span class="menuButton"><a href="${createLinkTo(dir:'')}">Home</a></span>
      <span class="menuButton"><g:link action="list">Requestmap List</g:link></span>
      <span class="menuButton"><g:link action="create">New Requestmap</g:link></span>
    </div>
    <div class="body">
      <h1>Edit Requestmap</h1>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${requestmap}">
        <div class="errors">
          <g:renderErrors bean="${requestmap}" as="list" />
        </div>
      </g:hasErrors>
      <div class="prop">
	    <span class="name">Id:</span>
	    <span class="value">${requestmap?.id}</span>
      </div>           
      <g:form controller="requestmap" method="post" >
        <input type="hidden" name="id" value="${requestmap?.id}" />
        <div class="dialog">
          <table>
            <tbody>
			  <tr class='prop'>
			    <td valign='top' class='name'><label for='url'>URL:</label></td>
		    	<td valign='top' 
				   class='value ${hasErrors(bean:requestmap,field:'url','errors')}'>
				  <input type="text" name='url' 
				     value="${requestmap?.url?.encodeAsHTML()}"/>
				 </td>
			  </tr>
                       
			  <tr class='prop'>
			    <td valign='top' class='name'>
			      <label for='configAttribute'>Roles(split by a comma):</label>
			    </td>
				<td valign='top' 
				  class='value ${hasErrors(bean:requestmap,field:'configAttribute','errors')}'>
				  
				  <%
                    String[] configAttrs=
                        org.springframework.util.StringUtils.
                        commaDelimitedListToStringArray(requestmap.configAttribute);
                     
                    String roles="";
                  %>
			      <g:each var="role" in="${configAttrs}">
			        <g:if test="${role.startsWith('ROLE_')}">            
            	       <%roles+=role.substring(5).toLowerCase()+",";%>
            	    </g:if>
            	    <g:else>
            	       <%roles+=role;%>
            	    </g:else>
                  </g:each>
				  <input type="text" name='configAttribute' 
				     value="${roles?.encodeAsHTML()}"/>
				</td>
			  </tr>
                       
            </tbody>
          </table>
        </div>

        <div class="buttons">
          <span class="button"><g:actionSubmit value="Update" /></span>
          <span class="button"><g:actionSubmit value="Delete" /></span>
        </div>
      </g:form>
    </div>
  </body>
</html>
