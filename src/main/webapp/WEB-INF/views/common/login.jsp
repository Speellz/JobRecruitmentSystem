<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<jsp:include page="navbar.jsp"/>

<c:set var="isBusinessPage" value="${sessionScope.roleType == 'business'}" />

<div class="container d-flex justify-content-center align-items-center" style="min-height: 80vh;">
    <jsp:include page="messages.jsp"/>
    <div class="card p-4 shadow-sm" style="min-width: 350px; max-width: 400px; width: 100%;">
        <h3 class="text-center mb-4">Login</h3>

        <form action="${pageContext.request.contextPath}/perform_login" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="mb-3">
                <input type="text" name="username" class="form-control" placeholder="Email" required>
            </div>

            <div class="mb-3">
                <input type="password" name="password" class="form-control" placeholder="Password" required>
            </div>

            <div class="d-grid">
                <button type="submit" class="btn btn-primary">Login</button>
            </div>
        </form>

        <div class="text-center mt-3">
            <c:choose>
                <c:when test="${isBusinessPage}">
                    <a href="${pageContext.request.contextPath}/auth/company-signup">Don't have an account? Sign up</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/auth/signup">Don't have an account? Sign up</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

</body>
</html>
