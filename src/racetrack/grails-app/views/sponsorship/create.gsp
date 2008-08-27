<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Sponsorship</title>         
    </head>
    <body>
        <div class="body">
            <h1>Create Sponsorship</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${sponsorship}">
            <div class="errors">
                <g:renderErrors bean="${sponsorship}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="companyName">Company Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:sponsorship,field:'companyName','errors')}">
                                    <input type="text" id="companyName" name="companyName" value="${fieldValue(bean:sponsorship,field:'companyName')}"/>
                                </td>
                            </tr> 
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="phoneNumber">Phone Number:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:sponsorship,field:'phoneNumber','errors')}">
                                    <input type="text" id="phoneNumber" name="phoneNumber" value="${fieldValue(bean:sponsorship,field:'phoneNumber')}"/>
                                </td>
                            </tr> 
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Create" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
