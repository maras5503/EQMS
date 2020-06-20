<%@ include file="partials/header.jsp" %>

<h1>Second page</h1>
<p>Everyone has access to this page.</p>

<sec:authentication var="username" property="principal.username" />
<sec:authorize access="hasRole('ROLE_USER') and isAuthenticated()">
<p>Welcome ${username} !!!</p>
</sec:authorize>

<br>
<br>

<p>pageContext.request.requestURI     ->     ${pageContext.request.requestURI}<p>
<p>pageContext.request.requestURL     ->     ${pageContext.request.requestURL}<p>
<p>pageContext.request.contextPath     ->     ${pageContext.request.contextPath}<p>

<br>
<br>

<c:url value="/main/second" var="secondUrl"/>
<p>Default URL is ${secondUrl}</p>

<br>
<br>

<div class="col-sm-12" id="importTestDiv" align="center" style="padding-bottom: 20px">
	<form class="form-horizontal" action="<c:url value="/import_export/doPreviewAjax"/>" method="POST" id="importForm" accept-charset="utf-8">
		<div class="form-group" style="margin-bottom: 10px; margin-top: 10px;">
			<label class="control-label col-sm-12" style="text-align: center; padding-bottom: 6px;">Choose file from disk, that contains test to import:</label>
		</div>
		<div class="form-group" style="margin-bottom: 10px; margin-top: 10px;">
			<input type="file" id="inputTestFile" name="inputTestFile" accept=".tg_org,.tg" />
		</div>
		<input type="hidden" name="subjectReference" id="subjectReference" />
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>
</div>


<c:url var="logoutUrl" value="/auth/logout"/>
<form action="${logoutUrl}" method="post">		<!-- or action="../auth/logout" -->
  <input type="submit" value="Log out" />
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>

<%@ include file="partials/footer.jsp" %>