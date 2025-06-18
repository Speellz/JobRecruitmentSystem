<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Assign Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">

<div class="container my-5">
    <jsp:include page="../common/messages.jsp"/>
    <div class="card shadow-sm mx-auto" style="max-width: 600px;">
        <div class="card-body">
            <h3 class="card-title text-center mb-4">Assign Manager to <span class="text-primary">${branch.name}</span></h3>

            <form action="${pageContext.request.contextPath}/company/branches/update-manager/${branch.id}" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <div class="mb-3">
                    <label for="managerId" class="form-label"><strong>Select a recruiter:</strong></label>
                    <select id="managerId" name="managerId" class="form-select" required>
                        <option disabled selected value="">Select</option>
                        <c:forEach var="recruiter" items="${recruiters}">
                            <option value="${recruiter.id}"
                                    <c:if test="${branch.manager != null && recruiter.id == branch.manager.id}">selected</c:if>>
                                    ${recruiter.user.name} (${recruiter.user.email})
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="d-grid">
                    <button type="submit" class="btn btn-primary">Save Manager</button>
                </div>
            </form>

            <div class="text-center mt-3">
                <a href="${pageContext.request.contextPath}/company/branches" class="btn btn-secondary">← Back to Branches</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>
