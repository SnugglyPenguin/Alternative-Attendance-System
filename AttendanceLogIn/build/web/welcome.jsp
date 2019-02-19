<%-- 
    Document   : welcome
    Created on : 13-Feb-2019, 15:38:48
    Author     : Jake9
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="style.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%! String uname, pass; %>
        <%
            uname = request.getParameter("uname");
            pass = request.getParameter("pass");
        %>
        
    <center>
        <img src="Coventry Logo.jpg" alt="Coventry University Logo" height="234" width="402.66"/>
        <h1>Welcome, <%=uname%></h1>
        <br/><input type="submit" value="Generate QR Code" id="Button">
        <br/>
        <br/><input type="submit" value="Check attendance" id="Button">
        <br/>
        <form action="logout.jsp" method="get">
        <br/><input type="submit" value="Log Out" id="Button">
        <form/>
     <center/>
     
    </body>
</html>
