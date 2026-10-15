<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Log in" scope="request"/>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>

<h1>Log in</h1>

<c:if test="${not empty error}">
    <p class="error"><c:out value="${error}"/></p>
</c:if>

<form method="post" action="${ctx}/login" class="stack">
    <label>Email
        <input type="email" name="email" value="<c:out value='${email}'/>" required autofocus>
    </label>
    <label>Password
        <input type="password" name="password" required>
    </label>
    <div class="actions">
        <button type="submit" class="primary">Log in</button>
        <a href="${ctx}/register">Make an account</a>
    </div>
</form>

<p class="hint">Demo accounts are listed in the README.</p>

<%@ include file="/WEB-INF/views/partials/footer.jspf" %>
