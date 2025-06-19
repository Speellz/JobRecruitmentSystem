<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="df" uri="http://jobrecruitment.com/tags/dateformatter" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Company Feedback</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<jsp:include page="../common/navbar.jsp" />
<div class="container py-5">
    <jsp:include page="../common/messages.jsp"/>

    <h2 class="mb-4 text-primary">Give Company Feedback</h2>

    <c:if test="${empty interviews}">
        <div class="alert alert-info">No interviews eligible for company feedback.</div>
    </c:if>

    <div class="row g-4">
        <c:forEach var="interview" items="${interviews}">
            <c:set var="companyId" value="${interview.job.company.id}" />
            <div class="col-md-6">
                <div class="card shadow-sm h-100">
                    <div class="card-body">
                        <h5 class="card-title">${interview.job.company.name}</h5>
                        <p class="card-subtitle mb-2 text-muted">${interview.job.position}</p>
                        <p><strong>Interview Time:</strong> ${df:format(interview.time)}</p>

                        <c:choose>
                            <c:when test="${reviewMap[companyId] != null}">
                                <div class="alert alert-success mt-3">
                                    You already submitted feedback ✅<br/>
                                    <strong>Rating:</strong> ${reviewMap[companyId].rating}/5<br/>
                                    <strong>Review:</strong> ${reviewMap[companyId].reviewText}
                                </div>
                            </c:when>

                            <c:otherwise>
                                <form method="post" action="${pageContext.request.contextPath}/applicant/company/feedback/submit">
                                    <input type="hidden" name="interviewId" value="${interview.id}" />
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                                    <div class="mb-3">
                                        <label class="form-label">Rating (1-5)</label>
                                        <input type="number" name="rating" class="form-control" min="1" max="5" required />
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Review</label>
                                        <textarea name="reviewText" class="form-control" rows="4" required></textarea>
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
