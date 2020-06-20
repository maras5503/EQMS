<%@ include file="partials/header.jsp" %>

<div class="row">

    <div class="col-sm-12">

        <c:choose>
        	<c:when test="${not empty allSubjectsModel}">
        	
        		<div class="page-header" style="margin-top: 10px;">	
					<div class="col-sm-offset-3 col-sm-6" align="center">			
		            	<h1>All subjects</h1>
		            </div>
		            
		            <div id="addSubject" class="pull-right" >
						<button type="button" class="btn btn-success" id="addSubjectBtn" name="addSubjectBtn" style="margin-top: 20px; margin-bottom: 10px;" data-toggle="modal" data-target="#addSubjectModal"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add new subject</button>
					</div>
					
					<div class="clearfix"></div>
			    </div>
	        	
	        	<!-- try http://getbootstrap.com/css/#tables-responsive -->
	        	<div class="table-responsive" style="margin-bottom: 0px">
		        	<table class="table table-bordered" id="subjects_table">
			        	<thead>
							<tr class="success">
								<th style="width: 35%;">SUBJECT NAME</th>
								<th style="width: 15%;">CREATION DATE</th>
								<th style="width: 20%;">CREATED BY</th>
								<th style="border-right-width: 0px;"></th>
								<th style="border-right-width: 0px;"></th>
								<th style="border-right-width: 0px;"></th> <!-- style="empty-cells: hide" -->
							</tr>
						</thead>
						<tbody>
							<!-- Auxiliary value that determines whether the current object belongs to a given user -->
							<c:set var="isUserSubject" value="false"/>	
							<c:forEach var="subject" items="${allSubjectsModel}">
								
								<c:forEach var="userSubject" items="${userSubjectsModel}">
									<c:if test="${subject.subjectName == userSubject.subjectName}">
										<!-- If current object belongs to a given user, set true -->
										<c:set var="isUserSubject" value="true"/>
									</c:if>
								</c:forEach>
								
								<tr class="success" id="${subject.subjectId}">
									<td><c:out value="${subject.subjectName}" /></td>
									<td><c:out value="${subject.creationDate}" /></td>
									<td><c:out value="${subject.createdBy}" /></td>
									<c:if test="${isUserSubject eq true}">	<!-- We have to disable "Ask for access" anchor, if current object belongs to a given user -->
										<td>
											<a href="#" class="btn btn-primary btn-block btn-sm disabled" role="button"><span class="glyphicon glyphicon-share" aria-hidden="true"></span> Ask for access</a>
										</td>
										<td>
											<button type="button" class="btn btn-info btn-block btn-sm" id="editSubjectBtn" name="editSubjectBtn" data-toggle="modal" data-target="#editSubjectModal" data-subject-name="${subject.subjectName}" data-subject-reference="${subject.subjectId}"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit</button>
										</td>
										<td>
											<button type="button" class="btn btn-danger btn-block btn-sm" id="deleteSubjectBtn" name="deleteSubjectBtn" data-toggle="modal" data-target="#confirmDeleteSubject" data-title="Delete Subject" data-message="Are you sure you want to delete subject '${subject.subjectName}'?" data-subject-reference="${subject.subjectId}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete</button>
										</td>
									</c:if>
									<c:if test="${isUserSubject eq false}">  <!-- We have to disable "Edit" and "Remove" anchors, if current object doesn't belong to a given user -->
										<td>
											<button type="button" id="sendMessageBtn" name="sendMessageBtn" class="btn btn-primary btn-block btn-sm" data-toggle="modal" data-target="#sendMessageModal" data-recipient="${subject.createdBy}" data-topic="${subject.subjectName}" data-subject-reference="${subject.subjectId}"><span class="glyphicon glyphicon-share" aria-hidden="true"></span> Ask for access</button>
										</td>
										<td>
											<a href="#" class="btn btn-info btn-block btn-sm disabled" role="button"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit</a>
										</td>
										<td>
											<a href="#" class="btn btn-danger btn-block btn-sm disabled" role="button"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete</a>
										</td>
									</c:if>
								</tr>
								
								<!-- After loop, set default false -->
								<c:set var="isUserSubject" value="false"/>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:when>
		    <c:otherwise>
		    	<div class="well">
                	There are no subjects.
           		</div>
		    </c:otherwise>
		</c:choose>
	</div>
</div>

<div class="modal fade" id="addSubjectModal" tabindex="-1" role="dialog" aria-labelledby="addSubjectLabelModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="addSubjectTitleModal">Add new subject</h4>
			</div>
			<div class="modal-body">
				<form action="<c:url value="/subjects/doAddSubject"/>" method="POST" id="addSubjectFormModal">
					<div class="form-group">
		      			<label for="subjectNameModal" class="control-label">Subject:</label>
		      			<input type="text" class="form-control" id="subjectNameModal" name="subjectNameModal" placeholder="Subject name" maxlength="100" aria-describedby="inputAddSubjectError">
		      			<span id="glyphiconErrorAddSubject" class="form-control-feedback" aria-hidden="true"></span>
  						<span id="inputAddSubjectError" class="sr-only">(error)</span>
					</div>
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="addSubjectBtnModal">Add subject</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="editSubjectModal" tabindex="-1" role="dialog" aria-labelledby="editSubjectLabelModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="editSubjectTitleModal">Edit subject</h4>
			</div>
			<div class="modal-body">
				<form action="<c:url value="/subjects/doEditSubject"/>" method="POST" id="editSubjectFormModal">
					<div class="form-group">		
		      			<label for="subjectNameModal" class="control-label">Subject:</label>
		      			<input type="text" class="form-control" id="subjectNameModal" name="subjectNameModal" placeholder="Subject name" maxlength="100" aria-describedby="inputEditSubjectError">
		      			<span id="glyphiconErrorEditSubject" class="form-control-feedback" aria-hidden="true"></span> 
  						<span id="inputEditSubjectError" class="sr-only">(error)</span>
					</div>
					<input type="hidden" name="subjectReference" id="subjectReference" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="editSubjectBtnModal">Edit subject</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="confirmDeleteSubject" tabindex="-1" role="dialog" aria-labelledby="confirmDeleteSubjectLabelModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="confirmDeleteSubjectTitleModal">Delete subject</h4>
			</div>
			<div class="modal-body">
				<form action="<c:url value="/tests/doDeleteSubject"/>" method="POST" id="deleteSubjectFormModal" >
					<div class="form-group" id="confirmDeleteSubjectLabel">
					</div>
					<input type="hidden" name="subjectReference" id="subjectReference" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-danger" id="confirmDeleteSubjectBtnModal">Delete</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="sendMessageModal" tabindex="-1" role="dialog" aria-labelledby="sendMessageLabelModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="sendMessageTitleModal">New message</h4>
			</div>
			<div class="modal-body" >
				<form action="<c:url value="/subjects/askForAccess"/>" method="POST" id="sendMessageFormModal">
					<div class="form-group">
						<label for="recipientName" class="control-label">Recipient:</label>
						<input type="text" class="form-control" name="recipientName" id="recipientName" readonly="readonly">
					</div>
					<div class="form-group">
						<label for="topic" class="control-label">Topic:</label>
						<input type="text" class="form-control" name="topic" id="topic" readonly="readonly">
					</div>
					<div class="form-group">
						<label for="messageText" class="control-label">Message:</label>
						<textarea class="form-control resizable" name="messageText" id="messageText" placeholder="Message" maxlength="2000" aria-describedby="inputSendMessageError"></textarea>
						<span id="glyphiconErrorSendMessage" class="form-control-feedback" aria-hidden="true"></span> 
  						<span id="inputSendMessageError" class="sr-only">(error)</span>
					</div>
					<input type="hidden" name="subjectReference" id="subjectReference" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="sendMessageBtnModal">Send message</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	/*************************************************************/
	/*************** Configuring DataTable options ***************/
	/*************************************************************/
	/*
		paging    	-> Enable or disable table pagination
		searching	-> Feature control search (filtering) abilities
		ordering  	-> Feature control ordering (sorting) abilities in DataTables
		lengthMenu 	-> Change the options in the page length select list
		pageLength 	-> Change the initial page length (number of rows per page)
		columns.orderable	-> Enable or disable ordering on this column
	*/
	
	$('#subjects_table').DataTable( {
	    "paging": true,
	    "searching": true,
	    "ordering":  true,
	    "lengthMenu": [ [5, 10, 25, 50, -1], [5, 10, 25, 50, "All"] ],
	    "pageLength": 10,
	    "columns": [
	        null,
	        null,
	        null,
	        { "orderable": false },
	        { "orderable": false },
	        { "orderable": false }
	    ],
	    "language": {
	    	"info": "Showing _START_ to _END_ of _TOTAL_ subjects",		// default is: Showing _START_ to _END_ of _TOTAL_ entries
	    	"infoEmpty": "Showing 0 to 0 of 0 subjects", 				// default is: Showing 0 to 0 of 0 entries
	    	"infoFiltered": "(filtered from _MAX_ total subjects)",		// default is: (filtered from _MAX_ total entries)
	    	"lengthMenu": "Show _MENU_ subjects"						// default is: Show _MENU_ entries
		}
	} );

	/**************************************************************/
	/*** Adding validation and handling events for "addSubject" ***/
	/**************************************************************/

	jQuery.validator.addMethod("checkSubjectName", function(value, element) {
		var isSuccess = false;
		console.log("isSuccess value before ajax call = " + isSuccess.toString());

		var data = {};
		data[csrfParameter] = csrfToken;
		data["subjectName"] = value;

		console.log("subjectName value = " + value);
	    
	    $.ajax({
			url: URLWithContextPath + "/subjects/checkSubjectName",
			type: "POST",
			async: false,
			data: data,
			success: function(data) {
				console.log("Data in response: " + data);
				isSuccess = data === "SUCCESS" ? true : false;
				console.log("isSuccess value after retrieving response = " + isSuccess.toString());
			}
		});

	    console.log("isSuccess value before return statement = " + isSuccess.toString());
	    return isSuccess;
	}, "This subject already exists! Please enter another subject name.");

	var validatorAddSubject = $("#addSubjectFormModal").validate({
		rules: {
			subjectNameModal: {
				required: true,
				maxlength: 200,
				checkSubjectName: true
			}
		},
		messages: {
			subjectNameModal: {
				required: "Subject name text field is required.",
				maxlength: "Given subject name is too long, please change it."
			}
		},
		highlight: function(event) {
			$(event).closest('.form-group').find('.form-control-feedback').addClass('glyphicon glyphicon-remove');
			$(event).closest('.form-group').addClass('has-error has-feedback');
		},
		unhighlight: function(event) {
			$(event).closest('.form-group').find('.form-control-feedback').removeClass('glyphicon glyphicon-remove');
			$(event).closest('.form-group').removeClass('has-error has-feedback');
		},
		submitHandler: function(form) {
			console.log("********* submitHandler *********");
			
			$.ajax({
				url: URLWithContextPath + "/subjects/doAddSubjectAjax",
				data: $(form).serialize(),
				type: "POST",
				success: function(data) {
					console.log("********* AJAX CALL *********");
					console.log("Status: " + data.status);
					console.log("Result: " + data.result);

					var subjectsTableDT = $('#subjects_table').DataTable();
					var rowNode = subjectsTableDT.row.add( [
						data.result.subjectName,
					    data.result.creationDate,
					    data.result.createdBy,
					    data.result.sendMessage,
					    data.result.editSubject,
					    data.result.deleteSubject
					] ).draw( false ).node();

					$(rowNode).addClass("success");
					$(rowNode).attr("id", data.result.subjectId);

					$("#addSubjectModal").modal('hide');
				}
			});
		}
	});

	$('#addSubjectBtn').on('click', function() {
		console.log("$('#addSubjectBtn').on('click')");
	});

	$('#addSubjectModal').on('show.bs.modal', function(event) {
		console.log("$('#addSubjectModal').on('show.bs.modal')");
	});

	$('#addSubjectModal').on('hide.bs.modal', function(event) {
		console.log("$('#addSubjectModal').on('hide.bs.modal')");
		
		validatorAddSubject.resetForm();
	});

	$('#addSubjectModal .modal-footer #addSubjectBtnModal').on('click', function(event) {
		console.log("$('#addSubjectModal .modal-footer #addSubjectBtnModal').on('click')");

		$('#addSubjectFormModal').submit();
	});

	/***************************************************************/
	/*** Adding validation and handling events for "editSubject" ***/
	/***************************************************************/

	var validatorEditSubject = $("#editSubjectFormModal").validate({
		rules: {
			subjectNameModal: {
				required: true,
				maxlength: 200,
				checkSubjectName: true
			}
		},
		messages: {
			subjectNameModal: {
				required: "Subject name text field is required.",
				maxlength: "Given subject name is too long, please change it."
			}
		},
		highlight: function(event) {
			$(event).closest('.form-group').find('.form-control-feedback').addClass('glyphicon glyphicon-remove');
			$(event).closest('.form-group').addClass('has-error has-feedback');
		},
		unhighlight: function(event) {
			$(event).closest('.form-group').find('.form-control-feedback').removeClass('glyphicon glyphicon-remove');
			$(event).closest('.form-group').removeClass('has-error has-feedback');
		},
		submitHandler: function(form) {
			console.log("********* submitHandler *********");
			
			$.ajax({
				url: URLWithContextPath + "/subjects/doEditSubjectAjax",
				data: $(form).serialize(),
				type: "POST",
				success: function(data) {
					console.log("********* AJAX CALL *********");
					console.log("Status: " + data.status);
					console.log("Result: " + data.result);

					var subjectId = $("#editSubjectModal").find('.modal-body #subjectReference').val();
					var subjectsTable = $('#subjects_table');
					var subjectRow = subjectsTable.find('#' + subjectId);

					// returns DataTables API instance with selected row in the result set
					var subjectRowDT = subjectsTable.DataTable().row(subjectRow);

					var cellsData = subjectRowDT.data();
					cellsData[0] = data.result.subjectName;
					cellsData[4] = data.result.editSubject;
					cellsData[5] = data.result.deleteSubject;
					subjectRowDT.data(cellsData);

					$("#editSubjectModal").modal('hide');
				}
			});
		}
	});

	$('#subjects_table #editSubjectBtn').on('click', function() {
		console.log("$('#subjects_table #editSubjectBtn').on('click')");
	});

	$('#editSubjectModal').on('show.bs.modal', function(event) {
		console.log("$('#editSubjectModal').on('show.bs.modal')");

		var button = $(event.relatedTarget);
		var subjectId = button.data('subject-reference');
		var subjectName = button.data('subject-name');
		
		$(this).find('.modal-body #subjectReference').val(subjectId);
		$(this).find('.modal-body #subjectNameModal').val(subjectName);
	});

	$('#editSubjectModal').on('hide.bs.modal', function(event) {
		console.log("$('#editSubjectModal').on('hide.bs.modal')");

		$(this).find('.modal-body #subjectReference').val("");
		
		validatorEditSubject.resetForm();
	});

	$('#editSubjectModal .modal-footer #editSubjectBtnModal').on('click', function(event) {
		console.log("$('#editSubjectModal .modal-footer #editSubjectBtnModal').on('click')");

		$('#editSubjectFormModal').submit();
	});

	/*****************************************************************/
	/*** Adding validation and handling events for "deleteSubject" ***/
	/*****************************************************************/

	var validatorDeleteGroup = $('#deleteSubjectFormModal').validate({
		submitHandler: function(form) {
			console.log("********* submitHandler *********");

			$.ajax({
				url: URLWithContextPath + "/subjects/doDeleteSubjectAjax",
				data: $(form).serialize(),
				type: "POST",
				success: function(data) {
					console.log("********* AJAX CALL *********");
					console.log("Status: " + data.status);
					console.log("Result: " + data.result);

					var subjectId = $("#confirmDeleteSubject").find('.modal-body #subjectReference').val();
					var subjectsTable = $('#subjects_table');
					var subjectRow = subjectsTable.find('#' + subjectId);

					// returns DataTables API instance with selected row in the result set
					var subjectRowDT = subjectsTable.DataTable().row(subjectRow);
					subjectRowDT.remove().draw();
					
					$("#confirmDeleteSubject").modal('hide');
				}
			});
		}
	});
	
	$('#subjects_table #deleteSubjectBtn').on('click', function() {
		console.log("$('#subjects_table #deleteSubjectBtn').on('click')");
	});
	
	$('#confirmDeleteSubject').on('show.bs.modal', function (event) {
		console.log("$('#confirmDeleteSubject').on('show.bs.modal')");

		var button = $(event.relatedTarget);
		var subjectId = button.data('subject-reference');
		var message = button.data('message');

		$(this).find('.modal-body #subjectReference').val(subjectId);
		$(this).find('.modal-body #confirmDeleteSubjectLabel').text(message);
	});

	$('#confirmDeleteSubject .modal-footer #confirmDeleteSubjectBtnModal').on('click', function(){
		console.log("$('#confirmDeleteSubject .modal-footer #confirmDeleteSubjectBtnModal').on('click')");
   
		$('#deleteSubjectFormModal').submit();
 	});

	/****************************************************************/
	/*** Adding validation and handling events for "askForAccess" ***/
	/****************************************************************/

	var validatorSendMessage = $("#sendMessageFormModal").validate({
		rules: {
			messageText: {
				required: true,
				maxlength: 2000
			}
		},
		messages: {
			messageText: {
				required: "Text of message is required.",
				maxlength: "Given message is too long, please change it."
			}
		},
		highlight: function(event) {
			$(event).closest('.form-group').find('.form-control-feedback').addClass('glyphicon glyphicon-remove');
			$(event).closest('.form-group').addClass('has-error has-feedback');
		},
		unhighlight: function(event) {
			$(event).closest('.form-group').find('.form-control-feedback').removeClass('glyphicon glyphicon-remove');
			$(event).closest('.form-group').removeClass('has-error has-feedback');
		},
		submitHandler: function(form) {
			console.log("********* submitHandler *********");
			
			$.ajax({
				url: URLWithContextPath + "/subjects/askForAccessAjax",
				data: $(form).serialize(),
				type: "POST",
				success: function(data) {
					console.log("********* AJAX CALL *********");
					console.log("Status: " + data.status);
					console.log("Result: " + data.result);

					// POWIADOMIENIE O WYSLANEJ WIADOMOSCI
					
					$("#sendMessageModal").modal('hide');
				}
			});
		}
	});

	$('#subjects_table #sendMessageBtn').on('click', function() {
		console.log("$('#subjects_table #sendMessageBtn').on('click')");
	});

	$('#sendMessageModal').on('show.bs.modal', function(event) {
		console.log("$('#sendMessageModal').on('show.bs.modal')");

		var button = $(event.relatedTarget);
		var recipient = button.data('recipient');
		var topic = button.data('topic');
		var subjectId = button.data('subject-reference');
		
		$(this).find('.modal-title').text('New message to ' + recipient);
		$(this).find('.modal-body #recipientName').val(recipient);
		$(this).find('.modal-body #topic').val('Access to subject \"' + topic + "\"");
		$(this).find('.modal-body #subjectReference').val(subjectId);
	});

	$('#sendMessageModal').on('hide.bs.modal', function(event) {
		console.log("$('#sendMessageModal').on('hide.bs.modal')");

		$(this).find('.modal-body #subjectReference').val("");
		$(this).find('.modal-body #messageText').css('height', '54px');
		$(this).find('.modal-body #messageText').css('width', '568px');
		
		validatorSendMessage.resetForm();
	});

	$('#sendMessageModal .modal-footer #sendMessageBtnModal').on('click', function(event) {
		console.log("$('#sendMessageModal .modal-footer #sendMessageBtnModal').on('click')");

		$('#sendMessageFormModal').submit();
	});
</script>

<%@ include file="partials/footer.jsp" %>