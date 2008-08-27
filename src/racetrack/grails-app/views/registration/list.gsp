

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Registration List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Registration</g:link></span>
        </div>
        <div class="body">
            <h1>Registration List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="dateCreated" title="Date Created" />
                        
                   	        <g:sortableColumn property="email" title="Email" />
                        
                   	        <g:sortableColumn property="name" title="Name" />
                        
                   	        <th>Race</th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${registrationList}" status="i" var="registration">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${registration.id}">${registration.id?.encodeAsHTML()}</g:link></td>
                        
                            <td>${registration.dateCreated?.encodeAsHTML()}</td>
                        
                            <td>${registration.email?.encodeAsHTML()}</td>
                        
                            <td>${registration.name?.encodeAsHTML()}</td>
                        
                            <td>${registration.race?.encodeAsHTML()}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${Registration.count()}" />
            </div>
        </div>
    </body>
</html>
