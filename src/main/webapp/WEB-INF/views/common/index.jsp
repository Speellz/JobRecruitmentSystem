<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="df" uri="http://jobrecruitment.com/tags/dateformatter" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>JobRecruit | Home</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body class="bg-light">

<jsp:include page="navbar.jsp" />

<div class="container py-4">
    <jsp:include page="messages.jsp"/>
    <div class="mb-4">
        <h2 class="text-primary">Latest Job Postings</h2>
        <div class="d-flex justify-content-between align-items-center flex-wrap mt-3">

            <!-- Sol kısım -->
            <div class="d-flex gap-3 flex-wrap">
                <input type="text" id="searchInput" name="q" placeholder="Search jobs by title or keyword..." class="form-control" style="max-width: 300px;">

                <sec:authorize access="hasAuthority('RECRUITER')">
                    <a href="${pageContext.request.contextPath}/recruiter/job/form" class="btn btn-primary">+ Add Job</a>
                </sec:authorize>

                <sec:authorize access="hasAuthority('APPLICANT')">
                    <a href="${pageContext.request.contextPath}/applicant/applications" class="btn btn-outline-primary">My Applications</a>
                </sec:authorize>
            </div>

            <sec:authorize access="hasAuthority('APPLICANT')">
                <div>
                    <a href="${pageContext.request.contextPath}/applicant/saved/jobs" class="btn btn-outline-primary">Saved Jobs</a>
                </div>
            </sec:authorize>

        </div>


        <sec:authorize access="hasAuthority('RECRUITER')">
            <div class="mt-3">
                <a href="${pageContext.request.contextPath}/recruiter/jobs/my-branch"
                   class="btn btn-outline-primary me-2 ${viewMode == 'branch' ? 'active' : ''}">My Branch Job Postings</a>
                <a href="${pageContext.request.contextPath}/recruiter/jobs/my"
                   class="btn btn-outline-primary ${viewMode == 'personal' ? 'active' : ''}">My Job Postings</a>
            </div>
        </sec:authorize>
    </div>

    <div id="jobContainer">
        <c:if test="${empty jobList}">
            <div class="alert alert-info">No job postings found.</div>
        </c:if>

        <c:forEach var="job" items="${jobList}">
            <div class="card mb-4 shadow-sm position-relative">

                <sec:authorize access="hasAuthority('APPLICANT')">
                    <c:choose>
                        <c:when test="${savedMap[job.id]}">
                            <form method="post" action="${pageContext.request.contextPath}/applicant/saved/remove/${job.id}"
                                  style="position: absolute; top: 10px; right: 10px;">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <button type="submit" class="btn btn-link p-0 border-0" style="font-size: 20px;">
                                    <span style="color: gold;">&#9733;</span>
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form method="post" action="${pageContext.request.contextPath}/applicant/saved/add/${job.id}"
                                  style="position: absolute; top: 10px; right: 10px;">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <button type="submit" class="btn btn-link p-0 border-0" style="font-size: 20px;">
                                    <span style="color: #ccc;">&#9733;</span>
                                </button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </sec:authorize>

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
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    const basePath = "${pageContext.request.contextPath}";

    document.getElementById('searchInput').addEventListener('input', function () {
        const keyword = this.value.trim();

        if (keyword === "") {
            window.location.href = basePath + "/";
            return;
        }

        fetch(basePath + "/search-live?q=" + encodeURIComponent(keyword), {
            headers: {
                'Accept': 'text/html'
            }
        })
            .then(response => {
                if (!response.ok) throw new Error("Fetch error: " + response.status);
                return response.text();
            })
            .then(html => {
                document.getElementById("jobContainer").innerHTML = html;
            })
            .catch(err => {
                console.error("Search error:", err);
            });
    });
</script>

</body>
</html>
