<%-- 
    Document   : verify
    Created on : 13-Feb-2019, 15:30:44
    Author     : Jake9
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%! String uname, pass; %>
        <%
            uname = request.getParameter("uname");
            pass = request.getParameter("pass");
            if(uname.equals("student1")&&pass.equals("1234"))
                           {%>
                           <jsp:forward page="welcome.jsp"/>
                           <%} else
                                                         {%>
                           <jsp:include page="index.jsp"/>
                           <p>Incorrect Username or Password</p>
                           
                           <%}%>
    </body>
</html>
