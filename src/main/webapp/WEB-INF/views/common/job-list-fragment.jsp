<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="df" uri="http://jobrecruitment.com/tags/dateformatter" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<c:if test="${empty jobList}">
    <p style="margin-top: 15px;">No job postings found.</p>
</c:if>
<c:forEach var="job" items="${jobList}">
    <div class="job-card">
        <div class="job-header">
            <div class="recruiter-name">${job.recruiter.user.name}</div>
            <div class="recruiter-info">${job.company.name} • ${job.branch.name}</div>
        </div>
        <div class="job-body">
            <h3>${job.title}</h3>
            <p>${job.description}</p>
            <p><strong>Position:</strong> ${job.position}</p>
            <p><strong>Location:</strong> ${job.location}</p>
            <p><strong>Type:</strong> ${job.employmentType}</p>
            <p><strong>Salary:</strong> ${job.salaryRange}</p>
            <p><strong>Created:</strong> ${df:format(job.createdAt)}</p>
        </div>
        <div class="job-actions">
            <sec:authorize access="hasAnyAuthority('RECRUITER', 'COMPANY')">
                <a href="${pageContext.request.contextPath}/recruiter/job/${job.id}/edit" class="view-link">Edit</a>
            </sec:authorize>
            <sec:authorize access="hasAuthority('APPLICANT')">
                <a href="${pageContext.request.contextPath}/job/${job.id}/apply" class="view-link">Apply</a>
            </sec:authorize>
            <sec:authorize access="!isAuthenticated()">
                <a href="${pageContext.request.contextPath}/auth/login" class="view-link">Login to Apply</a>
            </sec:authorize>
        </div>
    </div>
</c:forEach>
