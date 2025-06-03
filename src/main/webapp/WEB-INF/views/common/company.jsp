<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Business Home | JobRecruit</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<jsp:include page="navbar.jsp"/>

<div class="container">
    <h1>Welcome to JobRecruit for Businesses</h1>
    <p>
        Find the best talents for your company and manage your hiring process easily.
        JobRecruit offers a powerful recruitment management system for businesses.
    </p>

    <div class="features">
        <h2>Why Join Us?</h2>
        <ul>
            <li>📌 Post unlimited job listings</li>
            <li>📌 Manage applications and track candidates</li>
            <li>📌 Organize recruitment teams and assign recruiters</li>
            <li>📌 Simplify your hiring process with our intuitive dashboard</li>
        </ul>
    </div>

    <div class="cta-buttons">
        <a href="<%= request.getContextPath() %>/business/signup" class="btn btn-primary">Sign Up</a>
        <a href="<%= request.getContextPath() %>/business/business-login" class="btn btn-secondary">Login</a>
    </div>
</div>

</body>
</html>
