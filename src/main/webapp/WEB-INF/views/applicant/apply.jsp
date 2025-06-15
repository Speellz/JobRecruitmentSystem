<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Apply to Job</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<jsp:include page="../common/navbar.jsp" />

<div class="page-container">
    <div class="main-column">
        <h2 style="text-align:center;">Apply to: <span style="color:#0a66c2">${job.title}</span></h2>

        <form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/job/${job.id}/apply">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <label class="form-label">CV File (PDF):</label>
            <input type="file" name="cvFile" accept=".pdf" class="input" required />

            <label class="form-label">Cover Letter:</label>
            <textarea name="coverLetter" rows="6" class="input" placeholder="Write a brief message..." required></textarea>

            <div style="margin-top:20px;">
                <button type="submit" class="button-blue">Submit Application</button>
            </div>
        </form>
    </div>
</div>

</body>
</html>
