<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<jsp:include page="../common/navbar.jsp"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Branch</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">

<div class="container my-5">
    <div class="card shadow-sm">
        <div class="card-body">
            <h3 class="mb-4">Edit Branch</h3>

            <form:form method="post" action="${pageContext.request.contextPath}/company/branches/update" modelAttribute="branch" class="row g-3">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <form:hidden path="id"/>

                <div class="col-12">
                    <label class="form-label">Name</label>
                    <form:input path="name" cssClass="form-control" />
                </div>

                <div class="col-12">
                    <label class="form-label">Location</label>
                    <form:input path="location" cssClass="form-control" />
                </div>

                <div class="col-12">
                    <button type="submit" class="btn btn-primary">Update</button>
                    <a href="${pageContext.request.contextPath}/company/branches" class="btn btn-secondary">Cancel</a>
                </div>
            </form:form>
        </div>
    </div>
</div>

</body>
</html>
