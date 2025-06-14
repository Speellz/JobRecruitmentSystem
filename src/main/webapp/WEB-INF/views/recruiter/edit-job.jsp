<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Job Posting</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="../common/navbar.jsp" />

<div class="main-column">
    <h2 style="text-align:center; margin-bottom: 25px; color:#0a66c2;">Edit Job Posting</h2>

    <form method="post" action="${pageContext.request.contextPath}/recruiter/job/${jobPosting.id}/update" class="job-form">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <label class="form-label">Title:</label>
        <input type="text" name="title" class="input" value="${jobPosting.title}" required>

        <label class="form-label">Position:</label>
        <input type="text" name="position" class="input" value="${jobPosting.position}" required>

        <label class="form-label">Description:</label>
        <textarea name="description" rows="5" class="input" required>${jobPosting.description}</textarea>

        <label class="form-label">Location:</label>
        <input type="text" name="location" class="input" value="${jobPosting.location}" required>

        <label class="form-label">Employment Type:</label>
        <select name="employmentType" class="input">
            <option value="Full-time" ${jobPosting.employmentType == 'Full-time' ? 'selected' : ''}>Full-time</option>
            <option value="Part-time" ${jobPosting.employmentType == 'Part-time' ? 'selected' : ''}>Part-time</option>
            <option value="Contract" ${jobPosting.employmentType == 'Contract' ? 'selected' : ''}>Contract</option>
            <option value="Internship" ${jobPosting.employmentType == 'Internship' ? 'selected' : ''}>Internship</option>
        </select>

        <label class="form-label">Salary Range:</label>
        <input type="text" name="salaryRange" class="input" value="${jobPosting.salaryRange}">

        <label class="form-label">Category:</label>
        <select name="categoryId" class="input">
            <c:forEach var="cat" items="${categoryList}">
                <option value="${cat.id}" ${cat.id == jobPosting.category.id ? 'selected' : ''}>${cat.name}</option>
            </c:forEach>
        </select>

        <button type="submit" class="button-blue">Update Job</button>
    </form>
</div>

</body>
</html>
