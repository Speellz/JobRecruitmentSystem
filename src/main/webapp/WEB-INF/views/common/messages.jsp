<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${not empty success}">
    <div class="alert alert-success alert-dismissible fade show" role="alert" id="successAlert">
            ${success}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<c:if test="${not empty error}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert" id="errorAlert">
            ${error}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
</c:if>

<script>
    setTimeout(() => {
        const success = document.getElementById("successAlert");
        const error = document.getElementById("errorAlert");

        if (success) {
            success.classList.remove("show");
            success.classList.add("fade");
            setTimeout(() => {
                success.remove();
                window.scrollTo({ top: 0, behavior: 'smooth' })
            }, 300);
        }

        if (error) {
            error.classList.remove("show");
            error.classList.add("fade");
            setTimeout(() => {
                error.remove();
                window.scrollTo({ top: 0, behavior: 'smooth' });
            }, 300);
        }
    }, 3000);
</script>

