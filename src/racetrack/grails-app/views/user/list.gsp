  
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Person List</title>
  </head>
  <body>
    <div class="nav">
      <span class="menuButton"><a href="${createLinkTo(dir:'')}">Home</a></span>
      <span class="menuButton"><g:link action="create">New Person</g:link></span>
    </div>
    <div class="body">
      <h1>Person List</h1>
      <g:if test="${flash.message}">
        <div class="message">
          ${flash.message}
        </div>
      </g:if>
      <table>
        <thead>
          <tr>
               
            <g:sortableColumn property="id" title="Id" />
                  
            <g:sortableColumn property="username" title="Login Name" />
                  
            <g:sortableColumn property="userRealName" title="Full Name" />
                  
            <g:sortableColumn property="enabled" title="Enabled" />
                  
            <g:sortableColumn property="description" title="Description" />
                  
            <th></th>
          </tr>
        </thead>
        <tbody>
          <g:each in="${personList}">
            <tr>
                       
              <td>${it.id?.encodeAsHTML()}</td>
                       
              <td>${it.username?.encodeAsHTML()}</td>
                       
              <td>${it.userRealName?.encodeAsHTML()}</td>
                       
              <td>${it.enabled?.encodeAsHTML()}</td>
                       
              <td>${it.description?.encodeAsHTML()}</td>
                       
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
        <g:paginate total="${Person.count()}" />
      </div>
      
    </div>
  </body>
</html>
