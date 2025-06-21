<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Job Applications</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">

<jsp:include page="../common/navbar.jsp"/>

<div class="container py-4">
    <h3 class="text-primary mb-4">Applications for: <span class="text-dark">${job.title}</span></h3>

    <c:if test="${empty applications}">
        <div class="alert alert-info">No applications found for this job.</div>
    </c:if>

    <c:forEach var="app" items="${applications}">
        <div class="card mb-3">
            <div class="card-body">
                <h5 class="card-title">${app.applicant.name}</h5>
                <p class="card-text">
                    <strong>Status:</strong> ${app.status} <br>
                    <strong>Cover Letter:</strong> ${app.coverLetter}
                </p>
                <a href="${pageContext.request.contextPath}/profile/applicant/${app.applicant.id}" class="btn btn-sm btn-outline-primary">View Profile</a>
            </div>
        </div>
    </c:forEach>
</div>

</body>
</html>
