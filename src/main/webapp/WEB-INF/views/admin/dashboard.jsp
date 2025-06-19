<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<jsp:include page="../common/navbar.jsp" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>

<body class="bg-light">
<div class="container my-5">
    <jsp:include page="../common/messages.jsp"/>
    <h1 class="text-center mb-4">Admin Dashboard</h1>

    <c:if test="${not empty success}">
        <div class="alert alert-success">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <h4 class="mb-3">Pending Companies</h4>

    <c:if test="${empty pendingCompanies}">
        <div class="alert alert-info text-center">No pending companies to approve.</div>
    </c:if>

    <div class="row g-4">
        <c:forEach var="company" items="${pendingCompanies}">
            <div class="col-md-6 col-lg-4">
                <div class="card shadow-sm h-100">
                    <div class="card-body">
                        <p><strong>🧾 Name:</strong> ${company.name}</p>
                        <p><strong>📧 Email:</strong> ${company.email}</p>
                        <p><strong>📞 Phone:</strong> ${company.phone}</p>
                        <p><strong>🌐 Website:</strong> <a href="${company.website}" target="_blank">${company.website}</a></p>
                    </div>
                    <div class="card-footer d-flex justify-content-between">
                        <form action="${pageContext.request.contextPath}/admin/approve-company/${company.id}" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-success btn-sm">Approve</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/admin/reject-company/${company.id}" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-danger btn-sm">Reject</button>
                        </form>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
