<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Make an account" scope="request"/>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>

<h1>Make an account</h1>

<c:choose>
    <c:when test="${closed}">
        <p class="notice">New sign-ups are closed for now.</p>
        <p><a href="${ctx}/login">Back to log in</a></p>
    </c:when>
    <c:otherwise>
        <c:if test="${not empty error}">
            <p class="error"><c:out value="${error}"/></p>
        </c:if>

        <form method="post" action="${ctx}/register" class="stack">
            <label>Name
                <input type="text" name="name" maxlength="80" required value="<c:out value='${name}'/>">
            </label>
            <label>Email
                <input type="email" name="email" required value="<c:out value='${email}'/>">
            </label>
            <label>Password
                <input type="password" name="password" minlength="6" required>
                <span class="hint">At least 6 characters.</span>
            </label>
            <div class="actions">
                <button type="submit" class="primary">Make account</button>
                <a href="${ctx}/login">I already have one</a>
            </div>
        </form>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/partials/footer.jspf" %>
