<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Job Posting</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<jsp:include page="../common/navbar.jsp" />

<div class="container my-5">
    <jsp:include page="../common/messages.jsp"/>
    <div class="card p-4 shadow-sm">
        <h2 class="mb-4 text-center text-primary">Add Job Posting</h2>

        <form action="${pageContext.request.contextPath}/recruiter/job/add" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <div class="mb-3">
                <label class="form-label">Title</label>
                <input type="text" name="title" class="form-control" required placeholder="CutechDev Software Engineer">
            </div>

            <div class="mb-3">
                <label class="form-label">Position</label>
                <input type="text" name="position" class="form-control" required placeholder="Software Engineer">
            </div>

            <div class="mb-3">
                <label class="form-label">Description</label>
                <textarea name="description" rows="4" class="form-control" required></textarea>
            </div>

            <div class="mb-3">
                <label class="form-label">Category</label>
                <select name="categoryId" class="form-select" required>
                    <c:forEach var="cat" items="${categoryList}">
                        <option value="${cat.id}">${cat.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="mb-3">
                <label class="form-label">Location</label>
                <input type="text" name="location" class="form-control" required placeholder="Istanbul, Turkey">
            </div>

            <div class="mb-3">
                <label class="form-label">Salary Range</label>
                <input type="text" name="salaryRange" class="form-control" placeholder="1000$ - 2000$">
            </div>

            <div class="mb-3">
                <label class="form-label">Employment Type</label>
                <select name="employmentType" class="form-select">
                    <option value="Full-time">Full-time</option>
                    <option value="Part-time">Part-time</option>
                    <option value="Contract">Contract</option>
                    <option value="Internship">Internship</option>
                </select>
            </div>

            <div class="mb-4">
                <label class="form-label">Required Skills</label>
                <input type="text" name="skills" class="form-control" placeholder="Java, Spring Boot, SQL">
            </div>

            <div class="text-center">
                <button type="submit" class="btn btn-primary px-4">Create Job</button>
            </div>
        </form>
    </div>
</div>

</body>
</html>
