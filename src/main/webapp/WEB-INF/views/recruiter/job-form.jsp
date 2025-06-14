<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<html>
<head>
    <title>Add Job Posting</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="../common/navbar.jsp" />

<div class="main-column">
    <h2 style="text-align:center; margin-bottom: 20px;">Add Job Posting</h2>

    <form action="${pageContext.request.contextPath}/recruiter/job/add" method="post" class="job-form">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <label class="form-label">Title:</label>
        <input type="text" name="title" class="input" required>

        <label class="form-label">Position:</label>
        <input type="text" name="position" class="input" required>

        <label class="form-label">Description:</label>
        <textarea name="description" rows="4" class="input" required></textarea>

        <label class="form-label">Category:</label>
        <select name="categoryId" class="input" required>
            <c:forEach var="cat" items="${categoryList}">
                <option value="${cat.id}">${cat.name}</option>
            </c:forEach>
        </select>

        <label class="form-label">Location:</label>
        <input type="text" name="location" class="input">

        <label class="form-label">Salary Range:</label>
        <input type="text" name="salaryRange" class="input">

        <label class="form-label">Employment Type:</label>
        <select name="employmentType" class="input">
            <option value="Full-time">Full-time</option>
            <option value="Part-time">Part-time</option>
            <option value="Contract">Contract</option>
            <option value="Internship">Internship</option>
        </select>

        <button type="submit" class="button-blue">Create Job</button>
    </form>
</div>

</body>
</html>
