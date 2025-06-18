<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<jsp:include page="../common/navbar.jsp" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Company Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>

<body class="bg-light">
<div class="container my-5">
    <jsp:include page="../common/messages.jsp"/>
    <!-- COMPANY INFO -->
    <div class="card p-4 mb-4">
        <div class="d-flex justify-content-between align-items-start">
            <div class="d-flex align-items-center gap-4">
                <img src="<c:choose>
                          <c:when test="${not empty company.logoPath}">
                              ${pageContext.request.contextPath}/uploads/${company.logoPath}
                          </c:when>
                          <c:otherwise>
                              ${pageContext.request.contextPath}/img/company-default.png
                          </c:otherwise>
                      </c:choose>"
                     alt="Company Logo"
                     class="rounded"
                     style="width:100px;height:100px;object-fit:cover;cursor:pointer;"
                     data-bs-toggle="modal"
                     data-bs-target="#logoModal"/>
                <div>
                    <h2 class="mb-1">${company.name}</h2>
                    <p class="mb-0">${company.email}</p>
                    <p class="text-muted mb-0"><i class="bi bi-telephone"></i> ${company.phone}</p>
                    <p class="text-muted"><i class="bi bi-link-45deg"></i>
                        <a href="${company.website}" target="_blank">${company.website}</a></p>
                </div>
            </div>
            <button class="btn btn-outline-secondary btn-sm" onclick="toggleCompanyEdit()">Edit</button>
        </div>

        <!-- Edit Form -->
        <form action="${pageContext.request.contextPath}/company/update-info" method="post" id="companyEditForm" class="mt-4" style="display: none;">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label">Company Name</label>
                    <input type="text" name="name" class="form-control" value="${company.name}" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Email</label>
                    <input type="email" name="email" class="form-control" value="${company.email}" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Phone</label>
                    <input type="text" name="phone" class="form-control" value="${company.phone}">
                </div>
                <div class="col-md-6">
                    <label class="form-label">Website</label>
                    <input type="text" name="website" class="form-control" value="${company.website}">
                </div>
            </div>
            <div class="mt-3 d-flex gap-2">
                <button type="submit" class="btn btn-success">Save Changes</button>
                <button type="button" class="btn btn-secondary" onclick="toggleCompanyEdit()">Cancel</button>
            </div>
        </form>
    </div>

    <!-- OVERVIEW CARDS -->
    <div class="row g-4 mb-4">
        <div class="col-md-4">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h5 class="card-title mb-2"><i class="bi bi-building-fill me-2"></i>Branches</h5>
                    <p class="card-text">Total: <strong>${branches.size()}</strong></p>
                    <a href="${pageContext.request.contextPath}/company/branches" class="btn btn-outline-primary btn-sm">View All Branches</a>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h5 class="card-title mb-2"><i class="bi bi-people-fill me-2"></i>Recruiters</h5>
                    <p class="card-text">Total: <strong>${recruiters.size()}</strong></p>
                    <a href="${pageContext.request.contextPath}/company/recruiters" class="btn btn-outline-primary btn-sm">View All Recruiters</a>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h5 class="card-title mb-2"><i class="bi bi-briefcase-fill me-2"></i>Job Postings</h5>
                    <p class="card-text">Total: <strong>${totalJobPostings}</strong></p>
                    <a href="${pageContext.request.contextPath}/" class="btn btn-outline-primary btn-sm">View All Postings</a>
                </div>
            </div>
        </div>
    </div>

    <!-- BRANCH LISTING -->
    <h5 class="mb-3">Your Branches</h5>
    <div class="row g-3">
        <c:forEach var="branch" items="${branches}">
            <div class="col-md-6">
                <div class="card p-3">
                    <h6 class="fw-bold">${branch.name}</h6>
                    <p class="text-muted">${branch.location}</p>
                    <a href="${pageContext.request.contextPath}/company/branches/${branch.id}" class="btn btn-primary btn-sm">Manage Branch</a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>



<!-- LOGO MODAL -->
<div class="modal fade" id="logoModal" tabindex="-1" aria-labelledby="logoModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content p-3">
            <div class="modal-header">
                <h5 class="modal-title" id="logoModalLabel">Manage Logo</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-center">
                <img id="logoPreview"
                     src="<c:choose>
                              <c:when test='${not empty company.logoPath}'>
                                  ${pageContext.request.contextPath}/uploads/${company.logoPath}
                              </c:when>
                              <c:otherwise>
                                  ${pageContext.request.contextPath}/img/company-default.png
                              </c:otherwise>
                          </c:choose>"
                     alt="Logo"
                     class="rounded mb-3"
                     style="width:120px;height:120px;object-fit:cover;"/>

                <form action="${pageContext.request.contextPath}/company/upload-logo" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <input type="file" name="logo" class="form-control mb-3" accept="image/*" required onchange="previewLogo(this)">
                    <button type="submit" class="btn btn-primary me-2">Change Logo</button>
                </form>

                <form action="${pageContext.request.contextPath}/company/remove-logo" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <button type="submit" class="btn btn-danger">Remove Logo</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Preview Script -->
<script>
    function previewLogo(input) {
        if (input.files && input.files[0]) {
            const reader = new FileReader();
            reader.onload = function (e) {
                document.getElementById('logoPreview').src = e.target.result;
            };
            reader.readAsDataURL(input.files[0]);
        }
    }

    function toggleCompanyEdit() {
        const form = document.getElementById("companyEditForm");
        form.style.display = form.style.display === "none" ? "block" : "none";
    }
</script>