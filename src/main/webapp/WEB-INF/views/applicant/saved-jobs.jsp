<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="df" uri="http://jobrecruitment.com/tags/dateformatter" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Saved Jobs</title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body id="pageBody">
<jsp:include page="../common/navbar.jsp" />

<div class="container mt-4">
    <h2 class="mb-4">Saved Jobs</h2>
    <jsp:include page="../common/job-list-fragment.jsp" />
</div>

</body>
</html>