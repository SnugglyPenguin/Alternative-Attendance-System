<%-- 
    Document   : index
    Created on : 13-Feb-2019, 15:24:16
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
    <center>
    <body>
        <img src="Coventry Logo.jpg" alt="Coventry University Logo" height="234" width="402.66"/>
        <h1>Attendance Monitoring System</h1>
        <form action="verify.jsp" method="get">        
            Username<br/> 
            <input type="text" name="uname"/><br/>
            <br/>Password<br/>
            <input type="password" name="pass"/><br/>
            <br/><input type="submit" value="Log In" id="Button">
        </form>
    <center/>
    </body>
</html>
