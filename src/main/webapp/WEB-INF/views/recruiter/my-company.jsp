<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Company</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container my-5">
    <jsp:include page="../common/messages.jsp"/>
    <h1 class="text-center text-primary mb-4">My Company</h1>

    <div class="card shadow-sm p-4 mb-4">
        <h4 class="mb-3">🏢 Company Info</h4>
        <p><strong>Name:</strong> ${company.name}</p>
        <p><strong>Email:</strong> ${company.email}</p>
        <p><strong>Phone:</strong> ${company.phone}</p>
        <p><strong>Website:</strong> <a href="${company.website}" target="_blank">${company.website}</a></p>
    </div>

    <div class="card shadow-sm p-4 mb-4">
        <h4 class="mb-3">🏬 Branch Info</h4>
        <p><strong>Name:</strong> ${branch.name}</p>
        <p><strong>Location:</strong> ${branch.location}</p>
    </div>

    <div class="card shadow-sm p-4 mb-4">
        <h4 class="mb-3">👤 Branch Manager</h4>
        <c:choose>
            <c:when test="${not empty manager}">
                <p><strong>Name:</strong> ${manager.name}</p>
                <p><strong>Email:</strong> ${manager.email}</p>
            </c:when>
            <c:otherwise>
                <p class="text-warning">No manager assigned to this branch.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="card shadow-sm p-4 mb-4">
        <h4 class="mb-3">👥 Recruiters</h4>

        <c:forEach var="recruiter" items="${recruiters}">
            <div class="border rounded p-3 mb-3">
                <p><strong>Name:</strong> ${recruiter.user.name}</p>
                <p><strong>Email:</strong> ${recruiter.user.email}</p>
                <p><strong>Phone:</strong> ${recruiter.phone}</p>

                <c:if test="${isManager}">
                    <form action="${pageContext.request.contextPath}/recruiter/delete/${recruiter.id}" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <button class="btn btn-danger btn-sm" type="submit" onclick="return confirm('Are you sure?')">Delete</button>
                    </form>
                </c:if>
            </div>
        </c:forEach>
    </div>

    <c:if test="${isManager}">
        <div class="card shadow-sm p-4 mb-4">
            <h4 class="mb-3">➕ Add New Recruiter</h4>
            <form action="${pageContext.request.contextPath}/recruiter/add-by-manager" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                <div class="mb-3">
                    <input type="text" name="name" class="form-control" placeholder="Name" required>
                </div>
                <div class="mb-3">
                    <input type="email" name="email" class="form-control" placeholder="Email" required>
                </div>
                <div class="mb-3">
                    <input type="text" name="phone" class="form-control" placeholder="Phone" required>
                </div>
                <div class="mb-3">
                    <input type="password" name="password" class="form-control" placeholder="Password" required>
                </div>
                <button class="btn btn-primary" type="submit">Add Recruiter</button>
            </form>
        </div>
    </c:if>

    <div class="text-center">
        <a href="${pageContext.request.contextPath}/recruiter/dashboard" class="btn btn-secondary">← Back to Dashboard</a>
    </div>
</div>

</body>
</html>
