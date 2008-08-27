  
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Authority List</title>
  </head>
  <body>
    <div class="nav">
      <span class="menuButton"><a href="${createLinkTo(dir:'')}">Home</a></span>
      <span class="menuButton"><g:link action="create">New Authority</g:link></span>
    </div>
    <div class="body">
      <h1>Authority List</h1>
      <g:if test="${flash.message}">
        <div class="message">
          ${flash.message}
        </div>
      </g:if>
      
      <table>
      
        <thead>
          <tr>
               
            <g:sortableColumn property="id" title="Id" />
                  
            <g:sortableColumn property="authority" title="Authority Name" />
                  
            <g:sortableColumn property="description" title="Description" />
                  
            <th></th>
          </tr>
        </thead>
        
        <tbody>
          <g:each in="${authorityList}">
            <tr>
                       
              <td>${it.id?.encodeAsHTML()}</td>
                       
              <td>${it.authority?.substring(5)?.toLowerCase()?.encodeAsHTML()}</td>
                       
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
        <g:paginate total="${Authority.count()}" />
      </div>
    </div>
  </body>
</html>
