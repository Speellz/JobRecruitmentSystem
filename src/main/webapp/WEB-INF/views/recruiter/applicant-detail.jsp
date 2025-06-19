<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Applicant Detail</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<jsp:include page="../common/navbar.jsp" />

<div class="container my-5">
    <div class="card p-4 d-flex flex-row align-items-center gap-3 mb-4">
        <img src="<%= request.getContextPath() %>/img/default-profile.png" alt="Profile Image" class="rounded-circle" style="width:100px;height:100px;object-fit:cover;">
        <div>
            <h2 class="mb-1">${user.name}</h2>
            <p class="mb-0">${user.email}</p>
            <p class="text-muted">Role: ${user.role}</p>
        </div>
    </div>

    <!-- Education -->
    <div class="card p-4 mb-3">
        <h5 class="mb-3">🎓 Education</h5>
        <c:forEach var="edu" items="${educationList}">
            <div class="mb-2">
                <strong>${edu.institutionName}</strong> - ${edu.degree} in ${edu.fieldOfStudy}<br>
                <small class="text-muted">${edu.startDate} - ${edu.endDate}</small>
            </div>
        </c:forEach>
        <c:if test="${empty educationList}">
            <p class="text-muted">No education information available.</p>
        </c:if>
    </div>

    <!-- Employment History -->
    <div class="card p-4 mb-3">
        <h5 class="mb-3">💼 Employment History</h5>
        <c:forEach var="emp" items="${employmentList}">
            <div class="mb-2">
                <strong>${emp.companyName}</strong> - ${emp.jobTitle}<br>
                <small class="text-muted">${emp.startDate} - ${emp.endDate}</small>
            </div>
        </c:forEach>
        <c:if test="${empty employmentList}">
            <p class="text-muted">No employment history available.</p>
        </c:if>
    </div>

    <!-- Certifications -->
    <div class="card p-4 mb-3">
        <h5 class="mb-3">📜 Certifications</h5>
        <c:forEach var="cert" items="${certificationList}">
            <div class="mb-2">
                <strong>${cert.certificationName}</strong><br>
                <small class="text-muted">Issued by ${cert.issuedBy}</small>
            </div>
        </c:forEach>
        <c:if test="${empty certificationList}">
            <p class="text-muted">No certifications available.</p>
        </c:if>
    </div>

    <!-- Skills -->
    <div class="card p-4 mb-3">
        <h5 class="mb-3">🛠 Skills</h5>
        <c:forEach var="skill" items="${skillList}">
            <span class="badge bg-primary me-2 mb-2">${skill.skillName}</span>
        </c:forEach>
        <c:if test="${empty skillList}">
            <p class="text-muted">No skills listed.</p>
        </c:if>
    </div>
</div>

</body>
</html>
