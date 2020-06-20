<%@ include file="partials/header.jsp" %>

<div class="page-header" align="center" style="margin-top: 10px;">	            
    <h1>Main Page</h1>
</div>

<h3>Only users has access to this page.</h3>

<sec:authentication var="username" property="principal.username" />
<sec:authorize access="hasRole('ROLE_USER') and isAuthenticated()">
	<h3>Welcome ${username} !!!</h3>
</sec:authorize>

<br/>

<h4> Questions Exam Management System (short EQMS) is a system that allows you to dynamically manage items and tests. The main advantages of this application are: </h4>
<h4> - management of subjects (adding/deleting/modifying),</h4>
<h4> - sharing subjects,</h4>
<h4> - management of tests (adding/deleting/modifying),</h4>
<h4> - export existing tests,</h4>
<h4> - import new tests.</h4>

<script>
	var scheme = "${pageContext.request.scheme}";
	var serverName = "${pageContext.request.serverName}";
	var serverPort = "${pageContext.request.serverPort}";
	var contextPath = "${pageContext.request.contextPath}";
	var URLWithContextPath = "${pageContext.request.scheme}" + "://" + "${pageContext.request.serverName}" + ":" + "${pageContext.request.serverPort}" + "${pageContext.request.contextPath}";
	
	console.log(scheme.toString());
	console.log(serverName.toString());
	console.log(serverPort.toString());
	console.log(contextPath.toString());
	console.log(URLWithContextPath.toString());
</script>

<%@ include file="partials/footer.jsp" %>