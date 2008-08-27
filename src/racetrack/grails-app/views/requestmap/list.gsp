  
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Requestmap List</title>
  </head>
  <body>
  	<div class="nav">
      <span class="menuButton"><a href="${createLinkTo(dir:'')}">Home</a></span>
      <span class="menuButton"><g:link action="create">New Requestmap</g:link></span>
    </div>
    <div class="body">
      <h1>Requestmap List</h1>
      <g:if test="${flash.message}">
        <div class="message">
          ${flash.message}
        </div>
      </g:if>
      <table>
        <thead>
          <tr>
               
            <g:sortableColumn property="id" title="Id" />
                  
            <g:sortableColumn property="url" title="URL" />
                  
            <g:sortableColumn property="configAttribute" title="Roles" />
                  
            <th></th>
          </tr>
        </thead>
        <tbody>
          <g:each in="${requestmapList}">
          <tr>
                       
            <td>${it.id?.encodeAsHTML()}</td>
                       
            <td>${it.url?.encodeAsHTML()}</td>
                       
            <td>
		
              <%
                String[] configAttrs=
                        org.springframework.util.StringUtils.
                        commaDelimitedListToStringArray(it.configAttribute);
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
                     
            <td class="actionButtons">
              <span class="actionButton">
                <g:link action="show" id="${it.id}">Show</g:link>
              </span>
            </td>
          </tr>
          </g:each>
        </tbody>
      </table>
      <div class="paginateButtons">
        <g:paginate total="${Requestmap.count()}" />
      </div>
    </div>
  </body>
</html>
