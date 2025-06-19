<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8">
    <title>Scheduled Interviews</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<jsp:include page="../common/navbar.jsp"/>
<div class="container py-5">
    <jsp:include page="../common/messages.jsp"/>

    <h2 class="mb-4 text-primary">Scheduled Interviews</h2>

    <c:if test="${empty interviewMap}">
        <div class="alert alert-warning">No interviews scheduled.</div>
    </c:if>

    <c:if test="${not empty interviewMap}">
        <c:forEach items="${interviewMap}" var="entry">
            <h4 class="text-primary mt-5">${entry.key}</h4>
            <div class="row g-4">
                <c:forEach items="${entry.value}" var="interview">
                    <div class="col-md-6">
                        <div class="card shadow-sm h-100">
                            <div class="card-body">
                                <h5 class="card-title">${interview.applicant.name}</h5>
                                <p class="card-subtitle mb-2 text-muted">${interview.job.position}</p>

                                <p class="mb-1">
                                    🕐 <strong>Time:</strong> ${fn:substring(interview.time, 11, 16)}
                                </p>

                                <p class="mb-2">
                                    <span class="badge
                                        <c:choose>
                                            <c:when test="${interview.status == 'SCHEDULED'}">bg-success</c:when>
                                            <c:otherwise>bg-secondary</c:otherwise>
                                        </c:choose>">
                                            ${interview.status}
                                    </span>
                                </p>

                                <div class="d-flex gap-2">
                                    <a class="btn btn-sm btn-outline-warning"
                                       href="${pageContext.request.contextPath}/recruiter/interview/reschedule/${interview.id}">Reschedule</a>
                                    <a class="btn btn-sm btn-outline-danger"
                                       href="${pageContext.request.contextPath}/recruiter/interview/cancel/${interview.id}">Cancel</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:forEach>
    </c:if>
</div>

</body>
</html>
