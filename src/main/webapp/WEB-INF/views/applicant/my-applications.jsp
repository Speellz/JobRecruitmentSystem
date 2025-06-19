<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8">
    <title>My Applications & Interviews</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<jsp:include page="../common/navbar.jsp"/>
<div class="container py-5">
    <jsp:include page="../common/messages.jsp"/>

    <h2 class="mb-4 text-primary">My Applications</h2>

    <c:if test="${empty applications}">
        <div class="alert alert-info">You have not applied to any jobs yet.</div>
    </c:if>

    <div class="row g-4">
        <c:forEach var="application" items="${applications}">
            <div class="col-md-6">
                <div class="card shadow-sm h-100">
                    <div class="card-body">
                        <h5 class="card-title">${application.job.title}</h5>
                        <p class="card-subtitle mb-2 text-muted">${application.job.position}</p>

                        <p class="mb-1"><strong>Status:</strong> ${application.status}</p>

                        <c:if test="${interviewMap[application.id] != null}">
                            <p class="mb-1">
                                🗓️ <strong>Interview Date:</strong>
                                <fmt:formatDate value="${interviewTimeMap[application.id]}" pattern="dd MMM yyyy HH:mm"/>
                            </p>

                            <p class="mb-2">
                                <span class="badge
                                    <c:choose>
                                        <c:when test="${interviewMap[application.id].status == 'SCHEDULED'}">bg-success</c:when>
                                        <c:otherwise>bg-secondary</c:otherwise>
                                    </c:choose>">
                                        ${interviewMap[application.id].status}
                                </span>
                            </p>

                            <c:if test="${interviewFeedbackMap[application.id] != null}">
                                <hr>
                                <p class="mb-1">
                                    <strong>Feedback Result:</strong>
                                    <c:choose>
                                        <c:when test="${interviewFeedbackMap[application.id].result == 'APPROVED'}">
                                            <span class="text-success">Approved ✅</span>
                                        </c:when>
                                        <c:when test="${interviewFeedbackMap[application.id].result == 'REJECTED'}">
                                            <span class="text-danger">Rejected ❌</span>
                                        </c:when>
                                    </c:choose>
                                </p>

                                <p class="mb-1">
                                    <strong>Score:</strong> ${interviewFeedbackMap[application.id].score}/10
                                </p>
                            </c:if>
                        </c:if>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

</body>
</html>
