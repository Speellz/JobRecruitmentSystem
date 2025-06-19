<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../common/navbar.jsp" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Recruiter</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<div class="container my-5">
    <jsp:include page="../common/messages.jsp"/>
    <div class="card shadow-sm mx-auto" style="max-width: 600px;">
        <div class="card-body">
            <h3 class="card-title text-center mb-4">Add Recruiter</h3>

            <form action="${pageContext.request.contextPath}/company/recruiters/add" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <div class="mb-3">
                    <label for="branchId" class="form-label">Select Branch:</label>
                    <select name="branchId" id="branchId" class="form-select" required>
                        <option value="" disabled selected>Choose branch...</option>
                        <c:forEach var="branch" items="${branches}">
                            <option value="${branch.id}">${branch.name} (${branch.location})</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <input type="text" name="user.name" class="form-control" placeholder="Name" required/>
                </div>

                <div class="mb-3">
                    <input type="email" name="user.email" class="form-control" placeholder="Email" required/>
                </div>

                <div class="mb-3">
                    <input type="text" name="phone" class="form-control" placeholder="Phone" required/>
                </div>

                <div class="mb-4">
                    <input type="password" name="user.password" class="form-control" placeholder="Password" required/>
                </div>

                <div class="d-grid mb-2">
                    <button type="submit" class="btn btn-primary">Add Recruiter</button>
                </div>

                <div class="text-center">
                    <a href="${pageContext.request.contextPath}/company/recruiters" class="btn btn-secondary">← Back</a>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
