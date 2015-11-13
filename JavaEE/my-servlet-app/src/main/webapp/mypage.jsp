<jsp:useBean id="name" scope="request" type=""/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>


<html>
<head>
    <title>MyPage.com</title>
</head>
<body>
    <h1>This application name is ${name}</h1><br>
    <h3> Here is all threads:</h3>
    <c:forEach var="value" items="${ThreadsList}">
        ${value}<br>
    </c:forEach>

    <h3> Here is all headers:</h3>
    <c:forEach var="value" items="${headerNames}">
        <br> ${value}<br>
    </c:forEach>
</body>
</html>
