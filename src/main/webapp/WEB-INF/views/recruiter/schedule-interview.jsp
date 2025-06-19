<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
    <title>Schedule Interview</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="card p-4 shadow-sm">
        <h3 class="mb-4">Schedule Interview</h3>

        <form method="get"
              action="${pageContext.request.contextPath}/recruiter/interview/schedule/${application.id}">
            <div class="mb-3">
                <label class="form-label">Interview Date</label>
                <input type="date"
                       class="form-control"
                       name="selectedDate"
                       value="${selectedDate}"
                       onchange="this.form.submit()" />
            </div>
        </form>

        <form method="post" action="${pageContext.request.contextPath}/recruiter/interview/schedule">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input type="hidden" name="applicationId" value="${application.id}" />
            <input type="hidden" name="selectedDate" value="${selectedDate}" />

            <div class="mb-3">
                <label class="form-label">Select Interview Time</label>
                <div class="d-flex flex-wrap gap-2">
                    <c:forEach var="hour" begin="8" end="17">
                        <c:set var="hourStr" value="${hour lt 10 ? '0' : ''}${hour}:00" />
                        <c:set var="slotTime" value="${selectedDate}T${hourStr}:00" />
                        <c:set var="isBooked" value="false" />

                        <c:forEach var="booked" items="${bookedSlots}">
                            <c:if test="${booked == hourStr}">
                                <c:set var="isBooked" value="true" />
                            </c:if>
                        </c:forEach>

                        <button type="submit"
                                name="time"
                                value="${slotTime}"
                                class="btn btn-outline-primary"
                                <c:if test="${isBooked}">disabled</c:if>>
                                ${hourStr}
                        </button>
                    </c:forEach>
                </div>
            </div>

            <a href="${pageContext.request.contextPath}/recruiter/applications" class="btn btn-secondary mt-3">Cancel</a>
        </form>

    </div>
</div>

</body>
</html>
