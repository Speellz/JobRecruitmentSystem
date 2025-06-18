<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Branch Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">

<div class="container my-5">
    <div class="card shadow-sm mx-auto" style="max-width: 800px;">
        <div class="card-body">
            <h2 class="card-title text-center mb-4">Branch Detail</h2>

            <ul class="list-group list-group-flush mb-4">
                <li class="list-group-item"><strong>🏢 Name:</strong> ${branch.name}</li>
                <li class="list-group-item"><strong>📍 Location:</strong> ${branch.location}</li>
                <li class="list-group-item"><strong>📅 Created At:</strong> ${formattedCreatedAt}</li>
                <li class="list-group-item"><strong>🏢 Company:</strong> ${branch.company.name}</li>
            </ul>

            <c:if test="${not empty branch.manager}">
                <h5 class="mt-4">Branch Manager</h5>
                <ul class="list-group list-group-flush mb-4">
                    <li class="list-group-item"><strong>👤 Name:</strong> ${branch.manager.name}</li>
                    <li class="list-group-item"><strong>📧 Email:</strong> ${branch.manager.email}</li>
                </ul>
            </c:if>

            <h5 class="mt-4">Recruiters in this Branch</h5>
            <c:choose>
                <c:when test="${not empty recruiters}">
                    <div class="list-group mb-3">
                        <c:forEach var="recruiter" items="${recruiters}">
                            <div class="list-group-item">
                                <p class="mb-1"><strong>👤 Name:</strong> ${recruiter.user.name}</p>
                                <p class="mb-1"><strong>📧 Email:</strong> ${recruiter.user.email}</p>
                                <p class="mb-0"><strong>📞 Phone:</strong> ${recruiter.phone}</p>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-warning">No recruiters found in this branch.</div>
                </c:otherwise>
            </c:choose>

            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/company/branches" class="btn btn-secondary">← Back to Branches</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>
