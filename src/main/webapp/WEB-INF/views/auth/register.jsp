<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Create Account" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="max-width: 520px; margin: 1.5rem auto;">
    <div class="card">
        <div class="card-header">
            <h1 class="card-title">Customer Registration</h1>
        </div>

        <form action="${pageContext.request.contextPath}/register" method="post" onsubmit="return validateRegisterForm();">
            <div class="form-group">
                <label for="fullName" class="form-label">Full Name *</label>
                <input type="text" id="fullName" name="fullName" class="form-control" placeholder="e.g. Nimal Perera" required autofocus>
            </div>

            <div class="form-group">
                <label for="email" class="form-label">Email Address *</label>
                <input type="email" id="email" name="email" class="form-control" placeholder="nimal@example.com" required>
            </div>

            <div class="form-group">
                <label for="phone" class="form-label">Phone Number *</label>
                <input type="tel" id="phone" name="phone" class="form-control" placeholder="+94 77 123 4567" required>
            </div>

            <div class="form-group">
                <label for="address" class="form-label">Delivery Address *</label>
                <textarea id="address" name="address" class="form-control" rows="2" placeholder="Street, City, Postal Code" required></textarea>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="password" class="form-label">Password *</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="Min. 6 chars" required minlength="6">
                </div>
                <div class="form-group">
                    <label for="confirmPassword" class="form-label">Confirm Password *</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="Re-enter password" required minlength="6">
                </div>
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 0.5rem;">Register Account</button>
        </form>

        <div style="margin-top: 1.5rem; text-align: center; font-size: 0.9rem;">
            <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Sign in here</a></p>
        </div>
    </div>
</div>

<script>
    function validateRegisterForm() {
        var pwd = document.getElementById('password').value;
        var confirmPwd = document.getElementById('confirmPassword').value;

        if (pwd.length < 6) {
            alert('Password must be at least 6 characters long.');
            return false;
        }

        if (pwd !== confirmPwd) {
            alert('Passwords do not match. Please re-enter.');
            return false;
        }

        return true;
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
