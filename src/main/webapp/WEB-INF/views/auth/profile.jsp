<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My Profile" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="margin-bottom: 1.5rem;">
    <h1>User Profile &amp; Settings</h1>
    <p>Manage your personal information, delivery destination, and credentials.</p>
</div>

<div class="grid-2">
    <!-- Card 1: Personal & Contact Information -->
    <div class="card">
        <div class="card-header">
            <h2 class="card-title">Personal Details</h2>
            <span class="badge badge-success"><c:out value="${user.role}" /></span>
        </div>

        <form action="${pageContext.request.contextPath}/profile" method="post">
            <input type="hidden" name="action" value="updateProfile">

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">User ID</label>
                    <input type="text" class="form-control" value="<c:out value='${user.userId}' />" readonly disabled>
                </div>
                <div class="form-group">
                    <label class="form-label">Account Status</label>
                    <input type="text" class="form-control" value="<c:out value='${user.status}' />" readonly disabled>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">Email Address (Read-only)</label>
                <input type="email" class="form-control" value="<c:out value='${user.email}' />" readonly disabled>
            </div>

            <div class="form-group">
                <label for="fullName" class="form-label">Full Name *</label>
                <input type="text" id="fullName" name="fullName" class="form-control" value="<c:out value='${user.fullName}' />" required>
            </div>

            <div class="form-group">
                <label for="phone" class="form-label">Phone Number *</label>
                <input type="tel" id="phone" name="phone" class="form-control" value="<c:out value='${user.phone}' />" required>
            </div>

            <div class="form-group">
                <label for="address" class="form-label">Default Delivery Address *</label>
                <textarea id="address" name="address" class="form-control" rows="3" required><c:out value="${user.address}" /></textarea>
            </div>

            <button type="submit" class="btn btn-primary">Save Changes</button>
        </form>
    </div>

    <!-- Card 2: Security & Password -->
    <div>
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Security &amp; Password</h2>
            </div>

            <form action="${pageContext.request.contextPath}/profile" method="post" onsubmit="return validatePasswordChange();">
                <input type="hidden" name="action" value="changePassword">

                <div class="form-group">
                    <label for="currentPassword" class="form-label">Current Password *</label>
                    <input type="password" id="currentPassword" name="currentPassword" class="form-control" placeholder="••••••••" required>
                </div>

                <div class="form-group">
                    <label for="newPassword" class="form-label">New Password *</label>
                    <input type="password" id="newPassword" name="newPassword" class="form-control" placeholder="Min. 6 characters" required minlength="6">
                </div>

                <div class="form-group">
                    <label for="confirmPassword" class="form-label">Confirm New Password *</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="Re-enter new password" required minlength="6">
                </div>

                <button type="submit" class="btn btn-primary">Update Password</button>
            </form>
        </div>

        <!-- Card 3: Account Danger Zone (Deactivate & Permanent Delete) -->
        <div class="card" style="border-color: rgba(248, 113, 113, 0.4);">
            <div class="card-header">
                <h2 class="card-title text-danger">Account Actions &amp; Danger Zone</h2>
            </div>
            
            <div style="margin-bottom: 1.5rem; padding-bottom: 1.25rem; border-bottom: 1px solid var(--border-subtle);">
                <h4 style="font-size: 0.95rem; margin-bottom: 0.35rem; color: #ffffff;">Option 1: Deactivate Account</h4>
                <p style="font-size: 0.85rem; margin-bottom: 0.75rem;">Temporarily disables your account and future logins. Can be restored by reaching out to support.</p>
                <form action="${pageContext.request.contextPath}/profile" method="post" onsubmit="return confirm('Are you sure you want to deactivate your account? You will be signed out immediately.');">
                    <input type="hidden" name="action" value="deactivate">
                    <button type="submit" class="btn btn-sm btn-ghost" style="border-color: rgba(248, 113, 113, 0.5); color: #fca5a5;">Deactivate Account</button>
                </form>
            </div>

            <div>
                <h4 style="font-size: 0.95rem; margin-bottom: 0.35rem; color: var(--accent-danger);">Option 2: Permanently Delete Account</h4>
                <p style="font-size: 0.85rem; margin-bottom: 0.75rem;">Permanently and irreversibly purges your personal profile, credentials, and data from our active storage.</p>
                <form action="${pageContext.request.contextPath}/profile" method="post" onsubmit="return confirm('WARNING: Are you sure you want to PERMANENTLY DELETE your account? This action is irreversible and your account cannot be recovered.');">
                    <input type="hidden" name="action" value="deletePermanent">
                    <button type="submit" class="btn btn-sm btn-danger">Permanently Delete Account</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    function validatePasswordChange() {
        var newP = document.getElementById('newPassword').value;
        var confP = document.getElementById('confirmPassword').value;

        if (newP.length < 6) {
            alert('New password must be at least 6 characters.');
            return false;
        }
        if (newP !== confP) {
            alert('New passwords do not match.');
            return false;
        }
        return true;
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
