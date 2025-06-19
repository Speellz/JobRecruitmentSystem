<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Branch Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>

<body class="bg-light">
<div class="container my-5">
    <jsp:include page="../common/messages.jsp"/>
    <h1 class="text-center mb-4">Branch Detail</h1>

    <div class="card shadow-sm mb-4">
        <div class="card-body">
            <p><strong>🏢 Name:</strong> ${branch.name}</p>
            <p><strong>📍 Location:</strong> ${branch.location}</p>
        </div>
    </div>

    <!-- Manager Section -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
            <h5 class="mb-0">Manager</h5>
            <c:choose>
                <c:when test="${not empty branch.manager}">
                    <a href="${pageContext.request.contextPath}/admin/branches/${branch.id}/edit-manager" class="btn btn-light btn-sm">Edit</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/admin/branches/${branch.id}/assign-manager" class="btn btn-light btn-sm">Assign</a>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${not empty branch.manager}">
                    <p><strong>👤 Name:</strong> ${branch.manager.name}</p>
                    <p><strong>📧 Email:</strong> ${branch.manager.email}</p>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-warning mb-0">No manager assigned to this branch.</div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Recruiters Section -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-info text-white">
            <h5 class="mb-0">Recruiters in this Branch</h5>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${not empty recruiters}">
                    <div class="row row-cols-1 row-cols-md-2 g-3">
                        <c:forEach var="recruiter" items="${recruiters}">
                            <div class="col">
                                <div class="card h-100">
                                    <div class="card-body">
                                        <p><strong>👤 Name:</strong> ${recruiter.user.name}</p>
                                        <p><strong>📧 Email:</strong> ${recruiter.user.email}</p>
                                        <p><strong>📞 Phone:</strong> ${recruiter.phone}</p>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-light">No recruiters found in this branch.</div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Back Button -->
    <div class="text-center mt-4">
        <a href="${pageContext.request.contextPath}/admin/companies/${branch.company.id}" class="btn btn-secondary">← Back to Company</a>
    </div>
</div>
</body>
</html>
