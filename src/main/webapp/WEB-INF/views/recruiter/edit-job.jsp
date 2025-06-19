<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Job Posting</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<jsp:include page="../common/navbar.jsp" />

<div class="container my-5">
    <jsp:include page="../common/messages.jsp"/>
    <div class="card shadow-sm p-4">
        <h2 class="mb-4 text-center text-primary">Edit Job Posting</h2>

        <form method="post" action="${pageContext.request.contextPath}/recruiter/job/${jobPosting.id}/update">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <div class="mb-3">
                <label class="form-label">Title:</label>
                <input type="text" name="title" class="form-control" value="${jobPosting.title}" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Position:</label>
                <input type="text" name="position" class="form-control" value="${jobPosting.position}" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Description:</label>
                <textarea name="description" rows="5" class="form-control" required>${jobPosting.description}</textarea>
            </div>

            <div class="mb-3">
                <label class="form-label">Location:</label>
                <input type="text" name="location" class="form-control" value="${jobPosting.location}" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Employment Type:</label>
                <select name="employmentType" class="form-select">
                    <option value="Full-time" ${jobPosting.employmentType == 'Full-time' ? 'selected' : ''}>Full-time</option>
                    <option value="Part-time" ${jobPosting.employmentType == 'Part-time' ? 'selected' : ''}>Part-time</option>
                    <option value="Contract" ${jobPosting.employmentType == 'Contract' ? 'selected' : ''}>Contract</option>
                    <option value="Internship" ${jobPosting.employmentType == 'Internship' ? 'selected' : ''}>Internship</option>
                </select>
            </div>

            <div class="mb-3">
                <label class="form-label">Salary Range:</label>
                <input type="text" name="salaryRange" class="form-control" value="${jobPosting.salaryRange}">
            </div>

            <div class="mb-4">
                <label class="form-label">Category:</label>
                <select name="categoryId" class="form-select">
                    <c:forEach var="cat" items="${categoryList}">
                        <option value="${cat.id}" ${cat.id == jobPosting.category.id ? 'selected' : ''}>${cat.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="text-center">
                <button type="submit" class="btn btn-primary px-4">Update Job</button>
            </div>
        </form>
        <form method="post" action="${pageContext.request.contextPath}/recruiter/job/${jobPosting.id}/delete"
              onsubmit="return confirm('Are you sure you want to delete this job?');">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <button type="submit" class="btn btn-outline-danger w-100 mt-3">Delete Job</button>
        </form>
    </div>
</div>

</body>
</html>
