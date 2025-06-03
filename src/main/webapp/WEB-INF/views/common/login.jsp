<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<jsp:include page="navbar.jsp"/>

<c:set var="isBusinessPage" value="${sessionScope.roleType == 'business'}" />

<div class="auth-container">
    <h2>Login</h2>

    <form action="<%= request.getContextPath() %>/perform_login" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="text" name="username" placeholder="Email" required>
        <input type="password" name="password" placeholder="Password" required>
        <button type="submit">Login</button>
    </form>

    <div style="margin-top: 15px;">
        <c:choose>
            <c:when test="${isBusinessPage}">
                <a href="<%= request.getContextPath() %>/auth/business-signup">Sign up</a>
            </c:when>
            <c:otherwise>
                <a href="<%= request.getContextPath() %>/auth/signup">Sign up</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>

</body>
</html>
