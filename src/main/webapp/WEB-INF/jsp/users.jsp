<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Users</title>
</head>
<body>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
            <tr>
                <td>Name</td>
                <td>Email</td>
                <td>Enabled</td>
                <td>Registered</td>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${users}" var="user">
                <jsp:useBean id="user" type="com.train4game.social.model.User"/>
                <tr>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.enabled}</td>
                    <td>${user.registered}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
