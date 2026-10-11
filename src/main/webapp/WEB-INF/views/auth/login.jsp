<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Sign In" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="max-width: 440px; margin: 2.5rem auto;">
    <div class="card" style="box-shadow: var(--shadow-md);">
        <div class="card-header">
            <div>
                <h1 class="card-title">Welcome Back</h1>
                <p style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 0; margin-top: 0.25rem;">Sign in to your Aura account</p>
            </div>
            <span class="brand-badge">A</span>
        </div>

        <form action="${pageContext.request.contextPath}/login" method="post" onsubmit="return validateLoginForm();">
            <div class="form-group">
                <label for="email" class="form-label">Email Address</label>
                <input type="email" id="email" name="email" class="form-control" placeholder="name@example.com" required autofocus>
            </div>

            <div class="form-group">
                <label for="password" class="form-label">Password</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 0.75rem;">Sign In</button>
        </form>

        <div style="margin-top: 1.5rem; text-align: center; font-size: 0.9rem;">
            <p style="margin-bottom: 0;">Don't have an account? <a href="${pageContext.request.contextPath}/register" style="color: var(--accent-primary); font-weight: 600;">Create one here</a></p>
        </div>
    </div>
</div>

<script>
    function validateLoginForm() {
        var email = document.getElementById('email').value.trim();
        var password = document.getElementById('password').value;
        if (!email || !password) {
            alert('Please provide both email and password.');
            return false;
        }
        return true;
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
