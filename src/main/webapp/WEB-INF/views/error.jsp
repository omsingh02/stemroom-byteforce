<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Error" scope="request"/>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>

<c:set var="code" value="${requestScope['jakarta.servlet.error.status_code']}"/>
<c:choose>
    <c:when test="${code == 403}">
        <h1>Not allowed</h1>
        <p>Your account cannot open this page.</p>
    </c:when>
    <c:when test="${code == 404}">
        <h1>Not found</h1>
        <p>There is nothing at this address, or you cannot see it.</p>
    </c:when>
    <c:otherwise>
        <h1>Something went wrong</h1>
        <p>The server could not finish that request. Try again in a moment.</p>
    </c:otherwise>
</c:choose>
<p><a href="${ctx}/dashboard">Back to home</a></p>

<%@ include file="/WEB-INF/views/partials/footer.jspf" %>
