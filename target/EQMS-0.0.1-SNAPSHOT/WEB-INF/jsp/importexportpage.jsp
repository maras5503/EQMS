<%@ include file="partials/header.jsp" %>

<div class="row">
    <div class="col-sm-12">
    
    	<div class="page-header" align="center" style="margin-top: 10px;">	            
            <h1>Import / Export</h1>
        </div>
    
    	<div class="col-sm-6">
    		<h3 style="text-align: center">Export</h3>
    		
    		<label class="control-label col-sm-12" style="text-align: center; margin-top: 17px; margin-bottom: 16px;">Choose subject:</label>
    		<div class="col-sm-12" id="subjectSelect" align="center" style="padding-bottom: 10px">
		    	<select class="form-control" id="subjectDropDown" name="subjectDropDown">	
					<option value="" selected="selected">Select subject</option>	
					
					<c:forEach var="subject" items="${userSubjects}">
			    		<option value="${subject.subjectId}">${subject.subjectName}</option>
			    	</c:forEach>	
				</select>
	    	</div>
    		
	    	<label class="control-label col-sm-12" style="text-align: center; margin-top: 7px; margin-bottom: 16px;">Choose test:</label>
	    	<div class="col-sm-12" id="testSelect" align="center" style="padding-bottom: 20px">
		    	<select class="form-control" id="testDropDown" name="testDropDown" disabled="disabled">	
					<option value="" selected="selected">Select test</option>
				</select>
	    	</div>
	    	
	    	<div class="col-sm-12" id="groupsDiv" align="center" style="padding-bottom: 20px">
	    	</div>
	    	
	    	<div class="col-sm-12" style="text-align: center; margin-bottom: 20px;">
    			<button type="button" class="btn btn-primary" id="submitExportForm">Export</button>
    		</div>
    		
    		<div class="col-sm-12" id="exportResultDiv" style="text-align: center;" hidden="hidden">
    			<h3><label>Export result:</label></h3>
    			<div class="col-sm-12" id="exportResultDivContent" style="text-align: left;">
    				<h4></h4>
    			</div>
    		</div>
    	</div>
    	
    	<div class="col-sm-6">
    		<h3 style="text-align: center">Import</h3>
    		
    		<div class="col-sm-12" id="importTestDiv" align="center" style="padding-bottom: 20px">
    			<form class="form-horizontal" action="<c:url value="/import_export/doImportAjax"/>" method="POST" id="importForm" enctype="multipart/form-data; charset=UTF-8" >	<!-- accept-charset="utf-8" -->
					<div class="form-group" style="margin-bottom: 10px; margin-top: 10px;">
						<label class="control-label col-sm-12" style="text-align: center; padding-bottom: 6px;">Choose subject:</label>
					</div>
					<div class="form-group" id="subjectSelectForImport" style="margin-bottom: 10px; margin-top: 10px;">
				    	<select class="form-control" id="subjectDropDownForImport" name="subjectDropDownForImport">	
							<option value="" selected="selected">Select subject</option>	
							
							<c:forEach var="subject" items="${userSubjects}">
					    		<option value="${subject.subjectId}">${subject.subjectName}</option>
					    	</c:forEach>	
						</select>
			    	</div>
					<div class="form-group" style="margin-bottom: 10px; margin-top: 10px;">
						<label class="control-label col-sm-12" style="text-align: center; padding-bottom: 6px;">Choose file from disk, that contains test to import:</label>
					</div>
					<div class="form-group" style="margin-bottom: 10px; margin-top: 10px;">
						<input type="file" id="inputTestFile" name="inputTestFile" accept=".tg_org,.tg" formenctype="multipart/form-data"/>
					</div>
					<input type="hidden" name="subjectReference" id="subjectReference" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
    		</div>
    		
    		<div class="col-sm-12" style="text-align: center; margin-bottom: 20px;">
    			<button type="button" class="btn btn-success" id="previewImportBtn" style="margin-right: 10px;">Preview</button>
    			<button type="button" class="btn btn-primary" id="submitImportForm">Import</button>
    		</div>
    		
    		<div class="col-sm-12" id="importResultDiv" style="text-align: center;" hidden="hidden">
    			<h3><label>Import result:</label></h3>
    			<div class="col-sm-12" id="importResultDivContent" style="text-align: left;">
    				<h4></h4>
    			</div>
    		</div>
    	</div>
    </div>
</div>

<div class="modal fade" id="importPreviewModal" tabindex="-1" role="dialog" aria-labelledby="importPreviewLabelModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="importPreviewTitleModal" style="text-align: center;">Import Preview</h4>
			</div>
			<div class="modal-body">
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">

	/*********************************************************************************/
	/************ Adding validation and handling events for "exportForm" *************/
	/*********************************************************************************/

	$("#subjectDropDown").change(function(){
		
		var data = {};
		data[csrfParameter] = csrfToken;
		data["subjectId"] = $(this).val();
		
		if($(this).val() != "") {
			$.ajax({
				url: URLWithContextPath + "/import_export/getTestsBySubjectSelect",    
				data: data,
				dataType: 'json',
				type: "POST",
				success: function(data){
					console.log(data);

					$("#testDropDown").empty();
					$("#testDropDown").append(data.result);
					$('#groupsDiv').html("");
					$('#exportResultDiv').hide();
					
					if(data.status == "FAIL"){
						console.log("Error in retrieving data from response");
						$("#testDropDown").prop('disabled', true);
					} else if(data.status == "SUCCESS") {
						console.log("Correct response");
						$("#testDropDown").prop('disabled', false);	
					}
				}
			});
		} else {
			$("#testDropDown").prop('disabled', true);
			$("#testDropDown").empty();
			$("#testDropDown").append('<option value="" selected="selected">Select test</option>');
			$('#groupsDiv').html("");
			$('#exportResultDiv').hide();
		}
	});

	$("#testDropDown").change(function(){
		
		var data = {};
		data[csrfParameter] = csrfToken;
		data["testId"] = $(this).val();

		if($(this).val() != "") {
			$.ajax({
				url: URLWithContextPath + "/import_export/getExportFormByTestSelect",    
				data: data,
				dataType: 'json',
				type: "POST",
				success: function(data){
					console.log(data);
					$('#groupsDiv').html(data.result);
					$('#exportResultDiv').hide();
				}
			});
		} else {
			$('#groupsDiv').html("");
			$('#exportResultDiv').hide();
		}
	});
	
	$('#submitExportForm').on('click', function() {
		console.log("$('#submitExportForm').on('click')");

		/*
			In jquery.validate.js, we can find a function named checkForm, we have to modify it as below:
		
			checkForm: function() {
				this.prepareForm();
				for ( var i = 0, elements = (this.currentElements = this.elements()); elements[i]; i++ ) {
					if (this.findByName( elements[i].name ).length != undefined && this.findByName( elements[i].name ).length > 1) {
						for (var cnt = 0; cnt < this.findByName( elements[i].name ).length; cnt++) {
							this.check( this.findByName( elements[i].name )[cnt] );
						}
					} else {
						this.check( elements[i] );
					}
				}
			return this.valid();
			}
			
			This fragment of code was changed, because in this application we need to validate an array of input elements.
			jQuery Validation Plugin doesn't support it at all. We have to also set different ids for inputs with same name.
		*/

		// validator for form is placed here, because form it's added dynamically
		var validatorExportForm = $('#groupsDiv #exportForm').validate({
			rules: {
				numberOfQuestions: {
					required: true,
				}
			},
			messages: {
				numberOfQuestions: {
					required: "Number of questions for this group is required.",
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
				
				$(form).ajaxSubmit({
					url: URLWithContextPath + "/import_export/doExportAjax",
					dataType: "json",
					success: function(data) { 
						console.log("********* AJAX CALL *********");
						console.log("Status: " + data.status);
						console.log("Result: " + data.result);

						$('#exportResultDivContent h4').html(data.result);
						$('#exportResultDiv').show();
					}
				});
			}
		});
		
		/*
			We can also use validation for array of input elements in this way:
				 
			$("[name=numberOfQuestions]").each(function(){
	
				$(this).rules("add", {
					required: true,
					messages: {
						required: "Number of questions for this group is required."
					}
				} );		
			});
		*/

		$('#groupsDiv #exportForm').submit();
	});
	
	/* $('#fileURL').on('change', function(event) {
		console.log("$('fileURL').on('change')");
	
		var files = event.target.files;
		var file;
		var extension;
		
		for(var i=0, len=files.length; i<len; i++) {
			file = files[i];
			extension = file.name.split(".").pop();
			console.log(file.name);
		}
	}); */

	/*********************************************************************************/
	/************ Adding validation and handling events for "importForm" *************/
	/*********************************************************************************/

	$("#subjectDropDownForImport").change(function(){
		if($(this).val() != "") {
			var currentSubjectValue = $(this).val();
			$('#importForm').find('#subjectReference').val(currentSubjectValue);
		} 
		$('#importResultDiv').hide();
	});

	$("#inputTestFile").change(function() {
		$('#importResultDiv').hide();
	});
	
	var validatorImportForm = $('#importForm').validate({
		rules: {
			subjectDropDownForImport: {
				required: true,
			},
			inputTestFile: {
				required: true,
			}
		},
		messages: {
			subjectDropDownForImport: {
				required: "You have to choose subject.",
			},
			inputTestFile: {
				required: "You have to choose test file with the appropriate extension.",
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
			
			// contentType (default: 'application/x-www-form-urlencoded; charset=UTF-8')
			$(form).ajaxSubmit({
				//url: URLWithContextPath + "/import_export/doImportAjax",
				contentType: 'multipart/form-data; charset=UTF-8',
				dataType: "json",
				success: function(data) { 
					console.log("********* AJAX CALL *********");
					console.log("Status: " + data.status);
					console.log("Result: " + data.result);

					// If response contains errors or informations show them
					if(data.result.previewMessage != null) {
						$('#importResultDivContent h4').html(data.result.previewMessage);
						$('#importResultDiv').show();
						
					} else if(data.result.successMessage != null) {
						$('#importResultDivContent h4').html(data.result.successMessage);
						$('#importResultDiv').show();
						
					} else if(data.result.importErrors != null) {
						$('#importResultDivContent h4').html(data.result.importErrors);
						$('#importResultDiv').show();
					}

					// If test was imported from file, don't show preview again
					if(data.status == null) {
						$('#importPreviewModal').find('.modal-body').html(data.result.modalBody);
						$('#importPreviewModal').modal('show');
					}
				}
			});
		}
	});

	$('#submitImportForm').on('click', function() {
		console.log("$('#submitImportForm').on('click')");

		$('#importForm').attr('action', URLWithContextPath + "/import_export/doImportAjax")
		$('#importForm').submit();
	});

	$('#previewImportBtn').on('click', function() {
		console.log("$('#previewImportBtn').on('click')");
		
		$('#importForm').attr('action', URLWithContextPath + "/import_export/doPreviewAjax");
		$('#importForm').submit();
	});

</script>

<%@ include file="partials/footer.jsp" %>