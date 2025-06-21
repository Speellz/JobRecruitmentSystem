<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="df" uri="http://jobrecruitment.com/tags/dateformatter" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Job Details</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">
<jsp:include page="navbar.jsp" />

<div class="container my-5">
    <jsp:include page="messages.jsp"/>

    <div class="card shadow-sm">
        <div class="card-body">
            <h2 class="text-primary">${job.title}</h2>
            <p><strong>Position:</strong> ${job.position}</p>
            <p><strong>Company:</strong> ${job.company.name}</p>
            <p><strong>Branch:</strong> ${job.branch.name}</p>
            <p><strong>Recruiter:</strong> ${job.recruiter.user.name}</p>
            <p><strong>Location:</strong> ${job.location}</p>
            <p><strong>Type:</strong> ${job.employmentType}</p>
            <p><strong>Salary:</strong> ${job.salaryRange}</p>
            <p><strong>Posted At:</strong> ${df:format(job.createdAt)}</p>

            <hr>

            <h5>Description</h5>
            <p>${job.description}</p>

            <h5>Required Skills</h5>
            <ul class="list-group list-group-flush mb-3">
                <c:forEach var="skill" items="${skills}">
                    <li class="list-group-item">${skill.name}</li>
                </c:forEach>
            </ul>

            <sec:authorize access="hasAuthority('APPLICANT')">
                <c:choose>
                    <c:when test="${appliedMap[job.id]}">
                        <span class="btn btn-secondary disabled">Already Applied</span>

                        <c:if test="${not empty applicationId}">
                            <a href="#" class="btn btn-outline-primary ms-2 chat-link"
                               data-chat-url="${pageContext.request.contextPath}/messages/application/${applicationId}">
                                💬 Message Recruiter
                            </a>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/job/${job.id}/apply" class="btn btn-primary">Apply Now</a>
                    </c:otherwise>
                </c:choose>
            </sec:authorize>

            <sec:authorize access="!isAuthenticated()">
                <a href="${pageContext.request.contextPath}/auth/login" class="btn btn-outline-secondary">Login to Apply</a>
            </sec:authorize>
        </div>
    </div>
</div>

</body>
</html>
