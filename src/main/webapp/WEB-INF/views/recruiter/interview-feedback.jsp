<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="df" uri="http://jobrecruitment.com/tags/dateformatter" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Interview Feedback</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<jsp:include page="../common/navbar.jsp" />
<div class="container py-5">
    <jsp:include page="../common/messages.jsp"/>

    <h2 class="mb-4 text-primary">Give Interview Feedback</h2>

    <c:if test="${empty interviews}">
        <div class="alert alert-info">No past or due interviews available for feedback.</div>
    </c:if>

    <div class="row g-4">
        <c:forEach var="interview" items="${interviews}">
            <div class="col-md-6">
                <div class="card shadow-sm h-100">
                    <div class="card-body">
                        <h5 class="card-title">${interview.applicant.name}</h5>
                        <p class="card-subtitle mb-2 text-muted">${interview.job.position}</p>
                        <p><strong>Interview Time:</strong> ${df:format(interview.time)}</p>

                        <c:choose>
                            <c:when test="${feedbackMap[interview.id] != null}">
                                <div class="alert alert-success mt-3">
                                    Feedback already submitted ✅
                                    <br/><strong>Result:</strong> ${feedbackMap[interview.id].result}
                                    <br/><strong>Score:</strong> ${feedbackMap[interview.id].score}/10
                                    <br/><strong>Comment:</strong> ${feedbackMap[interview.id].feedback}
                                </div>

                                <div class="mt-3 text-end">
                                    <a class="btn btn-sm btn-outline-primary"
                                       href="${pageContext.request.contextPath}/recruiter/interview/feedback/edit/${interview.id}">
                                        Edit Feedback
                                    </a>
                                </div>
                            </c:when>

                            <c:otherwise>
                                <form method="post" action="${pageContext.request.contextPath}/recruiter/interview/feedback/submit">
                                    <input type="hidden" name="interviewId" value="${interview.id}" />
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                                    <div class="mb-3">
                                        <label class="form-label">Feedback</label>
                                        <textarea name="feedback" class="form-control" rows="4" required></textarea>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Score</label>
                                        <input type="number" name="score" class="form-control" min="0" max="10" required />
                                        <small class="text-muted">Score must be between 0 and 10</small>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Decision</label><br/>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" name="result" value="APPROVED" required />
                                            <label class="form-check-label">Approved</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" name="result" value="REJECTED" required />
                                            <label class="form-check-label">Rejected</label>
                                        </div>
                                    </div>

                                    <button type="submit" class="btn btn-primary w-100">Submit Feedback</button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

</body>
</html>
