<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Job Posting (Admin)</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container py-5">
    <h2 class="mb-4 text-primary">Edit Job Posting</h2>

    <form method="post" action="${pageContext.request.contextPath}/admin/job/${jobPosting.id}/edit">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

        <div class="mb-3">
            <label class="form-label">Title</label>
            <input type="text" name="title" class="form-control" value="${jobPosting.title}" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Position</label>
            <input type="text" name="position" class="form-control" value="${jobPosting.position}" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Description</label>
            <textarea name="description" class="form-control" rows="4" required>${jobPosting.description}</textarea>
        </div>

        <div class="mb-3">
            <label class="form-label">Location</label>
            <input type="text" name="location" class="form-control" value="${jobPosting.location}" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Salary Range</label>
            <input type="text" name="salaryRange" class="form-control" value="${jobPosting.salaryRange}" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Employment Type</label>
            <input type="text" name="employmentType" class="form-control" value="${jobPosting.employmentType}" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Category</label>
            <select name="categoryId" class="form-select" required>
                <c:forEach var="cat" items="${categoryList}">
                    <option value="${cat.id}" ${cat.id == jobPosting.category.id ? "selected" : ""}>${cat.name}</option>
                </c:forEach>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Save Changes</button>
        <a href="${pageContext.request.contextPath}/" class="btn btn-secondary ms-2">Cancel</a>
    </form>
</div>

</body>
</html>
