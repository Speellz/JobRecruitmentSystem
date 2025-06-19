<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Notifications</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<jsp:include page="../common/navbar.jsp" />

<div class="container d-flex justify-content-center align-items-center" style="min-height: 80vh;">
    <div class="card p-4 shadow-sm w-100" style="max-width: 600px;">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h4 class="mb-0"><i class="fa-solid fa-bell me-2"></i>Notifications</h4>
            <form method="post" action="${pageContext.request.contextPath}/notifications/clear">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <button class="btn btn-outline-danger btn-sm"><i class="fa-solid fa-trash"></i> Clear All</button>
            </form>
        </div>

        <c:if test="${empty notifications}">
            <div class="alert alert-info"><i class="fa-solid fa-circle-info me-2"></i>No notifications found.</div>
        </c:if>

        <ul class="list-group">
            <c:forEach var="notif" items="${notifications}">
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    <div><i class="fa-regular fa-envelope me-2 text-primary"></i>${notif.message}</div>
                    <form method="post" action="${pageContext.request.contextPath}/notifications/delete/${notif.id}">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <button class="btn btn-sm btn-outline-danger"><i class="fa-solid fa-xmark"></i></button>
                    </form>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>

</body>
</html>
