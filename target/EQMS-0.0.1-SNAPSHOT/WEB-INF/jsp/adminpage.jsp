<%@ include file="partials/header.jsp" %>

<h1>Admin Page</h1>
<p>Only admins have access to this page.</p>

<sec:authentication var="username" property="principal.username" />
<sec:authorize access="hasRole('ROLE_ADMIN') and isAuthenticated()">
<p>Welcome ${username} !!!</p>
</sec:authorize>

<br>
<br>

<p>pageContext.request.requestURI     ->     ${pageContext.request.requestURI}<p>
<p>pageContext.request.requestURL     ->     ${pageContext.request.requestURL}<p>
<p>pageContext.request.contextPath     ->     ${pageContext.request.contextPath}<p>

<br>
<br>

<c:url value="/main/admin" var="adminUrl"/>
<p>Default URL is ${adminUrl}</p>

<br>
<br>

<c:url var="logoutUrl" value="/auth/logout"/>
<form action="${logoutUrl}" method="post">		<!-- or action="../auth/logout" -->
  <input type="submit" value="Log out" />
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>

<%@ include file="partials/footer.jsp" %>