<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="df" uri="http://jobrecruitment.com/tags/dateformatter" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Applications</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<jsp:include page="../common/navbar.jsp" />

<div class="container my-5">
    <jsp:include page="../common/messages.jsp"/>
    <div class="mb-4">
        <h2>Applications for: <span class="text-primary">${job.title}</span></h2>
    </div>

    <c:forEach var="app" items="${applications}">
        <c:set var="application" value="${app.application}" />
        <div class="col-md-6 mb-4">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h5 class="card-title mb-2">${application.applicant.name}</h5>
                    <p class="mb-2">
                        <strong>Status:</strong>
                        <span class="badge
                            <c:choose>
                                <c:when test="${application.status == 'PENDING'}">bg-warning text-dark</c:when>
                                <c:when test="${application.status == 'APPROVED'}">bg-success</c:when>
                                <c:when test="${application.status == 'REJECTED'}">bg-danger</c:when>
                                <c:otherwise>bg-secondary</c:otherwise>
                            </c:choose>">
                                ${application.status}
                        </span>
                    </p>

                    <p><strong>Cover Letter:</strong><br>${application.coverLetter}</p>

                    <div class="d-flex gap-2 mb-2">
                        <a href="${application.cvUrl}" target="_blank" class="btn btn-sm btn-outline-primary">📄 View CV</a>
                        <a href="${pageContext.request.contextPath}/recruiter/application/${application.applicant.id}/profile"
                           class="btn btn-sm btn-outline-info">👤 View Profile</a>
                    </div>

                    <div class="d-flex gap-2 mt-3">
                        <c:if test="${application.status == 'PENDING'}">
                            <form method="post" action="/recruiter/application/${application.id}/approve">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit" class="btn btn-sm btn-success">✅ Approve</button>
                            </form>
                            <form method="post" action="/recruiter/application/${application.id}/reject">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit" class="btn btn-sm btn-warning">❌ Reject</button>
                            </form>
                        </c:if>
                        <c:if test="${application.status != 'PENDING'}">
                            <form method="post" action="/recruiter/application/${application.id}/remove">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit" class="btn btn-sm btn-danger">🗑 Remove</button>
                            </form>
                        </c:if>
                    </div>

                    <c:if test="${not empty app.referrer}">
                        <hr class="my-3">
                        <p class="text-muted mb-1">
                            👥 <strong>Referred by:</strong> ${app.referrer.name} (${app.referrer.email})
                        </p>
                        <a href="${pageContext.request.contextPath}/recruiter/application/${app.referrer.id}/profile"
                           class="btn btn-sm btn-outline-secondary">👤 View Referrer Profile</a>
                    </c:if>

                </div>
            </div>
        </div>
    </c:forEach>
</div>

</body>
</html>
