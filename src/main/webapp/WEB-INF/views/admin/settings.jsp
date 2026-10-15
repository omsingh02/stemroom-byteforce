<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Settings" scope="request"/>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>

<h1>Settings</h1>

<c:if test="${param.saved == '1'}">
    <p class="notice">Settings saved.</p>
</c:if>
<c:if test="${not empty error}">
    <p class="error"><c:out value="${error}"/></p>
</c:if>

<form method="post" action="${ctx}/admin/settings" class="stack">
    <label class="check">
        <input type="checkbox" name="allow_registration" ${allowRegistration ? 'checked' : ''}>
        Allow new people to make an account
    </label>
    <label>Projects per musician
        <input type="number" name="max_projects_per_user" min="1" max="100" required
               value="<c:out value='${maxProjects}'/>">
        <span class="hint">Between 1 and 100.</span>
    </label>
    <div class="actions">
        <button type="submit" class="primary">Save settings</button>
    </div>
</form>

<%@ include file="/WEB-INF/views/partials/footer.jspf" %>
