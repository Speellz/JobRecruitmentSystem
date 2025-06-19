<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="com.jobrecruitment.controller.common.DateFormatterUtil" %>

<c:set var="profileImageSrc" value="${empty user.profileImageUrl
    ? pageContext.request.contextPath.concat('/img/default-profile.png')
    : pageContext.request.contextPath.concat(user.profileImageUrl)}" />

<html>
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
    <script>
        function toggleEditForm(section) {
            const form = document.getElementById(section + '-form');
            form.style.display = (form.style.display === 'none' || form.style.display === '') ? 'block' : 'none';
        }

        function toggleEditRow(id) {
            const target = document.getElementById('edit-row-' + id);
            if (target) {
                target.style.display = (target.style.display === 'none' || target.style.display === '') ? 'block' : 'none';
            }
        }
    </script>
</head>
<body class="bg-light">
<jsp:include page="navbar.jsp" />

<div class="container my-5">
    <jsp:include page="messages.jsp"/>
    <div class="card p-4 d-flex flex-row align-items-center gap-3">
        <div class="profile-image-box position-relative d-inline-block mb-3">
            <img src="${profileImageSrc}"
                 alt="Profile Image"
                 class="rounded-circle border shadow"
                 style="width:110px;height:110px;object-fit:cover;">

            <button type="button"
                    class="btn btn-outline-secondary btn-sm position-absolute bottom-0 end-0"
                    data-bs-toggle="modal" data-bs-target="#photoModal"
                    style="border-radius:50%;">
                <i class="fa fa-camera"></i>
            </button>
        </div>
        <div>
            <h2 class="mb-1">${user.name}</h2>
            <p class="mb-0">${user.email}</p>
            <p class="text-muted">Role: ${user.role}</p>
        </div>
    </div>


    <div class="mt-4">
        <c:if test="${user.role eq 'RECRUITER'}">
            <div class="card p-4 mb-3">
                <h5 class="card-title">Recruiter Information</h5>
                <p><strong>Title:</strong>
                    <c:choose>
                        <c:when test="${recruiter.role eq 'HR_MANAGER'}">Manager</c:when>
                        <c:otherwise>Recruiter</c:otherwise>
                    </c:choose>
                </p>
                <p><strong>Company:</strong> ${recruiter.company.name}</p>
                <p><strong>Branch:</strong> ${recruiter.branch.name}</p>
                <p><strong>Phone:</strong> ${recruiter.phone}</p>
                <p><strong>Joined:</strong> <%= DateFormatterUtil.format((java.time.LocalDateTime) request.getAttribute("recruiter.createdAt")) %></p>
            </div>
            <div class="card p-4 mb-3">
                <h5 class="card-title">Recruiter Analytics</h5>
                <p><strong>Total Job Posts:</strong> ${analytics.totalPosts}</p>
                <p><strong>Average Response Time:</strong> ${analytics.averageHiringTime} days</p>
                <p><strong>Applications Reviewed:</strong> ${analytics.totalHires}</p>
            </div>

            <div class="card p-4 mb-3">
                <h5 class="card-title">Performance Chart</h5>
                <canvas id="recruiterChart" height="150"></canvas>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
            <script>
                const ctx = document.getElementById('recruiterChart').getContext('2d');
                const recruiterChart = new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: ['Job Posts', 'Applications Reviewed', 'Avg Hiring Time'],
                        datasets: [{
                            label: 'Metrics',
                            data: [
                                ${analytics.totalPosts},
                                ${analytics.totalHires},
                                ${analytics.averageHiringTime}
                            ],
                            backgroundColor: [
                                'rgba(54, 162, 235, 0.7)',
                                'rgba(255, 206, 86, 0.7)',
                                'rgba(75, 192, 192, 0.7)'
                            ],
                            borderColor: [
                                'rgba(54, 162, 235, 1)',
                                'rgba(255, 206, 86, 1)',
                                'rgba(75, 192, 192, 1)'
                            ],
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            legend: { display: false }
                        },
                        scales: {
                            y: { beginAtZero: true }
                        }
                    }
                });
            </script>

        </c:if>

        <c:if test="${user.role eq 'APPLICANT'}">
            <!-- Education -->
            <div class="card p-4 mb-3">
                <div class="d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">Education</h5>
                    <button class="btn btn-outline-primary btn-sm" onclick="toggleEditForm('education')">+ Add</button>
                </div>

                <!-- Add Form -->
                <div id="education-form" class="mt-3" style="display:none;">
                    <form method="post" action="${pageContext.request.contextPath}/applicant/education/add">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <div class="row g-2 mb-2">
                            <div class="col"><input class="form-control" name="institutionName" placeholder="Institution Name" required></div>
                            <div class="col"><input class="form-control" name="degree" placeholder="Degree" required></div>
                            <div class="col"><input class="form-control" name="fieldOfStudy" placeholder="Field of Study" required></div>
                        </div>
                        <div class="row g-2 mb-2">
                            <div class="col"><input class="form-control" type="date" name="startDate" placeholder="Start Date" required></div>
                            <div class="col"><input class="form-control" type="date" name="endDate" placeholder="End Date" required></div>
                        </div>
                        <button class="btn btn-primary">Save</button>
                    </form>
                </div>

                <!-- Education List -->
                <ul class="list-group list-group-flush mt-3">
                    <c:forEach var="edu" items="${educationList}">
                        <li class="list-group-item">
                            <div class="d-flex justify-content-between align-items-center">
                                <span>${edu.institutionName} - ${edu.degree} (${edu.startDate} to ${edu.endDate})</span>
                                <button class="btn btn-sm btn-outline-secondary" onclick="toggleEditRow('edu-${edu.id}')">Edit</button>
                            </div>

                            <div id="edit-row-edu-${edu.id}" style="display: none;" class="mt-3">
                                <form method="post" action="${pageContext.request.contextPath}/applicant/education/update/${edu.id}" class="row g-2 align-items-center mb-2">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <div class="col"><input class="form-control form-control-sm" name="institutionName" value="${edu.institutionName}" required></div>
                                    <div class="col"><input class="form-control form-control-sm" name="degree" value="${edu.degree}" required></div>
                                    <div class="col"><input class="form-control form-control-sm" name="fieldOfStudy" value="${edu.fieldOfStudy}" required></div>
                                    <div class="col"><input class="form-control form-control-sm" type="date" name="startDate" value="${edu.startDate}" required></div>
                                    <div class="col"><input class="form-control form-control-sm" type="date" name="endDate" value="${edu.endDate}" required></div>
                                    <div class="col-auto d-flex gap-2 mt-2">
                                        <button class="btn btn-sm btn-success" type="submit">Update</button>
                                        <button class="btn btn-sm btn-danger" type="button" onclick="toggleEditRow('edu-${edu.id}')">Cancel</button>
                                    </div>
                                </form>
                                <form method="post" action="${pageContext.request.contextPath}/applicant/education/delete/${edu.id}">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <button class="btn btn-sm btn-danger" type="submit">Remove</button>
                                </form>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>

            <!-- Employment -->
            <div class="card p-4 mb-3">
                <div class="d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">Employment History</h5>
                    <button class="btn btn-outline-primary btn-sm" onclick="toggleEditForm('employment')">+ Add</button>
                </div>

                <!-- Add Form -->
                <div id="employment-form" class="mt-3" style="display:none;">
                    <form method="post" action="${pageContext.request.contextPath}/applicant/employment/add">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <div class="row g-2 mb-2">
                            <div class="col"><input class="form-control" name="companyName" placeholder="Company Name" required></div>
                            <div class="col"><input class="form-control" name="jobTitle" placeholder="Job Title" required></div>
                            <div class="col"><input class="form-control" type="date" name="startDate" placeholder="Start Date" required></div>
                            <div class="col"><input class="form-control" type="date" name="endDate" placeholder="End Date" required></div>
                        </div>
                        <div class="mb-2">
                            <textarea class="form-control" name="description" placeholder="Job Description (Optional)" rows="2"></textarea>
                        </div>
                        <button class="btn btn-primary">Save</button>
                    </form>
                </div>

                <!-- Employment List -->
                <ul class="list-group list-group-flush mt-3">
                    <c:forEach var="emp" items="${employmentList}">
                        <li class="list-group-item">
                            <div class="d-flex justify-content-between align-items-center">
                                <span>${emp.companyName} - ${emp.jobTitle} (${emp.startDate} - ${emp.endDate})</span>
                                <button class="btn btn-sm btn-outline-secondary" onclick="toggleEditRow('emp-${emp.id}')">Edit</button>
                            </div>

                            <div id="edit-row-emp-${emp.id}" style="display:none;" class="mt-3">
                                <form method="post" action="${pageContext.request.contextPath}/applicant/employment/update/${emp.id}" class="row g-2 align-items-center mb-2">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <div class="col"><input class="form-control form-control-sm" name="companyName" value="${emp.companyName}" required></div>
                                    <div class="col"><input class="form-control form-control-sm" name="jobTitle" value="${emp.jobTitle}" required></div>
                                    <div class="col"><input class="form-control form-control-sm" type="date" name="startDate" value="${emp.startDate}" required></div>
                                    <div class="col"><input class="form-control form-control-sm" type="date" name="endDate" value="${emp.endDate}" required></div>
                                    <div class="col-12 mt-2">
                                        <textarea class="form-control form-control-sm" name="description" placeholder="Description" rows="2">${emp.description}</textarea>
                                    </div>
                                    <div class="col-auto d-flex gap-2 mt-2">
                                        <button class="btn btn-sm btn-success" type="submit">Update</button>
                                    </div>
                                </form>
                                <form method="post" action="${pageContext.request.contextPath}/applicant/employment/delete/${emp.id}">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <button class="btn btn-sm btn-danger" type="submit">Remove</button>
                                </form>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>

            <!-- Certifications -->
            <div class="card p-4 mb-3">
                <div class="d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">Certifications</h5>
                    <button class="btn btn-outline-primary btn-sm" onclick="toggleEditForm('certification')">+ Add</button>
                </div>

                <!-- Add Form -->
                <div id="certification-form" class="mt-3" style="display:none;">
                    <form method="post" action="${pageContext.request.contextPath}/applicant/certifications/add">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <div class="row g-2 mb-2">
                            <div class="col"><input class="form-control" name="certificationName" placeholder="Certification Name" required></div>
                            <div class="col"><input class="form-control" name="issuedBy" placeholder="Issued By" required></div>
                        </div>
                        <div class="row g-2 mb-2">
                            <div class="col"><input class="form-control" type="date" name="issueDate" placeholder="Issue Date"></div>
                            <div class="col"><input class="form-control" type="date" name="expirationDate" placeholder="Expiration Date"></div>
                        </div>
                        <button class="btn btn-primary">Save</button>
                    </form>
                </div>

                <!-- Certification List -->
                <ul class="list-group list-group-flush mt-3">
                    <c:forEach var="cert" items="${certificationList}">
                        <li class="list-group-item">
                            <div class="d-flex justify-content-between align-items-center">
                    <span>
                        ${cert.certificationName} (${cert.issuedBy})
                        <c:if test="${not empty cert.issueDate}"> - Issued: ${cert.issueDate}</c:if>
                        <c:if test="${not empty cert.expirationDate}">, Expires: ${cert.expirationDate}</c:if>
                    </span>
                                <button class="btn btn-sm btn-outline-secondary" onclick="toggleEditRow('cert-${cert.id}')">Edit</button>
                            </div>

                            <div id="edit-row-cert-${cert.id}" style="display:none;" class="mt-3">
                                <form method="post" action="${pageContext.request.contextPath}/applicant/certifications/update/${cert.id}" class="row g-2 align-items-center mb-2">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <div class="col"><input class="form-control form-control-sm" name="certificationName" value="${cert.certificationName}" required></div>
                                    <div class="col"><input class="form-control form-control-sm" name="issuedBy" value="${cert.issuedBy}" required></div>
                                    <div class="col"><input class="form-control form-control-sm" type="date" name="issueDate" value="${cert.issueDate}"></div>
                                    <div class="col"><input class="form-control form-control-sm" type="date" name="expirationDate" value="${cert.expirationDate}"></div>
                                    <div class="col-auto d-flex gap-2">
                                        <button class="btn btn-sm btn-success" type="submit">Update</button>
                                    </div>
                                </form>
                                <form method="post" action="${pageContext.request.contextPath}/applicant/certifications/delete/${cert.id}">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <button class="btn btn-sm btn-danger" type="submit">Remove</button>
                                </form>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>


            <!-- Skills -->
            <div class="card p-4 mb-3">
                <div class="d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">Skills</h5>
                    <button class="btn btn-outline-primary btn-sm" onclick="toggleEditForm('skill')">+ Add</button>
                </div>

                <!-- Add Form -->
                <div id="skill-form" class="mt-3" style="display:none;">
                    <form method="post" action="${pageContext.request.contextPath}/applicant/skills/add">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <div class="mb-2"><input class="form-control" name="skillName" placeholder="Skill" required></div>
                        <button class="btn btn-primary">Save</button>
                    </form>
                </div>

                <!-- Skills List -->
                <ul class="list-group list-group-flush mt-3">
                    <c:forEach var="skill" items="${skillList}">
                        <li class="list-group-item">
                            <div class="d-flex justify-content-between align-items-center">
                                <span>${skill.skillName}</span>
                                <button class="btn btn-sm btn-outline-secondary" onclick="toggleEditRow('skill-${skill.id}')">Edit</button>
                            </div>

                            <div id="edit-row-skill-${skill.id}" style="display:none;" class="mt-3">
                                <form method="post" action="${pageContext.request.contextPath}/applicant/skills/update/${skill.id}" class="d-flex gap-2 mb-2">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <input class="form-control form-control-sm" name="skillName" value="${skill.skillName}" required>
                                    <button class="btn btn-sm btn-success" type="submit">Update</button>
                                </form>
                                <form method="post" action="${pageContext.request.contextPath}/applicant/skills/delete/${skill.id}">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <button class="btn btn-sm btn-danger" type="submit">Remove</button>
                                </form>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>

            <!-- CV Upload -->
            <div class="card p-4 mb-3">
                <h5 class="mb-3">Upload CV</h5>

                <c:choose>
                    <c:when test="${not empty user.cvUrl}">
                        <p>
                            Current CV:
                            <a href="${user.cvUrl}" target="_blank" class="btn btn-sm btn-outline-primary">View</a>
                            <button class="btn btn-sm btn-outline-secondary ms-2" onclick="toggleEditForm('cv')">Change</button>
                        </p>

                        <div id="cv-form" class="mt-3" style="display: none;">
                            <form method="post" action="${pageContext.request.contextPath}/profile/upload-cv" enctype="multipart/form-data">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <input type="file" name="cvFile" accept=".pdf" class="form-control mb-2" required />
                                <button type="submit" class="btn btn-primary">Upload</button>
                            </form>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <form method="post" action="${pageContext.request.contextPath}/profile/upload-cv" enctype="multipart/form-data">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <input type="file" name="cvFile" accept=".pdf" class="form-control mb-2" required />
                            <button type="submit" class="btn btn-primary">Upload</button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>

        </c:if>
    </div>

    <!-- PROFILE PHOTO MODAL -->
    <div class="modal fade" id="photoModal" tabindex="-1" aria-labelledby="photoModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content p-3">
                <div class="modal-header">
                    <h5 class="modal-title" id="photoModalLabel">Manage Profile Photo</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body text-center">

                    <img id="photoPreview"
                         src="${profileImageSrc}"
                         alt="Profile Image"
                         class="rounded mb-3 border"
                         style="width:120px;height:120px;object-fit:cover;"/>

                    <form action="${pageContext.request.contextPath}/profile/upload-photo"
                          method="post" enctype="multipart/form-data">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <input type="file" name="photoFile" class="form-control mb-3" accept="image/*" required onchange="previewPhoto(this)">
                        <button type="submit" class="btn btn-primary me-2">Change Photo</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/profile/remove-photo"
                          method="post" class="mt-2">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <button type="submit" class="btn btn-danger">Remove Photo</button>
                    </form>
                </div>
            </div>
        </div>
    </div>


</div>
</body>
</html>


<script>
    function toggleEditForm(section) {
        const form = document.getElementById(section + '-form');
        form.style.display = (form.style.display === 'none' || form.style.display === '') ? 'block' : 'none';
    }

    function toggleEditSection(section) {
        document.querySelectorAll('[id^="' + section + '-edit-"]').forEach(el => {
            el.style.display = (el.style.display === 'none' || el.style.display === '') ? 'block' : 'none';
        });
    }

    function toggleEditRow(id) {
        const target = document.getElementById('edit-row-' + id);
        if (target) {
            target.style.display = (target.style.display === 'none' || target.style.display === '') ? 'block' : 'none';
        }
    }
</script>

<script>
    function previewPhoto(input) {
        if (input.files && input.files[0]) {
            const reader = new FileReader();
            reader.onload = function (e) {
                document.getElementById('photoPreview').src = e.target.result;
            };
            reader.readAsDataURL(input.files[0]);
        }
    }
</script>

