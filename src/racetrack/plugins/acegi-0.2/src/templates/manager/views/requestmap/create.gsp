  
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Create ${requestmapDomain}</title>         
  </head>
  <body>
    <div class="nav">
      <span class="menuButton"><a href="\${createLinkTo(dir:'')}">Home</a></span>
      <span class="menuButton"><g:link action="list">${requestmapDomain} List</g:link></span>
    </div>
    <div class="body">
      <h1>Create ${requestmapDomain}</h1>
      <g:if test="\${flash.message}">
        <div class="message">\${flash.message}</div>
      </g:if>
      <g:hasErrors bean="\${requestmap}">
        <div class="errors">
          <g:renderErrors bean="\${requestmap}" as="list" />
        </div>
      </g:hasErrors>
      <g:form action="save" method="post" >
        <div class="dialog">
          <table>
            <tbody>
                    
			  <tr class='prop'>
			    <td valign='top' class='name'><label for='url'>URL:</label></td>
				<td valign='top' class='value \${hasErrors(bean:requestmap,field:'url','errors')}'>
				  <input type="text" name='url' value="\${requestmap?.url?.encodeAsHTML()}"/>
				</td>
			  </tr>
                       
              <tr class='prop'>
                <td valign='top' class='name'>
                  <label for='configAttribute'>${authorityDomain}(split by a comma):</label>
                </td>
                <td valign='top' 
                	class='value \${hasErrors(bean:requestmap,field:'configAttribute','errors')}'>
                  <input type="text" name='configAttribute' 
                  	value="\${requestmap?.configAttribute?.encodeAsHTML()}"/>
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
