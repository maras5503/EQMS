<%@ include file="partials/header.jsp" %>

<div class="row">

	<div class="col-sm-12" style="padding-left: 0px; padding-right: 0px;">

		<div class="page-header" align="center" style="margin-top: 10px;">
			<h1>Tests</h1>
		</div>

		<div class="col-sm-offset-3 col-sm-6" id="subjectSelect" align="center" style="padding-bottom: 20px">
			<select class="form-control" id="subjectDropDown" name="subjectDropDown">
				<option value="" selected="selected">Select subject</option>

				<c:forEach var="subject" items="${userSubjects}">
					<option value="${subject.subjectId}">${subject.subjectName}</option>
				</c:forEach>
			</select>
		</div>

		<div id="addTestDiv" class="span3" align="right" hidden="hidden">
			<form action="<c:url value="/tests/addTest"/>" method="POST" id="addTestForm" >
				<input type="hidden" name="subjectReference" id="subjectReference" />
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			</form>
			<button type="button" class="btn btn-success" id="addTestBtn">Add new test</button>
		</div>

		<div class="col-sm-12" id="testsDiv" style="padding-right: 0px; padding-left: 0px;">
		</div>

	</div>

</div>

<div class="modal fade" id="confirmDeleteTest" tabindex="-1" role="dialog" aria-labelledby="confirmDeleteTestLabelModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="confirmDeleteTestTitleModal">Delete</h4>
			</div>
			<div class="modal-body">
				<form action="<c:url value="/tests/doDeleteTest"/>" method="POST" id="deleteTestFormModal" >
					<div class="form-group" id="confirmDeleteTestLabel">
					</div>
					<input type="hidden" name="testReference" id="testReference" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-danger" id="confirmDeleteTestBtnModal">Delete</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">		
	$('#addTestBtn').on('click', function() {
		$('#addTestForm').submit();
	});

	$("#subjectDropDown").change(function(){

		console.log("csrfParameter = " + csrfParameter);
		console.log("csrfToken = " + csrfToken);
		
		var data = {};
		data[csrfParameter] = csrfToken;
		data["subjectId"] = $(this).val();
		
		if($(this).val() != "") {

			$('#addTestDiv').show();
			$('#addTestForm').find('#subjectReference').val($(this).val());

			$.ajax({
			url: URLWithContextPath + "/tests/getTestsBySubject",    
			data: data,
			dataType: 'json',
			type: "POST",
			success: function(data){
				console.log(data);
				
				if(data.status == "FAIL"){
					console.log("Error in retrieving data from response");
				} else if(data.status == "SUCCESS") {
					console.log("Correct response");
					$("#testsDiv").html(data.result);

					var table = $('#tests_table').DataTable( {
						"paging": true,
					    "searching": true,
					    "ordering":  true,
					    "lengthMenu": [ [1, 5, 10, 25, 50, -1], [1, 5, 10, 25, 50, "All"] ],
					    "pageLength": 10,
					    "columns": [
					        null,
					        null,
					        null,
					        null,
					        null,
					        null,
					        null,
					        null,
					        { "orderable": false },
					        { "orderable": false }
					    ],
					    "language": {
					    	"info": "Showing _START_ to _END_ of _TOTAL_ tests",		// default is: Showing _START_ to _END_ of _TOTAL_ entries
					    	"infoEmpty": "Showing 0 to 0 of 0 tests", 					// default is: Showing 0 to 0 of 0 entries
					    	"infoFiltered": "(filtered from _MAX_ total tests)",		// default is: (filtered from _MAX_ total entries)
					    	"lengthMenu": "Show _MENU_ tests"							// default is: Show _MENU_ entries
						}
					} );
				}
			}        
			});
		} else {
			$('#testsDiv').html('');
			$('#addTestDiv').hide();
			$('#addTestForm').find('#subjectReference').val("");
		}
	});

	/******************************************************************/
	/***** Adding validation and handling events for "deleteTest" *****/
	/******************************************************************/
	
	var validatorDeleteTest = $('#deleteTestFormModal').validate({
		submitHandler: function(form) {
			console.log("***** submitHandler *****");

			$.ajax({
				url: URLWithContextPath + "/tests/doDeleteTestAjax",
				data: $(form).serialize(),
				type: "POST",
				success: function(data) {
					console.log("***** AJAX CALL *****");
					console.log("Status: " + data.status);
					console.log("Result: " + data.result);

					$("#confirmDeleteTest").modal('hide');

					var testId = $('#confirmDeleteTest .modal-body #testReference').val()
					console.log("testId = " + testId);
					var testTable = $('#testsDiv').find("#tests_table");
					var testRow = testTable.find('tr[data-key="' + testId +'"]');

					// returns DataTables API instance with selected row in the result set
					var testRowDT = testTable.DataTable().row(testRow);
					testRowDT.remove().draw();
				}
			});
		}
	});
	
	$('#confirmDeleteTest').on('show.bs.modal', function (event) {
		console.log("$('#confirmDeleteTest').on('show.bs.modal')");

		var button = $(event.relatedTarget);
		var testId = button.data('test-reference');
		var message = button.data('message');

		$(this).find('.modal-body #testReference').val(testId);
		$(this).find('.modal-body #confirmDeleteTestLabel').text(message);
	});

	$('#confirmDeleteTest .modal-footer #confirmDeleteTestBtnModal').on('click', function(){
		console.log("$('#confirmDeleteTest .modal-footer #confirmDeleteTestBtnModal').on('click')");
   
		$('#deleteTestFormModal').submit();
 	});
</script>

<%@ include file="partials/footer.jsp" %>