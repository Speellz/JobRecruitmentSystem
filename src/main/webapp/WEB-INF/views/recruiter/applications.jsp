<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Applications</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<jsp:include page="../common/navbar.jsp" />

<div class="page-container">
    <div class="title-panel">
        <h2>Applications for: <span style="color:#0a66c2;">${job.title}</span></h2>
    </div>

    <div class="main-column">
        <c:forEach var="app" items="${applications}">
            <div class="branch-item">
                <div class="branch-info">
                    <div class="recruiter-name">${app.applicant.name}</div>
                    <div class="recruiter-info">Status: <strong>${app.status}</strong></div>
                    <div style="margin-top: 5px;">📄 <a class="view-link" href="${app.cvUrl}" target="_blank">View CV</a></div>
                    <div style="margin-top: 10px; font-size: 14px; color: #555;">
                        Cover Letter: ${app.coverLetter}
                    </div>
                </div>

                <div class="branch-actions">
                    <c:if test="${app.status == 'PENDING'}">
                        <form method="post" action="/recruiter/application/${app.id}/approve">
                            <button type="submit" class="button-blue">✅ Approve</button>
                        </form>
                        <form method="post" action="/recruiter/application/${app.id}/reject">
                            <button type="submit" class="button-red">❌ Reject</button>
                        </form>
                    </c:if>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty applications}">
            <div class="panel recruiter-panel">
                <p>No applications found for this job posting.</p>
            </div>
        </c:if>
    </div>
</div>

</body>
</html>
