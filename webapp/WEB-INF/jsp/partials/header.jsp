<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 

<!DOCTYPE html>
<html>
<head>
	<!-- META tags for Twitter Bootstrap -> The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <sec:csrfMetaTags/>
	
	<link rel="stylesheet" type="text/css" href="<c:url value="/jqueryvalidation/css/screen.css"/>">

	<!-- Twitter Bootstrap -->
	<!-- <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap.css" />"> -->

	<!-- Latest compiled and minified CSS -->
	<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"> -->
	<link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap.min.css" />">
	
	<link rel="stylesheet" type="text/css" href="<c:url value="/datatables/datatables.min.css"/>">
	
	<style type="text/css" class="init">
		td.details-control {
			background: url('http://www.datatables.net/examples/resources/details_open.png') no-repeat center center;
			cursor: pointer;
		}
		tr.shown td.details-control {
			background: url('http://www.datatables.net/examples/resources/details_close.png') no-repeat center center;
		}
		
		div.slider {
		    display: none;
		}
		 
		table.dataTable tbody td.no-padding {
		    padding: 0;
		}
	</style>
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js" type="text/javascript"></script>
	
	<!-- Latest compiled and minified JavaScript -->
	<!-- <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script> -->
	<c:url value="/bootstrap/js/bootstrap.min.js" var="jqueryUrl"/>
	<script type="text/javascript" src="${jqueryUrl}"></script>
	
	<script src="https://www.google.com/recaptcha/api.js" async defer></script>
 
	<script type="text/javascript" src="<c:url value="/datatables/datatables.min.js"/>"></script>	
	<script type="text/javascript" src="<c:url value="/jqueryvalidation/js/jquery.validate.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/form/jquery.form.min.js"/>"></script>
	
	<script type="text/javascript">
		var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
		var csrfHeader = $("meta[name='_csrf_header']").attr("content");
		var csrfToken = $("meta[name='_csrf']").attr("content");

		var URLWithContextPath = "${pageContext.request.scheme}" + "://" + "${pageContext.request.serverName}" + ":" + "${pageContext.request.serverPort}" + "${pageContext.request.contextPath}";
	</script>
	
	<style type="text/css">
		body { 
			padding-top: 70px; 
			padding-bottom: 40px;
		}
		
		th {
		    text-align: center;
		}
		
		.table>tbody>tr>td, .table>tbody>tr>th, .table>tfoot>tr>td, .table>tfoot>tr>th, .table>thead>tr>td, .table>thead>tr>th {
			vertical-align: middle;
		}
		
		/* Border of table */
		.table-bordered {
		    border: 1px solid #656565;
		}
		
		.table-bordered>tbody>tr>td, .table-bordered>tbody>tr>th, .table-bordered>tfoot>tr>td, .table-bordered>tfoot>tr>th, .table-bordered>thead>tr>td, .table-bordered>thead>tr>th {
			border: 1px solid #656565;
		}
		
		@media screen and (max-width: 767px) {
		.table-responsive {
		    border: 1px solid #656565;
		}
		/* End of border of table */
		
		#subjects_table_wrapper .row {
			margin-right: -15px;
		    margin-left: -15px;
		}

		label.error {
		    color: green;
		    font-style: italic;
		}
		
		#addSubject {
			margin-bottom: 10px;
		}
		
		.popover {
			max-width: 600px !important;
		}
		
		#wrap_password .popover {
			width: 320px;
			max-width: 320px;
		}
	</style>
	
	<title>EQMS</title>
</head>
<body>

	<div class="container">

		<nav class="navbar navbar-default navbar-inverse navbar-fixed-top">
		  <div class="container"> <!-- container-fluid -->
		    <!-- Brand and toggle get grouped for better mobile display -->
		    <div class="navbar-header" >
		      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
		        <span class="sr-only">Toggle navigation</span>
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
      	      </button>
		      <a class="navbar-brand" href="#">EQMS</a>
		    </div>
		
		    <!-- Collect the nav links, forms, and other content for toggling -->
		    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">		      
		      <ul class="nav navbar-nav">
		        <!-- Retrieves list items for users with ROLE_ANONYMOUS -->
			    <sec:authorize access="isAnonymous()">
				  <li><a href="<c:url value="/main/welcome"/>">Welcome page</a></li>
			    </sec:authorize>	
		      
		        <!-- Retrieves list items for users with ROLE_USER -->
		        <sec:authorize access="hasRole('ROLE_USER') and isAuthenticated()">
				  <li><a href="<c:url value="/main/common"/>">Main page</a></li>
				  <li><a href="<c:url value="/subjects/index"/>">Subjects</a></li>
				  <li><a href="<c:url value="/tests/index"/>">Tests</a></li>
				  <!-- <li><a href="<c:url value="/subjects/datatables_test"/>">Datatables</a></li> -->   <!-- language test here -->
                  <li><a href="<c:url value="/groups_of_students/index"/>">Groups</a><li>
                  <li><a href="<c:url value="/students/index"/>">Students</a><li>
				  <li><a href="<c:url value="/import_export/index"/>">Import / Export</a><li>
		  	    </sec:authorize>	
			  
		        <!-- Retrieves list items for users with ROLE_ADMIN -->
			    <sec:authorize access="hasRole('ROLE_ADMIN') and isAuthenticated()">
				  <li><a href="<c:url value="/main/admin"/>">Admin page</a></li>
			    </sec:authorize>	
		      </ul>
		      
		      <ul class="nav navbar-nav navbar-right">
		      	<sec:authorize access="isAnonymous() or isAuthenticated()">
		      		<li >
			      		<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Choose Language <span class="caret"></span></a>
			            <ul class="dropdown-menu">
			              <li><a href="?locale=pl"><img src="<c:url value="/icons/poland_32x18.png" />">&nbsp &nbsp Polish</a></li>
			              <li role="separator" class="divider"></li>
			              <li><a href="?locale=en"><img src="<c:url value="/icons/usa_32x17.png" />">&nbsp &nbsp English</a></li>
			            </ul>
                 	</li>
		      	</sec:authorize> 
		      
		        <!-- Retrieves list items for users with ROLE_ANONYMOUS -->
			    <sec:authorize access="isAnonymous()">
				   <li>
			         <p class="navbar-btn">
				       <a href="<c:url value="/auth/register"/>" class="btn btn-default"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Register</a>
				       <a href="<c:url value="/auth/login" />" class="btn btn-default"><span class="glyphicon glyphicon-user" aria-hidden="true"></span> Sign in</a>
			        </p>
		          </li>
			    </sec:authorize>	
		      
		        <!-- Retrieves list items for users with ROLE_USER and ROLE_ADMIN (if authenticated) -->
		        <sec:authorize access="isAuthenticated()">
		          <li>
		            <p class="navbar-btn">
					  	<c:url var="logoutUrl" value="/auth/logout"/>
						<form action="${logoutUrl}" method="post">
						  <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-log-out" aria-hidden="true"></span> Log out</button>
						  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						</form>
					</p>
				  </li>
		  	    </sec:authorize>
		      </ul>
		    </div><!-- /.navbar-collapse -->
		  </div><!-- /.container-fluid -->
		</nav>
		
		<c:if test="${not empty error}">
			<div class="alert alert-danger alert-dismissible" role="alert" id="error">
	  			<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	 			<strong>${error}</strong>
			</div>
		</c:if>
		
		<c:if test="${not empty warning}">
			<div class="alert alert-warning alert-dismissible" role="alert" id="warning">
	  			<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	 			<strong>${warning}</strong>
			</div>
		</c:if>
		
		<c:if test="${not empty info}">
			<div class="alert alert-info alert-dismissible" role="alert" id="info">
				<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<strong>${info}</strong>
			</div>
		</c:if>
		
		<c:if test="${not empty success}">
			<div class="alert alert-success alert-dismissible" role="alert" id="success">
	  			<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	 			<strong>${success}</strong>
			</div>
		</c:if>