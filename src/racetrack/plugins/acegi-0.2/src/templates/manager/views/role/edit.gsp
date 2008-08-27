  
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Edit ${authorityDomain}</title>
  </head>
  <body>
    <div class="nav">
      <span class="menuButton"><a href="\${createLinkTo(dir:'')}">Home</a></span>
      <span class="menuButton"><g:link action="list">${authorityDomain} List</g:link></span>
      <span class="menuButton"><g:link action="create">New ${authorityDomain}</g:link></span>
    </div>
    <div class="body">
      <h1>Edit ${authorityDomain}</h1>
      <g:if test="\${flash.message}">
        <div class="message">\${flash.message}</div>
      </g:if>
      <g:hasErrors bean="\${authority}">
        <div class="errors">
          <g:renderErrors bean="\${authority}" as="list" />
        </div>
      </g:hasErrors>
      <div class="prop">
        <span class="name">Id:</span>
	    <span class="value">\${authority?.id}</span>
      </div>           
      <g:form controller="role" method="post" >
        <input type="hidden" name="id" value="\${authority?.id}" />
        <div class="dialog">
          <table>
            <tbody>     
			  <tr class='prop'>
			    <td valign='top' class='name'>
			    	<label for='authority'>${authorityDomain} Name:</label>
			    </td>
				<td valign='top' 
					class='value \${hasErrors(bean:authority,field:'authority','errors')}'>
				  <input type="text" name='authority' 
						 value="\${authority?.authority?.substring(5)?.toLowerCase()?.encodeAsHTML()}"/>
				</td>
			  </tr>
						
              <tr class='prop'>
                <td valign='top' class='name'>
                	<label for='description'>Description:</label>
                </td>
                <td valign='top' 
                    class='value \${hasErrors(bean:authority,field:'description','errors')}'>
                  <input type="text" name='description' 
                         value="\${authority?.description?.encodeAsHTML()}"/>
                </td>
              </tr>
                        
			  <tr class='prop'>
			    <td valign='top' class='name'>
			      <label for='people'>People:</label>
			    </td>
				<td valign='top' 
				    class='value \${hasErrors(bean:authority,field:'people','errors')}'>
				  <ul>
    			    <g:each var='p' in='\${authority?.people?}'>
        			  <li>\${p}</li>
    				</g:each>
				  </ul>
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
