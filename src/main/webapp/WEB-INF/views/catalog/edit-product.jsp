<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Edit ${product.name}" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="max-width: 680px; margin: 2rem auto;">
    <div class="card">
        <div class="card-header">
            <div>
                <h1 class="card-title">Edit Product: <c:out value="${product.name}" /></h1>
                <p style="font-size: 0.85rem; color: var(--text-muted); margin: 0.2rem 0 0;">Update product specs, category and photo.</p>
            </div>
            <span class="badge badge-info"><c:out value="${product.productId}" /></span>
        </div>

        <form action="${pageContext.request.contextPath}/admin/products/edit" method="post" onsubmit="return validateEditForm();">
            <input type="hidden" name="productId" value="<c:out value='${product.productId}' />">

            <div class="form-group">
                <label for="name" class="form-label">Product Name *</label>
                <input type="text" id="name" name="name" class="form-control" value="<c:out value='${product.name}' />" required>
            </div>

            <div class="form-row">
                <!-- Category Select with existing categories & custom option -->
                <div class="form-group">
                    <label for="categorySelect" class="form-label">Category *</label>
                    <select id="categorySelect" name="categorySelect" class="form-control" onchange="toggleCustomCategory();" required>
                        <c:forEach var="cat" items="${categories}">
                            <option value="<c:out value='${cat}' />" ${product.category eq cat ? 'selected' : ''}>
                                <c:out value="${cat}" />
                            </option>
                        </c:forEach>
                        <option value="__NEW__">+ Add New Custom Category...</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="type" class="form-label">Product Type</label>
                    <select id="type" name="type" class="form-control" onchange="updateExtraLabel();">
                        <option value="PERISHABLE" ${product.type eq 'PERISHABLE' ? 'selected' : ''}>PERISHABLE</option>
                        <option value="NON_PERISHABLE" ${product.type eq 'NON_PERISHABLE' ? 'selected' : ''}>NON_PERISHABLE</option>
                    </select>
                </div>
            </div>

            <!-- Custom Category Input (revealed only if + Add New is chosen) -->
            <div class="form-group" id="customCategoryGroup" style="display: none;">
                <label for="customCategory" class="form-label">Enter New Category Name *</label>
                <input type="text" id="customCategory" name="customCategory" class="form-control" placeholder="e.g. Exotic Fruits, Artisan Oils">
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="price" class="form-label">Unit Price (LKR) *</label>
                    <input type="number" id="price" name="price" class="form-control" value="${product.price}" min="0" step="0.01" required>
                </div>
                <div class="form-group">
                    <label for="stock" class="form-label">Current Stock *</label>
                    <input type="number" id="stock" name="stock" class="form-control" value="${product.stock}" min="0" step="1" required>
                </div>
            </div>

            <div class="form-group">
                <label for="extra" id="extraLabel" class="form-label">
                    <c:choose>
                        <c:when test="${product.type eq 'PERISHABLE'}">Expiry Date (YYYY-MM-DD) *</c:when>
                        <c:otherwise>Shelf Life in Months *</c:otherwise>
                    </c:choose>
                </label>
                <input type="text" id="extra" name="extra" class="form-control" value="<c:out value='${product.extra}' />" required>
            </div>

            <!-- Image URL with presets & preview -->
            <div class="form-group">
                <label for="imageUrl" class="form-label">Product Image URL</label>
                <input type="url" id="imageUrl" name="imageUrl" class="form-control" value="<c:out value='${product.imageUrl}' />" placeholder="https://..." oninput="updateImagePreview(this.value);">

                <!-- Quick Presets -->
                <div style="margin-top: 0.65rem;">
                    <span style="font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em; font-weight: 700; display: block; margin-bottom: 0.35rem;">
                        Quick Image Presets:
                    </span>
                    <div style="display: flex; gap: 0.4rem; flex-wrap: wrap;">
                        <button type="button" class="btn btn-sm btn-ghost" onclick="setPresetImg('https://images.unsplash.com/photo-1550583724-b2692b85b150?w=500&auto=format&fit=crop&q=60')">🥛 Milk</button>
                        <button type="button" class="btn btn-sm btn-ghost" onclick="setPresetImg('https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=500&auto=format&fit=crop&q=60')">🍎 Apples</button>
                        <button type="button" class="btn btn-sm btn-ghost" onclick="setPresetImg('https://images.unsplash.com/photo-1540420773420-3366772f4999?w=500&auto=format&fit=crop&q=60')">🥦 Veggies</button>
                        <button type="button" class="btn btn-sm btn-ghost" onclick="setPresetImg('https://images.unsplash.com/photo-1523049673857-eb18f1d7b578?w=500&auto=format&fit=crop&q=60')">🥑 Avocado</button>
                        <button type="button" class="btn btn-sm btn-ghost" onclick="setPresetImg('https://images.unsplash.com/photo-1509440159596-0249088772ff?w=500&auto=format&fit=crop&q=60')">🥖 Bread</button>
                        <button type="button" class="btn btn-sm btn-ghost" onclick="setPresetImg('https://images.unsplash.com/photo-1586201375761-83865001e31c?w=500&auto=format&fit=crop&q=60')">🌾 Grains</button>
                        <button type="button" class="btn btn-sm btn-ghost" onclick="setPresetImg('https://images.unsplash.com/photo-1506976785307-8732e854ad03?w=500&auto=format&fit=crop&q=60')">🥚 Eggs</button>
                        <button type="button" class="btn btn-sm btn-ghost" onclick="setPresetImg('https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=500&auto=format&fit=crop&q=60')">🌻 Olive Oil</button>
                    </div>
                </div>

                <!-- Live Preview -->
                <div style="margin-top: 0.75rem; display: flex; align-items: center; gap: 1rem;">
                    <img id="imagePreview" src="<c:out value='${product.imageUrl}' />" alt="Preview" style="width: 80px; height: 80px; object-fit: cover; border-radius: var(--radius-sm); border: 1px solid var(--border-subtle);">
                    <span style="font-size: 0.8rem; color: var(--text-muted);">Current Photo Preview</span>
                </div>
            </div>

            <div class="form-group">
                <label for="description" class="form-label">Product Description</label>
                <textarea id="description" name="description" class="form-control" rows="3"><c:out value="${product.description}" /></textarea>
            </div>

            <div class="card-footer" style="padding-bottom: 0; margin-bottom: 0;">
                <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-ghost">Cancel</a>
                <button type="submit" class="btn btn-primary">Save Changes</button>
            </div>
        </form>
    </div>
</div>

<script>
    function toggleCustomCategory() {
        var select = document.getElementById('categorySelect');
        var customGroup = document.getElementById('customCategoryGroup');
        var customInput = document.getElementById('customCategory');
        if (select.value === '__NEW__') {
            customGroup.style.display = 'block';
            customInput.required = true;
            customInput.focus();
        } else {
            customGroup.style.display = 'none';
            customInput.required = false;
        }
    }

    function setPresetImg(url) {
        var input = document.getElementById('imageUrl');
        input.value = url;
        updateImagePreview(url);
    }

    function updateImagePreview(url) {
        var img = document.getElementById('imagePreview');
        if (url && url.trim().length > 5) {
            img.src = url;
        }
    }

    function updateExtraLabel() {
        var type = document.getElementById('type').value;
        var label = document.getElementById('extraLabel');
        if (type === 'PERISHABLE') {
            label.innerText = 'Expiry Date (YYYY-MM-DD) *';
        } else {
            label.innerText = 'Shelf Life in Months *';
        }
    }

    function validateEditForm() {
        var price = parseFloat(document.getElementById('price').value);
        var stock = parseInt(document.getElementById('stock').value, 10);
        var catSelect = document.getElementById('categorySelect').value;
        var customCat = document.getElementById('customCategory').value.trim();

        if (catSelect === '__NEW__' && !customCat) {
            alert('Please enter a new category name.');
            return false;
        }
        if (isNaN(price) || price < 0) {
            alert('Price cannot be negative.');
            return false;
        }
        if (isNaN(stock) || stock < 0) {
            alert('Stock cannot be negative.');
            return false;
        }
        return true;
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
