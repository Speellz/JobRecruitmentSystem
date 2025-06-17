<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="df" uri="http://jobrecruitment.com/tags/dateformatter" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Job Details</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<jsp:include page="navbar.jsp" />

<div class="page-container">
    <div class="panel">
        <h2 style="color: #0a66c2;">${job.title}</h2>
        <p><strong>Position:</strong> ${job.position}</p>
        <p><strong>Company:</strong> ${job.company.name}</p>
        <p><strong>Branch:</strong> ${job.branch.name}</p>
        <p><strong>Recruiter:</strong> ${job.recruiter.user.name}</p>
        <p><strong>Location:</strong> ${job.location}</p>
        <p><strong>Type:</strong> ${job.employmentType}</p>
        <p><strong>Salary:</strong> ${job.salaryRange}</p>
        <p><strong>Posted At:</strong> ${df:format(job.createdAt)}</p>

        <h3>Description</h3>
        <p>${job.description}</p>

        <h3>Required Skills</h3>
        <ul class="skill-list">
            <c:forEach var="skill" items="${skills}">
                <li>${skill.name}</li>
            </c:forEach>
        </ul>


        <sec:authorize access="hasAuthority('APPLICANT')">
            <a href="${pageContext.request.contextPath}/job/${job.id}/apply" class="button-blue">Apply Now</a>
        </sec:authorize>

        <sec:authorize access="!isAuthenticated()">
            <a href="${pageContext.request.contextPath}/auth/login" class="button">Login to Apply</a>
        </sec:authorize>
    </div>
</div>

</body>
</html>
