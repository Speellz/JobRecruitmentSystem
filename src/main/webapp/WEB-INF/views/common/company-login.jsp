<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Business Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<jsp:include page="navbar.jsp"/>

<div class="container">
    <h2>Business Login</h2>

    <c:if test="${param.error != null}">
        <p class="error">Invalid email or password. Please try again.</p>
    </c:if>

    <form action="<%= request.getContextPath() %>/perform_business_login" method="post">
        <input type="email" name="username" placeholder="Email" required>
        <input type="password" name="password" placeholder="Password" required>
        <button type="submit">Login</button>
    </form>

    <p>Don't have an account? <a href="<%= request.getContextPath() %>/business/signup">Sign Up</a></p>
</div>

</body>
</html>
