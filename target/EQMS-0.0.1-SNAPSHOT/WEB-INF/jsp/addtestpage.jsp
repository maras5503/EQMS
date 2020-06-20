<%@ include file="partials/header.jsp" %>

<div class="row">

    <div class="col-sm-12">		
    
		<div class="page-header" align="center" style="margin-top: 10px;">	            
            <h1>Add new test</h1>
        </div>
        
        <div class="col-sm-12">
        	<form class="form-horizontal" action="<c:url value="/tests/doAddTest"/>" method="post" id="addTestForm" enctype="multipart/form-data">
		       	
		       	<div class="well" style="margin-left: -15px; margin-right: -15px;">
		       		<div class="form-group">
					    <label for="testName" class="col-md-2 control-label">Test name</label>
					    <div class="col-md-6">
					    	<input type="text" class="form-control" id="testName" name="testName" placeholder="Test name" required="required">
					    </div>
					    <label for="timeForTest" class="col-md-1 control-label">Time</label>
					    <div class="col-md-1">
					    	<input type="number" class="form-control" id="timeForTest" name="timeForTest" min="1" step="1" required="required">
					    </div>
					    <div class="col-md-2" align="right">
					    	<input type="number" id="numberOfGroups" name="numberOfGroups" min="0" step="1" style="width: 50px; margin-right: 5px;">
					    	<input type="hidden" id="numberOfGroupsHidden" name="numberOfGroupsHidden" />
					        <button type="button" class="btn btn-success" id="addNewGroup" name="addNewGroup" style="margin-right: 5px;"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></button>
					        <button type="button" class="btn btn-primary" id="removeLastGroup" name="removeLastGroup"><span class="glyphicon glyphicon-minus" aria-hidden="true"></span></button>
					    </div>
			  		</div>
			  		<div class="form-group">
			  			<div class="col-md-4">
			  				<div class="col-md-12" style="margin-bottom: 10px;">
							    <label for="mark2" class="col-md-6 control-label">Mark 2 &nbsp; ></label>
						  		<div class="col-md-6">
						  			<input type="number" class="form-control" id="mark2" name="mark2" min="0" step="1" max="100" required="required">
						  		</div>
					  		</div>
					  		<div class="col-md-12" style="margin-bottom: 10px;">
						  		<label for="mark3" class="col-md-6 control-label">Mark 3 &nbsp; ></label>
						  		<div class="col-md-6">
						  			<input type="number" class="form-control" id="mark3" name="mark3" min="0" step="1" max="100" required="required" disabled="disabled">
						  		</div>
						  	</div>
							<div class="col-md-12">
						  		<label for="mark3_5" class="col-md-6 control-label">Mark 3+ ></label>
						  		<div class="col-md-6">
						  			<input type="number" class="form-control" id="mark3_5" name="mark3_5" min="0" step="1" max="100" required="required" disabled="disabled">
						  		</div>
						  	</div>
					  	</div>
					  	<div class="col-md-4">	
					  		<div class="col-md-12" style="margin-bottom: 10px;">
						  		<label for="mark4" class="col-md-6 control-label">Mark 4 &nbsp; ></label>
						  		<div class="col-md-6">
						  			<input type="number" class="form-control" id="mark4" name="mark4" min="0" step="1" max="100" required="required" disabled="disabled">
						  		</div>
						  	</div>
					  		<div class="col-md-12" style="margin-bottom: 10px;">
						  		<label for="mark4_5" class="col-md-6 control-label">Mark 4+ ></label>
						  		<div class="col-md-6">
						  			<input type="number" class="form-control" id="mark4_5" name="mark4_5" min="0" step="1" max="100" required="required" disabled="disabled">
						  		</div>
						  	</div>
						  	<div class="col-md-12">
							    <label for="mark5" class="col-md-6 control-label">Mark 5 &nbsp; ></label>
						  		<div class="col-md-6">
						  			<input type="number" class="form-control" id="mark5" name="mark5" min="0" step="1" max="100" required="required" disabled="disabled">
						  		</div>
					  		</div>
				  		</div>
			  		</div>
		       	</div>
			  	 
				<div class="form-group well" id="submitContainer">
					<div class="col-md-12">
						<button type="submit" class="btn btn-md btn-block btn-primary">Save</button>
						<input type="hidden" name="subjectId" id="subjectId" value="${subjectId}" />
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					</div>
				</div>
		  	</form>
        </div>
        
    </div>
        
</div>

<script type="text/javascript">
	var groupId = 1;
	var questionId = 1;
	var answerId = 1;

	$("#mark2").on('change keyup', function () {
		console.log("$(\"#mark2\").change()");

		if($(this).val().length != 0) {
			$("#mark3").prop('disabled', false);
			if(parseInt($(this).val()) != 100) {
				$("#mark3").attr('min', parseInt($(this).val())+1);
			}
		} else {
			$("#mark3").prop('disabled', true);
		}
	});

	$("#mark3").on('change keyup', function () {
		console.log("$(\"#mark3\").change()");

		if($(this).val().length != 0) {
			$("#mark3_5").prop('disabled', false);
			if(parseInt($(this).val()) != 100) {
				$("#mark3_5").attr('min', parseInt($(this).val())+1);
			}
		} else {
			$("#mark3_5").prop('disabled', true);
		}
	});

	$("#mark3_5").on('change keyup', function () {
		console.log("$(\"#mark3_5\").change()");

		if($(this).val().length != 0) {
			$("#mark4").prop('disabled', false);
			if(parseInt($(this).val()) != 100) {
				$("#mark4").attr('min', parseInt($(this).val())+1);
			}
		} else {
			$("#mark4").prop('disabled', true);
		}
	});

	$("#mark4").on('change keyup', function () {
		console.log("$(\"#mark4\").change()");

		if($(this).val().length != 0) {
			$("#mark4_5").prop('disabled', false);
			if(parseInt($(this).val()) != 100) {
				$("#mark4_5").attr('min', parseInt($(this).val())+1);
			}
		} else {
			$("#mark4_5").prop('disabled', true);
		}
	});

	$("#mark4_5").on('change keyup', function () {
		console.log("$(\"#mark4_5\").change()");

		if($(this).val().length != 0) {
			$("#mark5").prop('disabled', false);
			if(parseInt($(this).val()) != 100) {
				$("#mark5").attr('min', parseInt($(this).val())+1);
			}
		} else {
			$("#mark5").prop('disabled', true);
		}
	});

	/********************************************/
	/********** Button Groups for Test **********/
	/********************************************/

	$("#addNewGroup").click(function () {
		console.log("$(\"addNewGroup\").click()");
		
		var group_id = ($('.form-horizontal #group_container').length + 1).toString();
		console.log("group_id = " + group_id);
		appendGroup();
		
		if($("#numberOfGroups").val() != "") {
			var addOne = parseInt($("#numberOfGroups").val()) + 1;
			$("#numberOfGroups").val(addOne);
		} else {
			$("#numberOfGroups").val($('.form-horizontal #group_container').length);
			
		}
	});

	$("#removeLastGroup").click(function () {
		console.log("$(\"#removeLastGroup\").click()");
		if ($('.form-horizontal #group_container').length == 0) {
			console.log("$('.form-horizontal #group_container').length == 0");
			return false;
		}
		
		$('.form-horizontal #group_container:nth-last-child(2)').remove();	// :last - remove latest .form-group

		if($("#numberOfGroups").val() != "") {
			var substractOne = parseInt($("#numberOfGroups").val()) - 1;
			$("#numberOfGroups").val(substractOne);
		} else {
			$("#numberOfGroups").val($('.form-horizontal #group_container').length);
		}
	});

	$("#numberOfGroups").on('change keyup', function(){
		console.log("$(\"#numberOfGroups\").on('change keyup')");

		$(this).on('mousewheel.disableScroll', function (e) {
		    e.preventDefault()
		})
		
		if(($("#numberOfGroups").val() != "") && (parseInt($("#numberOfGroups").val()) >= 0)){
			console.log("$(\"#numberOfGroups\").val() != \"\"");
			
			var startIteration = $('.form-horizontal #group_container').length + 1;
			console.log("startIteration = " + startIteration.toString());
			
			var endIteration = $("#numberOfGroups").val();
			console.log("endIteration = " + endIteration.toString());

			if (startIteration <= endIteration) {	// add new group_containers
				for (i = startIteration; i <= endIteration; i++) { 
					appendGroup();
				}
			} else {	// remove redundant group_containers
				for (i = 1; i < startIteration-endIteration; i++) {
					$('.form-horizontal #group_container:nth-last-child(2)').remove();
				}
			}
			
		} else {
			console.log("$(\"#numberOfGroups\").val() == \"\"");
		}
	});

	/*********************************************/
	/********** Button Groups for Group **********/
	/*********************************************/
	
	$('#addTestForm').on('click', '#group_container #currentGroupBtnsContainer #removeCurrentGroup', function (e) {
		console.log("$('#addTestForm').on('click', '#group_container #currentGroupBtnsContainer #removeCurrentGroup')");
		
		$(this).closest('#group_container').remove();

		if($("#numberOfGroups").val() != "") {
			var substractOne = parseInt($("#numberOfGroups").val()) - 1;
			$("#numberOfGroups").val(substractOne);
		} else {
			$("#numberOfGroups").val($('.form-horizontal #group_container').length - 1);
		}
	});

	$('#addTestForm').on('click', '#group_container #currentGroupBtnsContainer #addNewQuestion', function (e) {
		console.log("$('#addTestForm').on('click', '#group_container #currentGroupBtnsContainer #addNewQuestion')");
		
		var closestGroupContainer = $(this).closest("#group_container");
		var closestNumberOfQuestions = closestGroupContainer.find("#numberOfQuestions");
		appendQuestion(closestGroupContainer);

		if(closestNumberOfQuestions.val() != "") {
			var addOne = parseInt(closestNumberOfQuestions.val()) + 1;
			closestNumberOfQuestions.val(addOne);
		} else {
			closestNumberOfQuestions.val(closestGroupContainer.find(".question_container").length);
		}
	});

	$('#addTestForm').on('click', '#group_container #currentGroupBtnsContainer #removeLastQuestion', function (e) {
		console.log("$('#addTestForm').on('click', '#group_container #currentGroupBtnsContainer #removeLastQuestion')");

		var closestGroupContainer = $(this).closest('#group_container');
		var closestNumberOfQuestions = closestGroupContainer.find("#numberOfQuestions");
		
		if (closestGroupContainer.find(".question_container").length == 0) {
			console.log("closestGroupContainer.find(\".question_container\").length == 0");
			return false;
		}
		
		closestGroupContainer.find("#question_container:nth-last-child(1)").remove();

		if(closestNumberOfQuestions.val() != "") {
			var substractOne = parseInt(closestNumberOfQuestions.val()) - 1;
			closestNumberOfQuestions.val(substractOne);
		} else {
			closestNumberOfQuestions.val(closestGroupContainer.find(".question_container").length);
		}
	});

	$('#addTestForm').on('click keyup', '#group_container #currentGroupBtnsContainer #numberOfQuestions', function(e) {
		console.log("$('#addTestForm').on('click keyup', '#group_container #currentGroupBtnsContainer #numberOfQuestions')");

		$(this).on('mousewheel.disableScroll', function (e) {
		    e.preventDefault()
		})
		
		var closestGroupContainer = $(this).closest("#group_container");

		if((closestGroupContainer.find("#numberOfQuestions").val() != "") && (parseInt(closestGroupContainer.find("#numberOfQuestions").val()) >= 0)){
			console.log("$(\"#numberOfQuestions\").val() != \"\"");
			
			var startIteration = closestGroupContainer.find(".question_container").length + 1;
			console.log("startIteration = " + startIteration.toString());
			
			var endIteration = closestGroupContainer.find("#numberOfQuestions").val();
			console.log("endIteration = " + endIteration.toString());

			if (startIteration <= endIteration) {	// add new question_containers
				for (i = startIteration; i <= endIteration; i++) { 
					appendQuestion(closestGroupContainer);
				}
			} else {	// remove redundant question_containers
				for (i = 1; i < startIteration-endIteration; i++) {
					closestGroupContainer.find("#question_container:last").remove();
				}
			}
		} else {
			console.log("$(\"#numberOfQuestions\").val() == \"\"");
		}
	});

	/************************************************/
	/********** Button Groups for Question **********/
	/************************************************/

	$('#addTestForm').on('click', '#group_container #question_container #currentQuestionBtnsContainer #removeCurrentQuestion', function (e) {
		console.log("$('#addTestForm').on('click', '#group_container #question_container #currentQuestionBtnsContainer #removeCurrentQuestion')");

		var closestGroupContainer = $(this).closest("#group_container");
		var closestNumberOfQuestions = closestGroupContainer.find("#numberOfQuestions");
		$(this).closest('#question_container').remove();
		
		if(closestNumberOfQuestions.val() != "") {
			var substractOne = parseInt(closestNumberOfQuestions.val()) - 1;
			closestNumberOfQuestions.val(substractOne);
		} else {
			closestNumberOfQuestions.val(closestGroupContainer.find(".question_container").length - 1);
		}
	});

	$('#addTestForm').on('click', '#group_container #question_container #currentQuestionBtnsContainer #addNewAnswer', function (e) {
		console.log("$('#addTestForm').on('click', '#group_container #question_container #currentQuestionBtnsContainer #addNewAnswer')");

		var closestGroupContainer = $(this).closest("#group_container");
		var closestQuestionContainer = $(this).closest("#question_container");
		var closestNumberOfAnswers = closestQuestionContainer.find("#numberOfAnswers");
		appendAnswer(closestQuestionContainer);
		
		if(closestNumberOfAnswers.val() != "") {
			var addOne = parseInt(closestNumberOfAnswers.val()) + 1;
			closestNumberOfAnswers.val(addOne);
		} else {
			closestNumberOfAnswers.val(closestQuestionContainer.find(".answer_container").length);
		}
	});

	$('#addTestForm').on('click', '#group_container #question_container #currentQuestionBtnsContainer #removeLastAnswer', function (e) {
		console.log("$('#addTestForm').on('click', '#group_container #question_container #currentQuestionBtnsContainer #removeLastAnswer')");

		var closestQuestionContainer = $(this).closest('#question_container');
		var closestNumberOfAnswers = closestQuestionContainer.find("#numberOfAnswers");
		
		if (closestQuestionContainer.find(".answer_container").length == 0) {
			console.log("closestQuestionContainer.find(\".answer_container\").length == 0");
			return false;
		}
		
		closestQuestionContainer.find("#answer_container:nth-last-child(1)").remove();

		if(closestNumberOfAnswers.val() != "") {
			var substractOne = parseInt(closestNumberOfAnswers.val()) - 1;
			closestNumberOfAnswers.val(substractOne);
		} else {
			closestNumberOfAnswers.val(closestQuestionContainer.find(".answer_container").length);
		}
	});

	$('#addTestForm').on('click keyup', '#group_container #question_container #currentQuestionBtnsContainer #numberOfAnswers', function(e) {
		console.log("$('#addTestForm').on('click keyup', '#group_container #question_container #currentQuestionBtnsContainer #numberOfAnswers')");

		$(this).on('mousewheel.disableScroll', function (e) {
		    e.preventDefault()
		})
		
		var closestGroupContainer = $(this).closest("#group_container");
		var closestQuestionContainer = $(this).closest("#question_container");
		
		if((closestQuestionContainer.find("#numberOfAnswers").val() != "") && (parseInt(closestQuestionContainer.find("#numberOfAnswers").val()) >= 0)){
			console.log("$(\"#numberOfAnswers\").val() != \"\"");
			
			var startIteration = closestQuestionContainer.find(".answer_container").length + 1;
			console.log("startIteration = " + startIteration.toString());
			
			var endIteration = closestQuestionContainer.find("#numberOfAnswers").val();
			console.log("endIteration = " + endIteration.toString());

			if (startIteration <= endIteration) {	// add new answer_containers
				for (i = startIteration; i <= endIteration; i++) { 
					appendAnswer(closestQuestionContainer);
				}
			} else {	// remove redundant answer_containers
				for (i = 1; i < startIteration-endIteration; i++) {
					closestQuestionContainer.find("#answer_container:last").remove();
				}
			}
		} else {
			console.log("$(\"#numberOfAnswers\").val() == \"\"");
		}
	});

	$('#addTestForm').on('change', '#group_container #question_container input[name=inputImageForQuestion]', function(e) {
		console.log("$('addTestForm').on('change', '#group_container #question_container input[name=inputImageForQuestion]')");
		readURL(this, $(this).closest('#question_container').find("#imageForQuestion"));
	});

	/**********************************************/
	/********** Button Groups for Answer **********/
	/**********************************************/
	
	$('#addTestForm').on('click', '#group_container #question_container #answer_container #removeCurrentAnswer', function (e) {
		console.log("$('#addTestForm').on('click', '#group_container #question_container #answer_container #removeCurrentAnswer')");

		var closestQuestionContainer = $(this).closest("#question_container");
		var closestNumberOfAnswers = closestQuestionContainer.find("#numberOfAnswers");
		$(this).closest('#answer_container').remove();

		if(closestNumberOfAnswers.val() != "") {
			var substractOne = parseInt(closestNumberOfAnswers.val()) - 1;
			closestNumberOfAnswers.val(substractOne);
		} else {
			closestNumberOfAnswers.val(closestQuestionContainer.find(".answer_container").length - 1);
		}
	});

	$('#addTestForm').on('change', '#group_container #question_container #answer_container input[name=inputImageForAnswer]', function(e) {
		console.log("$('addTestForm').on('change', '#group_container #question_container #answer_container input[name=inputImageForAnswer]')");
		readURL(this, $(this).closest('#answer_container').find("#imageForAnswer"))
	});
	
	/**************************************/
	/********** Global functions **********/
	/**************************************/
	
	jQuery.validator.addMethod("checkTestName", function(value, element) {
		var isSuccess = false;
		console.log("isSuccess value before ajax call = " + isSuccess.toString());

		var data = {};
		data[csrfParameter] = csrfToken;
		data["testName"] = value;
		data["subjectId"] = $("#addTestForm #submitContainer").find('#subjectId').val();
		
		console.log("testName value = " + value);
		console.log("subjectId = " + $("#addTestForm #submitContainer").find('#subjectId').val());
	    
	    $.ajax({
			url: URLWithContextPath + "/tests/checkTestName",
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
	}, "This test already exists! Please enter another test name.");
	
	var validatorAddTest = $("#addTestForm").validate({
		rules: {
			testName: {
				required: true,
				maxlength: 200,
				checkTestName: true
			},
			timeForTest: {
				required: true,
			},
			mark2: {
				required: true,
			},
			mark3: {
				required: true,
			},
			mark3_5: {
				required: true,
			},
			mark4: {
				required: true,
			},
			mark4_5: {
				required: true,
			},
			mark5: {
				required: true,
			},
			groupName: {
				required: true,
				maxlength: 200
			},
			contentOfQuestion: {
				required: true,
				maxlength: 2000
			},
			contentOfAnswer: {
				required: true,
				maxlength: 2000
			},
			whetherCorrectSelect: {
				required: true,
			}
		},
		messages: {
			testName: {
				required: "Test name is required.",
				maxlength: "Given test name is to long, please change it.",
			},
			timeForTest: {
				required: "Time for test is required."
			},
			mark2: {
				required: "Mark 2 is required."
			},
			mark3: {
				required: "Mark 3 is required."
			},
			mark3_5: {
				required: "Mark 3+ is required."
			},
			mark4: {
				required: "Mark 4 is required."
			},
			mark4: {
				required: "Mark 4+ is required."
			},
			mark5: {
				required: "Mark 5 is required."
			},
			groupName: {
				required: "Group name is required.",
				maxlength: "Given group name is to long, please change it.",
			},
			contentOfQuestion: {
				required: "Content of question is required.",
				maxlength: "Given content of question is to long, please change it.",
			},
			contentOfAnswer: {
				required: "Content of answer is required.",
				maxlength: "Given content of asnwer is to long, please change it.",
			},
			whetherCorrectSelect: {
				required: "This select is required."
			}
		},
		highlight: function(event) {
			$(event).closest('.form-group').find('.form-control-feedback').addClass('glyphicon glyphicon-remove');
			$(event).closest('.form-group').addClass('has-error has-feedback');
		},
		unhighlight: function(event) {
			$(event).closest('.form-group').find('.form-control-feedback').removeClass('glyphicon glyphicon-remove');
			$(event).closest('.form-group').removeClass('has-error has-feedback');
		}
	});
	
	function readURL(input, imagePreviewReference) {
		if (input.files && input.files[0]) {
	        var reader = new FileReader();
	
	        reader.onload = function (e) {
	        	imagePreviewReference.attr('hidden', false);
	        	imagePreviewReference.attr('src', e.target.result);
	        }
	
	        reader.readAsDataURL(input.files[0]);
	    }
	}
	
	function appendGroup() { 
		$("#submitContainer").before('<div class="well" id="group_container" style="margin-left: -15px; margin-right: -15px;">' +
				'<div class="form-group" style="padding: 0px; margin-bottom: 0px;">' + 
					'<label for="groupName" class="col-md-2 control-label"> Group name</label>' +
					'<div class="col-md-7">' +
						'<input type="text" class="form-control" id="groupName_' + groupId + '" name="groupName" placeholder="Group name" required="required">' +
					'</div>' +
					'<div class="col-md-3" id="currentGroupBtnsContainer" align="right" style="padding-right: 15px;">' +
						'<input type="number" id="numberOfQuestions" name="numberOfQuestions" min="0" step="1" style="width: 50px; margin-right: 10px;">' +
						'<input type="hidden" id="numberOfQuestionsHidden" name="numberOfQuestionsHidden" value="0" />' +
						'<button type="button" class="btn btn-success" id="addNewQuestion" name="addNewQuestion" style="margin-right: 10px;"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></button>' +
						'<button type="button" class="btn btn-primary" id="removeLastQuestion" name="removeLastQuestion" style="margin-right: 10px;"><span class="glyphicon glyphicon-minus" aria-hidden="true"></span></button>' +
						'<button type="button" class="btn btn-danger" id="removeCurrentGroup" name="removeCurrentGroup"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button>' +
					'</div>' +
				'</div>' +
		'</div>');
		groupId = groupId + 1
	}

	function appendQuestion(closestGroupContainer) {
		closestGroupContainer.append('<div id="question_container" class="question_container" style="margin-top: 15px;">' +
				'<div class="form-group" style="border-bottom: 2px solid #E3E3E3; margin-bottom: 15px;"></div>' +
				'<div class="form-group" style="padding: 0px;">' +
					'<label for="contentOfQuestion" class="col-md-2 control-label" style="padding-left: 0px;"> Question</label>' +
					'<div class="col-md-7">' +
						'<textarea class="form-control resizable" name="contentOfQuestion" id="contentOfQuestion_' + questionId + '" placeholder="Content of question" maxlength="2000" required="required"></textarea>' +
					'</div> ' +
					'<div class="col-md-3" id="currentQuestionBtnsContainer" align="right" style="padding-right: 15px;">' +
						'<input type="number" id="numberOfAnswers" name="numberOfAnswers" min="0" step="1" style="width: 50px; margin-right: 10px;">' +
						'<input type="hidden" id="numberOfAnswersHidden" name="numberOfAnswersHidden" value="0" />' +
						'<button type="button" class="btn btn-success" id="addNewAnswer" name="addNewAnswer" style="margin-right: 10px;"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></button>' +
						'<button type="button" class="btn btn-primary" id="removeLastAnswer" name="removeLastAnswer" style="margin-right: 10px;"><span class="glyphicon glyphicon-minus" aria-hidden="true"></span></button>' +
						'<button type="button" class="btn btn-danger" id="removeCurrentQuestion" name="removeCurrentQuestion"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button>' +
					'</div>' +
				'</div>' +
				'<div class="form-group" style="padding: 0px; margin-bottom: 0px;">' +
					'<label for="question_input_image" class="col-md-2 control-label" style="padding-top: 0px">Picture</label>' +
					'<div class="col-md-4">' +
						'<input type="file" id="inputImageForQuestion_' + questionId + '" name="inputImageForQuestion" accept=".jpg,.jpeg,.png,.gif,.bmp">' +
					'</div>' +
					'<div class="col-md-6" align="center">' +
						'<img id="imageForQuestion" src="#" alt="Input image for question" width="75%" height="75%" hidden="true"/>' +
					'</div>' +
				'</div>' +
		'</div>');
		questionId = questionId + 1;
	}

	function appendAnswer(closestQuestionContainer) {
		closestQuestionContainer.append('<div id="answer_container" class="answer_container" style="margin-top: 15px;">' +
				'<div class="form-group" style="border-bottom: 2px dotted #E3E3E3; margin-bottom: 0px;"></div>' +
				'<div class="form-group" style="margin-top: 15px;">' +
					'<img class="col-md-1" src="<c:url value="/icons/right_arrow_transparent_50_procent.png" />">' + 
					'<label for="contentOfAnswer" class="col-md-2 control-label"> Content of answer</label>' +
					'<div class="col-md-6">' +
						'<textarea class="form-control resizable" name="contentOfAnswer" id="contentOfAnswer_' + answerId + '" placeholder="Content of answer" maxlength="2000" required="required"></textarea>' +
					'</div>' +
					'<div class="col-md-2" align="right" style="padding-right: 15px;">' +
						'<select class="form-control" id="whetherCorrectSelect_' + answerId + '" name="whetherCorrectSelect" required="required">' +
							'<option value="" selected="selected">Select true or false</option>' +
							'<option value="true">true</option>' +
							'<option value="false">false</option>' +
						'</select>' +
					'</div>' +
					'<div class="col-md-1" align="right" style="padding-right: 15px;">' +
						'<button type="button" class="btn btn-danger" id="removeCurrentAnswer" name="removeCurrentAnswer" style="margin-left: 10px;"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button>' +
					'</div>' +
				'</div>' +
				'<div class="form-group" style="padding: 0px;">' +
					'<label for="answer_input_image" class="col-md-offset-1 col-md-2 control-label" style="padding-top: 0px">Picture</label>' +
					'<div class="col-md-4">' +
						'<input type="file" id="inputImageForAnswer_' + answerId + '" name="inputImageForAnswer" accept=".jpg,.jpeg,.png,.gif,.bmp">' +
					'</div>' +
					'<div class="col-md-5" align="center">' +
						'<img id="imageForAnswer" src="#" alt="Input image for answer" width="75%" height="75%" hidden="true"/>' +
					'</div>' +
				'</div>' +
		'</div>');
		answerId = answerId + 1;
	}
</script>

<%@ include file="partials/footer.jsp" %>