<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="df" uri="http://jobrecruitment.com/tags/dateformatter" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Interview Feedback</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<jsp:include page="../common/navbar.jsp"/>
<div class="container py-5">
    <jsp:include page="../common/messages.jsp"/>

    <h2 class="mb-4 text-primary">Edit Feedback for ${interview.applicant.name}</h2>

    <div class="card shadow-sm">
        <div class="card-body">
            <p class="card-subtitle mb-2 text-muted">${interview.job.position}</p>
            <p><strong>Interview Time:</strong> ${df:format(interview.time)}</p>

            <form method="post" action="${pageContext.request.contextPath}/recruiter/interview/feedback/update">
                <input type="hidden" name="interviewId" value="${interview.id}" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                <div class="mb-3">
                    <label class="form-label">Feedback</label>
                    <textarea name="feedback" class="form-control" rows="4" required>${feedback.feedback}</textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label">Score</label>
                    <input type="number" name="score" class="form-control" min="0" max="10"
                           value="${feedback.score}" required />
                </div>

                <div class="mb-3">
                    <label class="form-label">Decision</label><br/>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="result" value="APPROVED"
                        ${feedback.result == 'APPROVED' ? 'checked' : ''} />
                        <label class="form-check-label">Approved</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="result" value="REJECTED"
                        ${feedback.result == 'REJECTED' ? 'checked' : ''} />
                        <label class="form-check-label">Rejected</label>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary w-100">Update Feedback</button>
            </form>
        </div>
    </div>
</div>

</body>
</html>
