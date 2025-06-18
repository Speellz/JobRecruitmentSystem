<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="df" uri="http://jobrecruitment.com/tags/dateformatter" %>

<c:if test="${empty jobList}">
    <div class="alert alert-info">No job postings found.</div>
</c:if>

<c:forEach var="job" items="${jobList}">
    <div class="card mb-4 shadow-sm">
        <div class="card-body">
            <div class="mb-2">
                <h5 class="card-title mb-0">${job.title}</h5>
                <small class="text-muted">${job.recruiter.user.name} • ${job.company.name} • ${job.branch.name}</small>
            </div>
            <p class="card-text mt-2">${job.description}</p>

            <ul class="list-unstyled">
                <li><strong>Position:</strong> ${job.position}</li>
                <li><strong>Location:</strong> ${job.location}</li>
                <li><strong>Type:</strong> ${job.employmentType}</li>
                <li><strong>Salary:</strong> ${job.salaryRange}</li>
                <li><strong>Created:</strong> ${df:format(job.createdAt)}</li>
            </ul>

            <div class="d-flex justify-content-between mt-3 flex-wrap gap-2">
                <div>
                    <sec:authorize access="hasAuthority('RECRUITER')">
                        <c:if test="${job.recruiter.user.id == sessionScope.currentUserId or sessionScope.isManager}">
                            <a href="${pageContext.request.contextPath}/recruiter/job/${job.id}/edit" class="btn btn-outline-secondary btn-sm">Edit</a>
                            <a href="${pageContext.request.contextPath}/recruiter/job/${job.id}/applications" class="btn btn-outline-secondary btn-sm">View Applications</a>
                        </c:if>
                    </sec:authorize>
                    <sec:authorize access="hasAuthority('COMPANY')">
                        <c:if test="${job.company != null and sessionScope.userCompany != null and job.company.id == sessionScope.userCompany.id}">
                            <a href="${pageContext.request.contextPath}/recruiter/job/${job.id}/edit" class="btn btn-outline-secondary btn-sm">Edit</a>
                            <a href="${pageContext.request.contextPath}/recruiter/job/${job.id}/applications" class="btn btn-outline-secondary btn-sm">View Applications</a>
                        </c:if>
                    </sec:authorize>
                </div>

                <div>
                    <a href="${pageContext.request.contextPath}/job/${job.id}" class="btn btn-outline-primary btn-sm">View Details</a>

                    <sec:authorize access="hasAuthority('APPLICANT')">
                        <c:choose>
                            <c:when test="${appliedMap[job.id]}">
                                <span class="btn btn-sm btn-secondary disabled">Already Applied</span>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/job/${job.id}/apply" class="btn btn-sm btn-success">Apply</a>
                            </c:otherwise>
                        </c:choose>
                    </sec:authorize>

                    <sec:authorize access="!isAuthenticated()">
                        <a href="${pageContext.request.contextPath}/auth/login" class="btn btn-sm btn-outline-dark">Login to Apply</a>
                    </sec:authorize>
                </div>
            </div>
        </div>
    </div>
</c:forEach>