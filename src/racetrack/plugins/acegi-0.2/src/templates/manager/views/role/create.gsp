  
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Create ${authorityDomain}</title>         
  </head>
  <body>
    <div class="nav">
      <span class="menuButton"><a href="\${createLinkTo(dir:'')}">Home</a></span>
      <span class="menuButton"><g:link action="list">${authorityDomain} List</g:link></span>
    </div>
    <div class="body">
    
      <h1>Create ${authorityDomain}</h1>
      <g:if test="\${flash.message}">
        <div class="message">\${flash.message}</div>
      </g:if>
      <g:hasErrors bean="\${authority}">
        <div class="errors">
          <g:renderErrors bean="\${authority}" as="list" />
        </div>
      </g:hasErrors>
      
      <g:form action="save" method="post" >
      
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
                         value="\${authority?.authority?.encodeAsHTML()}"/>
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
            </tbody>
          </table>
        </div>
               
        <div class="buttons">
          <span class="formButton">
            <input type="submit" value="Create"></input>
          </span>
        </div>
        
      </g:form>
      
    </div>
  </body>
</html>
