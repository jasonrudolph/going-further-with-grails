  
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Show ${authorityDomain}</title>
  </head>
  
  <body>
  
    <div class="nav">
      <span class="menuButton"><a href="\${createLinkTo(dir:'')}">Home</a></span>
      <span class="menuButton"><g:link action="list">${authorityDomain} List</g:link></span>
      <span class="menuButton"><g:link action="create">New ${authorityDomain}</g:link></span>
    </div>
    
    <div class="body">
      <h1>Show ${authorityDomain}</h1>
      <g:if test="\${flash.message}">
        <div class="message">\${flash.message}</div>
      </g:if>
      <div class="dialog">
        <table>
                   
          <tbody>
                   
            <tr class="prop">
              <td valign="top" class="name">Id:</td>
              <td valign="top" class="value">\${authority.id}</td>
            </tr>
                   
            <tr class="prop">
              <td valign="top" class="name">${authorityDomain} Name:</td>
              <td valign="top" class="value">\${authority.authority.substring(5).toLowerCase()}</td>
            </tr>
                   
            <tr class="prop">
              <td valign="top" class="name">Description:</td>
              <td valign="top" class="value">\${authority.description}</td>
            </tr>
                   
            <tr class="prop">
              <td valign="top" class="name">People:</td>
              <td valign="top" class="value">\${authority.people}</td>
            </tr>
                   
          </tbody>
        </table>
      </div>
    
      <div class="buttons">
        <g:form controller="role">
          <input type="hidden" name="id" value="\${authority?.id}" />
          <span class="button"><g:actionSubmit value="Edit" /></span>
          <span class="button"><g:actionSubmit value="Delete" /></span>
        </g:form>
      </div>
      
    </div>
    
  </body>
</html>
