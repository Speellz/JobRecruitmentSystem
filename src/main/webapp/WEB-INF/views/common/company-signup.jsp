<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Business Sign Up</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<jsp:include page="navbar.jsp"/>

<div class="container d-flex justify-content-center align-items-center" style="min-height: 80vh;">
    <jsp:include page="messages.jsp"/>
    <div class="card shadow p-4" style="width: 100%; max-width: 450px;">
        <h2 class="mb-4 text-center text-primary">Business Sign Up</h2>

        <form action="<%= request.getContextPath() %>/auth/company-signup" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <div class="mb-3">
                <input type="text" class="form-control" name="name" placeholder="Company Contact Name" required />
            </div>
            <div class="mb-3">
                <input type="email" class="form-control" name="email" placeholder="Email" required />
            </div>
            <div class="mb-4">
                <input type="password" class="form-control" name="password" placeholder="Password" required />
            </div>
            <button type="submit" class="btn btn-primary w-100">Register</button>
        </form>

        <div class="text-center mt-3">
            <a href="<%= request.getContextPath() %>/login">Already have an account? Login</a>
        </div>
    </div>
</div>

</body>
</html>
