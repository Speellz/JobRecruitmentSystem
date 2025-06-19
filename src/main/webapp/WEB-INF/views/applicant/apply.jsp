<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Apply to Job</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<jsp:include page="../common/navbar.jsp" />

<div class="container my-5">
    <jsp:include page="../common/messages.jsp"/>
    <div class="card p-4 shadow mx-auto" style="max-width: 600px;">
        <h2 class="mb-4 text-center">
            Apply to: <span class="text-primary">${job.title}</span>
        </h2>

        <form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/job/${job.id}/apply">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <c:choose>
                <c:when test="${not empty sessionScope.loggedUser.cvUrl}">
                    <div class="alert alert-info">
                        Your CV is already uploaded:
                        <a href="${sessionScope.loggedUser.cvUrl}" target="_blank" class="btn btn-sm btn-outline-primary ms-2">View CV</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="mb-3">
                        <label class="form-label">CV File (PDF)</label>
                        <input type="file" name="cvFile" accept=".pdf" class="form-control" required />
                    </div>
                </c:otherwise>
            </c:choose>

            <div class="mb-4">
                <label class="form-label">Cover Letter</label>
                <textarea name="coverLetter" rows="6" class="form-control" placeholder="Write a brief message..." required></textarea>
            </div>

            <div class="mb-4">
                <label class="form-label">Referrer Email <span class="text-muted">(optional)</span></label>
                <input type="email" name="referrerEmail" class="form-control" placeholder="example@email.com" />
            </div>

            <button type="submit" class="btn btn-primary w-100">Submit Application</button>
        </form>
    </div>
</div>

</body>
</html>
