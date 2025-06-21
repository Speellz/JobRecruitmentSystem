<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login | JobRecruit</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: "Segoe UI", sans-serif;
            background-color: #f8f9fa;
        }

        .auth-container {
            position: relative;
            width: 100vw;
            height: 100vh;
            overflow: hidden;
        }

        .form-panel {
            position: absolute;
            width: 50%;
            height: 100%;
            background: #fff;
            padding: 60px;
            box-sizing: border-box;
            transition: all 0.6s ease;
            display: flex;
            flex-direction: column;
            justify-content: center;
            z-index: 2;
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
            border-radius: 0 20px 20px 0;
        }

        .signup-panel {
            right: -100%;
            left: auto;
            border-radius: 20px 0 0 20px;
        }

        .login-panel {
            left: 0;
        }

        .image-panel {
            position: absolute;
            width: 50%;
            height: 100%;
            background-size: cover;
            background-position: center;
            transition: all 0.6s ease;
            z-index: 1;
            border-radius: 20px;
        }

        .login-img {
            right: 0;
            background-image: url('${pageContext.request.contextPath}/uploads/login.png');
        }

        .signup-img {
            left: -100%;
            background-image: url('${pageContext.request.contextPath}/uploads/signup.png');
        }

        #formToggle:checked ~ .auth-container .login-panel {
            left: -100%;
        }

        #formToggle:checked ~ .auth-container .signup-panel {
            right: 0;
        }

        #formToggle:checked ~ .auth-container .login-img {
            right: -100%;
        }

        #formToggle:checked ~ .auth-container .signup-img {
            left: 0;
        }

        .toggle-label {
            text-align: center;
            color: #007bff;
            cursor: pointer;
            margin-top: 20px;
            transition: color 0.3s ease;
        }

        .toggle-label:hover {
            color: #0056b3;
            text-decoration: underline;
        }

        .form-control {
            margin-bottom: 15px;
            border-radius: 8px;
            padding: 10px 14px;
            box-shadow: inset 0 1px 2px rgba(0,0,0,0.05);
            border: 1px solid #ced4da;
        }

        .btn {
            width: 100%;
            border-radius: 8px;
            padding: 10px;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .btn-primary:hover {
            background-color: #0056b3;
        }

        .btn-success:hover {
            background-color: #157347;
        }
    </style>
</head>

<body>
<jsp:include page="navbar.jsp"/>

<input type="checkbox" id="formToggle" hidden />

<%-- Sign-up yönlendirmesi: roleType = business ise company-signup --%>
<c:choose>
    <c:when test="${sessionScope.roleType == 'business'}">
        <c:set var="signupAction" value="${pageContext.request.contextPath}/auth/company-signup" />
    </c:when>
    <c:otherwise>
        <c:set var="signupAction" value="${pageContext.request.contextPath}/auth/signup" />
    </c:otherwise>
</c:choose>

<div class="auth-container">

    <!-- Login Form -->
    <div class="form-panel login-panel">
        <h3 class="text-center mb-4">Login</h3>
        <form action="${pageContext.request.contextPath}/perform_login" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <input type="text" name="username" placeholder="Email" class="form-control" required />
            <input type="password" name="password" placeholder="Password" class="form-control" required />

            <button type="submit" class="btn btn-primary">Login</button>
        </form>
        <label for="formToggle" class="toggle-label">Don't have an account? Sign up</label>
    </div>

    <!-- Signup Form -->
    <div class="form-panel signup-panel">
        <h3 class="text-center mb-4">
            <c:choose>
                <c:when test="${sessionScope.roleType == 'business'}">
                    Business Sign Up
                </c:when>
                <c:otherwise>
                    Sign Up
                </c:otherwise>
            </c:choose>
        </h3>
        <form id="signupForm" action="${signupAction}" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <input type="text" name="name" placeholder="Full Name" class="form-control" required />
            <input type="email" name="email" placeholder="Email" class="form-control" required />
            <input type="password" name="password" placeholder="Password" class="form-control" required />

            <button type="submit" class="btn btn-success">Sign Up</button>
        </form>
        <label for="formToggle" class="toggle-label">Already have an account? Login</label>
    </div>


    <!-- Image Panels -->
    <div class="image-panel login-img"></div>
    <div class="image-panel signup-img"></div>

</div>
</body>
</html>
