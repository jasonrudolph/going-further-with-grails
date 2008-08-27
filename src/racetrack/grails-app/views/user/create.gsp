  
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Create Person</title>         
  </head>
  <body>
    <div class="nav">
      <span class="menuButton"><a href="${createLinkTo(dir:'')}">Home</a></span>
      <span class="menuButton"><g:link action="list">Person List</g:link></span>
    </div>
    <div class="body">
      <h1>Create Person</h1>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${person}">
        <div class="errors">
          <g:renderErrors bean="${person}" as="list" />
        </div>
      </g:hasErrors>
      <g:form action="save" method="post" >
        <div class="dialog">
          <table>
            <tbody>

              <tr class='prop'>
                <td valign='top' class='name'>
                  <label for='username'>Login Name:</label>
                </td>
                <td valign='top' 
                	class='value ${hasErrors(bean:person,field:'username','errors')}'>
                  <input type="text" name='username' 
                  		 value="${person?.username?.encodeAsHTML()}"/>
                </td>
              </tr>
                       
              <tr class='prop'>
                <td valign='top' class='name'>
                  <label for='userRealName'>Full Name:</label>
                </td>
                <td valign='top' 
                    class='value ${hasErrors(bean:person,field:'userRealName','errors')}'>
                  <input type="text" name='userRealName' 
                  		 value="${person?.userRealName?.encodeAsHTML()}"/>
                </td>
              </tr>
                       
              <tr class='prop'>
                <td valign='top' class='name'>
                  <label for='passwd'>Password:</label>
                </td>
                <td valign='top' 
                    class='value ${hasErrors(bean:person,field:'passwd','errors')}'>
                  <input type="password" name='passwd' 
                         value="${person?.passwd?.encodeAsHTML()}"/>
                </td>
              </tr>
                       
              <tr class='prop'>
                <td valign='top' class='name'>
                  <label for='enabled'>Enabled:</label>
                </td>
                <td valign='top' 
                	class='value ${hasErrors(bean:person,field:'enabled','errors')}'>
                  <g:checkBox name='enabled' value="${person?.enabled}" ></g:checkBox>
                </td>
              </tr>
                       
              <tr class='prop'>
                <td valign='top' class='name'>
                  <label for='description'>Description:</label>
                </td>
                <td valign='top' 
                    class='value ${hasErrors(bean:person,field:'description','errors')}'>
                  <input type="text" name='description' 
                         value="${person?.description?.encodeAsHTML()}"/>
                </td>
              </tr>
                       
              <tr class='prop'>
                <td valign='top' class='name'>
                  <label for='email'>Email:</label>
                </td>
                <td valign='top' 
                    class='value ${hasErrors(bean:person,field:'email','errors')}'>
                  <input type="text" name='email' 
                         value="${person?.email?.encodeAsHTML()}"/>
                </td>
              </tr>
                       
              <tr class='prop'>
                <td valign='top' class='name'>
                  <label for='email_show'>Show Email:</label>
                </td>
                <td valign='top' 
                    class='value ${hasErrors(bean:person,field:'email_show','errors')}'>
                  <g:checkBox name='email_show' value="${person?.email_show}" >
                  </g:checkBox>
                </td>
              </tr>
                        
              <tr class='prop'>
                <td valign='top' class='name' align="left">Assign Roles:</td>
              </tr>
            
              <g:each in="${authorityList}">
	    	  <tr>
	     	    <td valign='top' class='name' align="left">
	     	      ${it.authority?.substring(5)?.toLowerCase()?.encodeAsHTML()}
	     	    </td>
	     	    <td align="left">
	     	      <g:checkBox name='${it.authority}' value="${false}" ></g:checkBox>
	     	    </td>
	    	  </tr>
	          </g:each>
	    				
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
