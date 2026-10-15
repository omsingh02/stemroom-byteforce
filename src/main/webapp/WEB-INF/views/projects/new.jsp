<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="New project" scope="request"/>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>

<h1>New project</h1>

<c:if test="${not empty error}">
    <p class="error"><c:out value="${error}"/></p>
</c:if>

<form method="post" action="${ctx}/projects/new" class="stack">
    <label>Title
        <input type="text" name="title" maxlength="120" required value="<c:out value='${title}'/>">
    </label>
    <label>Description
        <textarea name="description" maxlength="1000"><c:out value="${description}"/></textarea>
        <span class="hint">Optional.</span>
    </label>
    <label>Deadline
        <input type="date" name="deadline" value="<c:out value='${deadline}'/>">
        <span class="hint">Optional.</span>
    </label>
    <div class="actions">
        <button type="submit" class="primary">Create project</button>
        <a href="${ctx}/projects">Cancel</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/partials/footer.jspf" %>
