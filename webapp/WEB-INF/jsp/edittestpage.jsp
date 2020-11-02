<%@ include file="partials/header.jsp" %>

<div class="row">

    <div class="col-sm-12" id="testDiv">		
    
		<div class="page-header" align="center" style="margin-top: 10px;">	            
            <h1>Edit test</h1>
        </div>
        
        <div class="col-sm-12 well">
        	<div class="col-sm-12">
        		<label for="testName" class="col-sm-3 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Test name:</label>
				<label class="col-sm-5 control-label" id="testName" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${test.testName}</label>
				<div class="col-sm-4" style="text-align: right;">
                    <c:choose>
                        <c:when test="${test.enabled ==false}">
                            <button type="button" class="btn btn-primary btn-sm" id="enableTestBtn" name="enableTestBtn" data-toggle="modal" data-target="#confirmEnableTest" data-title="Enable Test" data-message="Are you sure you want to enable test '${test.testName}'?" data-test-reference="${test.testId}" style="margin-right: 5px;"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span> Enable test</button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="btn btn-danger btn-sm" id="disableTestBtn" name="disableTestBtn" data-toggle="modal" data-target="#confirmDisableTest" data-title="Disable Test" data-message="Are you sure you want to disable test '${test.testName}'?" data-test-reference="${test.testId}" style="margin-right: 5px;"><span class="glyphicon glyphicon-lock" aria-hidden="true"></span> Disable test</button>
                        </c:otherwise>
                    </c:choose>
					<button type="button" class="btn btn-success btn-sm" id="addGroupBtn" name="addGroupBtn" data-toggle="modal" data-target="#addGroupModal" data-test-reference="${test.testId}" style="margin-right: 5px;"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add new group</button>
					<button type="button" class="btn btn-info btn-sm" id="editTestBtn" name="editTestBtn" data-toggle="modal" data-target="#editTestModal" data-test-name="${test.testName}" data-time-for-test="${test.timeForTest}" data-mark2="${set.mark2}" data-mark3="${set.mark3}" data-mark3_5="${set.mark3_5}" data-mark4="${set.mark4}" data-mark4_5="${set.mark4_5}" data-mark5="${set.mark5}" data-subject-reference="${subject.subjectId}" data-test-reference="${test.testId}" data-set-reference="${set.setId}" style="margin-right: 5px;"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit</button>
				</div>
				
				<label for="timeForTest" class="col-md-3 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Time for test:</label>
				<label class="col-sm-9 control-label" id="timeForTest" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${test.timeForTest} min</label>
				
				<label for="numberOfGroups" class="col-md-3 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Number of groups:</label>
				<label class="col-sm-9 control-label" id="numberOfGroups" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${test.numberOfGroups}</label>
				
				<label for="numberOfQuestions" class="col-md-3 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Number of questions:</label>
				<label class="col-sm-9 control-label" id="numberOfQuestions" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${test.numberOfQuestions}</label>
				
				<label for="creationDate" class="col-md-3 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Creation date:</label>
				<label class="col-sm-9 control-label" id="creationDate" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${test.creationDate}</label>
				
				<label for="createdBy" class="col-md-3 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Created by:</label>
				<label class="col-sm-9 control-label" id="createdBy" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${test.createdBy}</label>
				
				<label for="modificationDate" class="col-md-3 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Modification date:</label>
				<c:choose>
					<c:when test="${not empty test.modificationDate}">
						<label class="col-sm-9 control-label" id="modificationDate" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${test.modificationDate}</label>
					</c:when>
					<c:otherwise>
						<label class="col-sm-9 control-label" id="modificationDate" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">-</label>
					</c:otherwise>
				</c:choose>
				
				<label for="modifiedBy" class="col-md-3 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Modified by:</label>
				<c:choose>
					<c:when test="${not empty test.modifiedBy}">
						<label class="col-sm-9 control-label" id="modifiedBy" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${test.modifiedBy}</label>
					</c:when>
					<c:otherwise>
						<label class="col-sm-9 control-label" id="modifiedBy" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">-</label>	
					</c:otherwise>
				</c:choose>
			</div>
        </div>

        <c:forEach var="group" items="${groups}">
	        <div class="col-sm-12 well" id="group_${group.groupId}" style="margin-bottom: 20px;">
	       		<div class="col-sm-12">
	        		<label for="groupName" class="col-md-2 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Group name:</label>	<!-- ${groupCounter}. Group name: -->
					<label id="groupName" class="col-md-5 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${group.groupName}</label> 
					<div class="col-md-5" style="text-align: right;">
						<button type="button" class="btn btn-primary btn-sm" id="collapseGroup" name="collapseGroup" data-reference="${group.groupId}" data-toggle="collapse" href="#collapseGroup_${group.groupId}" aria-expanded="false" aria-controls="collapseGroup_${group.groupId}" style="margin-right: 5px;"><span class="glyphicon glyphicon-triangle-bottom" aria-hidden="true"></span> Show questions</button>
						<button type="button" class="btn btn-success btn-sm" id="addQuestionBtn" name="addQuestionBtn" data-toggle="modal" data-target="#addQuestionModal" data-group-reference="${group.groupId}" style="margin-right: 5px;"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add question</button>
						<button type="button" class="btn btn-info btn-sm" id="editGroupBtn" name="editGroupBtn" data-toggle="modal" data-target="#editGroupModal" data-test-reference="${test.testId}" data-group-name="${group.groupName}" data-group-reference="${group.groupId}" style="margin-right: 5px;"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit</button>
						<button type="button" class="btn btn-danger btn-sm" id="deleteGroupBtn" name="deleteGroupBtn" data-toggle="modal" data-target="#confirmDeleteGroup" data-title="Delete Group" data-message="Are you sure you want to delete group '${group.groupName}'?" data-group-reference="${group.groupId}" data-test-reference="${test.testId}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete</button>
					</div>
				</div>
	      
	        	<div class="col-sm-12">
					<label for="numberOfQuestions" class="col-md-2 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Num. of questions:</label>
					<label id="numberOfQuestions" class="col-md-1 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${group.numberOfQuestions}</label>
					<div class="col-sm-12" style="text-align: right;">
						<button type="button" class="btn btn-warning btn-sm" id="generatePasswordsBtn" name="generatePasswordsBtn" data-reference="${group.groupId}" data-target="#generatePasswordsModal" data-toggle="modal" aria-expanded="false" style="margin-right: 5px"><span class="glyphicon glyphicon-random" aria-hidden="true"></span>  Generate Passwords</button>
					</div>
				</div>
	      
		        <div class="collapse" id="collapseGroup_${group.groupId}" data-reference="${group.groupId}">
				</div>
			</div>
        </c:forEach>


    </div>
    
</div>

<div class="modal fade" id="editTestModal" tabindex="-1" role="dialog" aria-labelledby="editTestLabelModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="editTestTitleModal">Edit test</h4>
			</div>
			<div class="modal-body">
				<form action="<c:url value="/tests/doEditTest"/>" method="POST" id="editTestFormModal" >
					<div class="form-group">
		      			<label for="testNameModal" class="control-label">Test name:</label>
		      			<input type="text" class="form-control" id="testNameModal" name="testNameModal" placeholder="Test name" maxlength="200" aria-describedby="inputEditTestNameError">
		      			<span id="glyphiconErrorEditTestName" class="form-control-feedback" aria-hidden="true"></span> 
  						<span id="inputEditTestNameError" class="sr-only">(error)</span>
					</div>
					<div class="form-group">
		      			<label for="timeForTestModal" class="control-label">Time for test (min):</label>
		      			<input type="number" class="form-control" id="timeForTestModal" name="timeForTestModal" placeholder="Time for test" aria-describedby="inputEditTimeForTestError" min="5">
		      			<span id="glyphiconErrorEditTimeForTest" class="form-control-feedback" aria-hidden="true"></span> 
  						<span id="inputEditTimeForTestError" class="sr-only">(error)</span>
					</div>
					<div class="form-group">
		      			<label for="mark2Modal" class="control-label">Mark 2:</label>
		      			<input type="number" class="form-control" id="mark2Modal" name="mark2Modal" placeholder="Mark 2" aria-describedby="inputEditMark2Error" min="0" max="100">
		      			<span id="glyphiconErrorEditMark2" class="form-control-feedback" aria-hidden="true"></span> 
  						<span id="inputEditMark2Error" class="sr-only">(error)</span>
					</div>
					<div class="form-group">
		      			<label for="mark3Modal" class="control-label input-modal-error">Mark 3:</label>
		      			<input type="number" class="form-control" id="mark3Modal" name="mark3Modal" placeholder="Mark 3" aria-describedby="inputEditMark3Error" readonly="readonly" max="100">
		      			<span id="glyphiconErrorEditMark3" class="form-control-feedback" aria-hidden="true"></span> 
  						<span id="inputEditMark3Error" class="sr-only">(error)</span>
					</div>
					<div class="form-group">
		      			<label for="mark3_5Modal" class="control-label input-modal-error">Mark 3+:</label>
		      			<input type="number" class="form-control" id="mark3_5Modal" name="mark3_5Modal" placeholder="Mark 3+" aria-describedby="inputEditMark3_5Error" readonly="readonly" max="100">
		      			<span id="glyphiconErrorEditMark3_5" class="form-control-feedback" aria-hidden="true"></span> 
  						<span id="inputEditMark3_5Error" class="sr-only">(error)</span>
					</div>
					<div class="form-group">
		      			<label for="mark4Modal" class="control-label">Mark 4:</label>
		      			<input type="number" class="form-control" id="mark4Modal" name="mark4Modal" placeholder="Mark 4" aria-describedby="inputEditMark4Error" readonly="readonly" max="100">
		      			<span id="glyphiconErrorEditMark4" class="form-control-feedback" aria-hidden="true"></span> 
  						<span id="inputEditMark4Error" class="sr-only">(error)</span>
					</div>
					<div class="form-group">
		      			<label for="mark4_5Modal" class="control-label input-modal-error">Mark 4+:</label>
		      			<input type="number" class="form-control" id="mark4_5Modal" name="mark4_5Modal" placeholder="Mark 4+" aria-describedby="inputEditMark4_5Error" readonly="readonly" max="100">
		      			<span id="glyphiconErrorEditMark4_5" class="form-control-feedback" aria-hidden="true"></span> 
  						<span id="inputEditMark4_5Error" class="sr-only">(error)</span>
					</div>
					<div class="form-group">
		      			<label for="mark5Modal" class="control-label">Mark 5:</label>
		      			<input type="number" class="form-control" id="mark5Modal" name="mark5Modal" placeholder="Mark 5" aria-describedby="inputEditMark5Error" readonly="readonly" max="100"> 
		      			<span id="glyphiconErrorEditMark5" class="form-control-feedback" aria-hidden="true"></span> 
  						<span id="inputEditMark5Error" class="sr-only">(error)</span>
					</div>
					
					<input type="hidden" name="subjectReference" id="subjectReference" />
					<input type="hidden" name="testReference" id="testReference" />
					<input type="hidden" name="setReference" id="setReference" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="editTestBtnModal">Edit test</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="addGroupModal" tabindex="-1" role="dialog" aria-labelledby="addGroupLabelModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="addGroupTitleModal">Add new group</h4>
			</div>
			<div class="modal-body">
				<form action="<c:url value="/tests/doAddGroup"/>" method="POST" id="addGroupFormModal">
					<div class="form-group">		
		      			<label for="groupNameModal" class="control-label">Group name:</label>
		      			<input type="text" class="form-control" id="groupNameModal" name="groupNameModal" placeholder="Group name" maxlength="200" aria-describedby="inputAddGroupNameError">
		      		 	<span id="glyphiconErrorAddGroupName" class="form-control-feedback" aria-hidden="true"></span> 
  						<span id="inputAddGroupNameError" class="sr-only">(error)</span>
					</div>
					<input type="hidden" name="testReference" id="testReference" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="addGroupBtnModal">Add group</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="editGroupModal" tabindex="-1" role="dialog" aria-labelledby="editGroupLabelModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="editGroupTitleModal">Edit group</h4>
			</div>
			<div class="modal-body">
				<form action="<c:url value="/tests/doEditGroup"/>" method="POST" id="editGroupFormModal">
					<div class="form-group">		
		      			<label for="groupNameModal" class="control-label">Group name:</label>
		      			<input type="text" class="form-control" id="groupNameModal" name="groupNameModal" placeholder="Group name" maxlength="200" aria-describedby="inputEditGroupNameError">
		      		 	<span id="glyphiconErrorAddGroupName" class="form-control-feedback" aria-hidden="true"></span> 
  						<span id="inputEditGroupNameError" class="sr-only">(error)</span>
					</div>
					<input type="hidden" name="testReference" id="testReference" />
					<input type="hidden" name="groupReference" id="groupReference" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="editGroupBtnModal">Edit group</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="confirmDeleteGroup" tabindex="-1" role="dialog" aria-labelledby="confirmDeleteGroupLabelModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="confirmDeleteGroupTitleModal">Delete group</h4>
			</div>
			<div class="modal-body">
				<form action="<c:url value="/tests/doDeleteGroupAjax"/>" method="POST" id="deleteGroupFormModal" >
					<div class="form-group" id="confirmDeleteGroupLabel">
					</div>
					<input type="hidden" name="testReference" id="testReference" />
					<input type="hidden" name="groupReference" id="groupReference" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-danger" id="confirmDeleteGroupBtnModal">Delete</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="confirmEnableTest" tabindex="-1" role="dialog" aria-labelledby="confirmEnableTestLabelModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="confirmEnableTestTitleModal">Enable test</h4>
            </div>
            <div class="modal-body">
                <form action="<c:url value="/tests/doEnableTestAjax"/>" method="POST" id="enableTestFormModal" >
                    <div class="form-group" id="confirmEnableTestLabel">
                    </div>
                    <input type="hidden" name="testReference" id="testReference" />
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-danger" id="confirmEnableTestBtnModal">Enable</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="confirmDisableTest" tabindex="-1" role="dialog" aria-labelledby="confirmDisableTestLabelModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="confirmDisableTestTitleModal">Disable test</h4>
            </div>
            <div class="modal-body">
                <form action="<c:url value="/tests/doDisableTestAjax"/>" method="POST" id="disableTestFormModal" >
                    <div class="form-group" id="confirmDisableTestLabel">
                    </div>
                    <input type="hidden" name="testReference" id="testReference" />
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-danger" id="confirmDisableTestBtnModal">Disable</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="generatePasswordsModal" tabindex="-1" role="dialog" aria-labelledby="generatePasswordsLabelModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="generatePasswordsTitleModal">Generate passwords</h4>
            </div>
            <div class="modal-body">
                <form action="<c:url value="/tests/generatedPasswords"/>" method="POST" id="generatePasswordsFormModal" >
                    <div class="col-sm-offset-3 col-sm-6" id="studentGroupSelect" align="center" style="padding-bottom: 10px">
                        <select class="form-control" id="studentGroupsDropDown" name="studentGroupsDropDown">
                            <option value="" selected="selected">Select group</option>

                            <c:forEach var="studentgroup" items="${allStudentGroupsModel}">
                                <option value="${studentgroup.studentgroupId}">${studentgroup.studentgroupName}</option>
                            </c:forEach>
                        </select>
                    </div>

					<input type="hidden" name="studentGroupReference" id="studentGroupReference"/>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />


				</form>
            </div>
            <div class="modal-footer">
				<button type="button" class="btn btn-danger" id="generatePasswordsBtnModal">Generate Passwords</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="addQuestionModal" tabindex="-1" role="dialog" aria-labelledby="addQuestionLabelModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="addQuestionTitleModal">Add new question</h4>
			</div>
			<div class="modal-body">
				<form action="<c:url value="/tests/doAddQuestion"/>" method="POST" id="addQuestionFormModal" enctype="multipart/form-data">
					<div class="form-group">
						<label for="contentOfQuestionModal" class="control-label">Content of question:</label>
						<textarea class="form-control resizable" name="contentOfQuestionModal" id="contentOfQuestionModal" placeholder="Content of question" maxlength="2000" aria-describedby="inputAddContentOfQuestionError"></textarea>
						<span id="glyphiconErrorAddContentOfQuestion" class="form-control-feedback" aria-hidden="true"></span> 
  						<span id="inputAddContentOfQuestionError" class="sr-only">(error)</span>
					</div>
					<div class="form-group">
						<label for="inputImageForQuestionModal" class="control-label">Picture (optional):</label>
						<input type="file" id="inputImageForQuestionModal" name="inputImageForQuestionModal" accept=".jpg,.jpeg,.png,.gif,.bmp"/>
						<img id="imageForQuestionModal" src="" alt="Input image for question" width="50%" height="50%" hidden="true"/>
					</div>
					<input type="hidden" name="testReference" id="testReference" />
					<input type="hidden" name="groupReference" id="groupReference" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="addQuestionBtnModal">Add question</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="editQuestionModal" tabindex="-1" role="dialog" aria-labelledby="editQuestionLabelModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="editQuestionTitleModal">Edit question</h4>
			</div>
			<div class="modal-body">
				<form action="<c:url value="/tests/doEditQuestion"/>" method="POST" id="editQuestionFormModal" enctype="multipart/form-data">
					<div class="form-group">
						<label for="contentOfQuestionModal" class="control-label">Content of question:</label>
						<textarea class="form-control resizable" name="contentOfQuestionModal" id="contentOfQuestionModal" placeholder="Content of question" maxlength="2000" aria-describedby="inputEditContentOfQuestionError"></textarea>
						<span id="glyphiconErrorEditContentOfQuestion" class="form-control-feedback" aria-hidden="true"></span> 
  						<span id="inputEditContentOfQuestionError" class="sr-only">(error)</span>
					</div>
					<div class="form-group">
						<label for="inputImageForQuestionModal" class="control-label">Picture (optional):</label>
						<input type="file" id="inputImageForQuestionModal" name="inputImageForQuestionModal" accept=".jpg,.jpeg,.png,.gif,.bmp"/>
						<img id="imageForQuestionModal" src="" alt="Input image for question" width="50%" height="50%" hidden="true"/>
					</div>
					<input type="hidden" name="testReference" id="testReference" />
					<input type="hidden" name="groupReference" id="groupReference" />
					<input type="hidden" name="questionReference" id="questionReference" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="editQuestionBtnModal">Edit question</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="confirmDeleteQuestion" tabindex="-1" role="dialog" aria-labelledby="confirmDeleteQuestionLabelModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="confirmDeleteQuestionTitleModal">Delete question</h4>
			</div>
			<div class="modal-body">
				<form action="<c:url value="/tests/doDeleteQuestion"/>" method="POST" id="deleteQuestionFormModal" >
					<div class="form-group" id="confirmDeleteQuestionLabel">
					</div>
					<input type="hidden" name="testReference" id="testReference" />
					<input type="hidden" name="groupReference" id="groupReference" />
					<input type="hidden" name="questionReference" id="questionReference" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-danger" id="confirmDeleteQuestionBtnModal">Delete</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="addAnswerModal" tabindex="-1" role="dialog" aria-labelledby="addAnswerLabelModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="addAnswerTitleModal">Add new answer</h4>
			</div>
			<div class="modal-body">
				<form action="<c:url value="/tests/doAddAnswer"/>" method="POST" id="addAnswerFormModal" enctype="multipart/form-data">
					<div class="form-group">
						<label for="contentOfAnswerModal" class="control-label">Content of answer:</label>
						<textarea class="form-control resizable" name="contentOfAnswerModal" id="contentOfAnswerModal" placeholder="Content of answer" maxlength="2000" aria-describedby="inputAddContentOfAnswerError"></textarea>
						<span id="glyphiconErrorAddContentOfAnswer" class="form-control-feedback" aria-hidden="true"></span> 
  						<span id="inputAddContentOfAnswerError" class="sr-only">(error)</span>
					</div>
					<div class="form-group">
						<label class="radio-inline">
							<input type="radio" id="whetherCorrect" name="whetherCorrect" value="true"> true
						</label>
						<label class="radio-inline">
							<input type="radio" id="whetherCorrect" name="whetherCorrect" value="false"> false
						</label>
					</div>
					<div class="form-group">
						<label for="inputImageForAnswerModal" class="control-label">Picture (optional):</label>
						<input type="file" id="inputImageForAnswerModal" name="inputImageForAnswerModal" accept=".jpg,.jpeg,.png,.gif,.bmp"/>
						<img id="imageForAnswerModal" src="" alt="Input image for answer" width="50%" height="50%" hidden="true"/>
					</div>
					<input type="hidden" name="groupReference" id="groupReference" />
					<input type="hidden" name="questionReference" id="questionReference" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="addAnswerBtnModal">Add answer</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="editAnswerModal" tabindex="-1" role="dialog" aria-labelledby="editAnswerLabelModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="editAnswerTitleModal">Edit answer</h4>
			</div>
			<div class="modal-body">
				<form action="<c:url value="/tests/doEditAnswer"/>" method="POST" id="editAnswerFormModal" enctype="multipart/form-data">
					<div class="form-group">
						<label for="contentOfAnswerModal" class="control-label">Content of answer:</label>
						<textarea class="form-control resizable" name="contentOfAnswerModal" id="contentOfAnswerModal" placeholder="Content of answer" maxlength="2000" aria-describedby="inputEditContentOfAnswerError"></textarea>
						<span id="glyphiconErrorEditContentOfAnswer" class="form-control-feedback" aria-hidden="true"></span> 
  						<span id="inputEditContentOfAnswerError" class="sr-only">(error)</span>
					</div>
					<div class="form-group">
						<label class="radio-inline">
							<input type="radio" id="whetherCorrect" name="whetherCorrect" value="true"> true
						</label>
						<label class="radio-inline">
							<input type="radio" id="whetherCorrect" name="whetherCorrect" value="false"> false
						</label>
					</div>
					<div class="form-group">
						<label for="inputImageForAnswerModal" class="control-label">Picture (optional):</label>
						<input type="file" id="inputImageForAnswerModal" name="inputImageForAnswerModal" accept=".jpg,.jpeg,.png,.gif,.bmp"/>
						<img id="imageForAnswerModal" src="" alt="Input image for answer" width="50%" height="50%" hidden="true"/>
					</div>
					<input type="hidden" name="groupReference" id="groupReference" />
					<input type="hidden" name="questionReference" id="questionReference" />
					<input type="hidden" name="answerReference" id="answerReference" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="editAnswerBtnModal">Edit answer</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="confirmDeleteAnswer" tabindex="-1" role="dialog" aria-labelledby="confirmDeleteAnswerLabelModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="confirmDeleteAnswerTitleModal">Delete answer</h4>
			</div>
			<div class="modal-body">
				<form action="<c:url value="/tests/doDeleteAnswer"/>" method="POST" id="deleteAnswerFormModal" >
					<div class="form-group" id="confirmDeleteAnswerLabel">
					</div>
					<input type="hidden" name="groupReference" id="groupReference" />
					<input type="hidden" name="questionReference" id="questionReference" />
					<input type="hidden" name="answerReference" id="answerReference" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-danger" id="confirmDeleteAnswerBtnModal">Delete</button>
			</div>
		</div>
	</div>
</div>

<div id="hiddenDiv" style="display: none; width: 300px; height: 300px; position: absolute; text-align: center; vertical-align: middle; line-height: 300px;">
	<img id="hiddenImg" style="width: 100px; height: 100px;">
</div>

<script type="text/javascript">

	$("#studentGroupsDropDown").change(function(){

		console.log("csrfParameter = " + csrfParameter);
		console.log("csrfToken = " + csrfToken);

		var data = {};
		data[csrfParameter] = csrfToken;
		data["studentgroupId"] = $(this).val();

		if($(this).val() != "") {

			$('#generatePasswordsBtnModal').show();
			$('#generatePasswordsFormModal').find('#studentGroupReference').val($(this).val());


		} else {
			$('#generatePasswordsBtnModal').hide();
			$('#generatePasswordsFormModal').find('#studentGroupReference').val("");
		}
	});
    /*****************************************************************/
    /*** Adding validation and handling events for "deleteStudent" ***/
    /*****************************************************************/




    $('#generatePasswordsModal').on('show.bs.modal', function (event) {
        console.log("$('#generatePasswordsModal').on('show.bs.modal')");

        var button = $(event.relatedTarget);
        var studentgroupId = button.data('studentgroup-reference');
        var message = button.data('message');

        $(this).find('.modal-body #studentGroupReference').val(studentgroupId);
        $(this).find('.modal-body #generatePasswordsLabel').text(message);
    });

    $('#generatePasswordsModal .modal-footer #generatePasswordsBtnModal').on('click', function(){
        console.log("$('#generatePasswordsModal .modal-footer #generatePasswordsBtnModal').on('click')");

        $('#generatePasswordsFormModal').submit();
    });


	/***************************************************************************************/
	/************** Adding validation and handling events for "addGroupModal" **************/
	/***************************************************************************************/
	
	jQuery.validator.addMethod("checkGroupName", function(value, element) {
		var isSuccess = false;
		console.log("isSuccess value before ajax call = " + isSuccess.toString());

		var data = {};
		data[csrfParameter] = csrfToken;
		data["groupName"] = value;

		if($('#editGroupModal').find('.modal-body #groupReference').val() != "") {
			data["groupId"] = $('#editGroupModal').find('.modal-body #groupReference').val();
		}

		if($('#editGroupModal').find('.modal-body #testReference').val() != "") {
			data["testId"] = $('#editGroupModal').find('.modal-body #testReference').val();
		} else {
			data["testId"] = $('#addGroupModal').find('.modal-body #testReference').val();
		}

		console.log("groupName value = " + value);
	    
	    $.ajax({
			url: URLWithContextPath + "/tests/checkGroupName",
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
	}, "This group already exists! Please enter another group name.");
	
	var validatorAddGroup = $("#addGroupFormModal").validate({
		rules: {
			groupNameModal: {
				required: true,
				maxlength: 200,
				checkGroupName: true
			}
		},
		messages: {
			groupNameModal: {
				required: "Group name text field is required.",
				maxlength: "Given group name is too long, please change it."
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
			
			/*var data = {};
			data[csrfParameter] = csrfToken;
			
			var groupNameModal = $('#addGroupModal .modal-body #groupNameModal').val();
			var testReference = $('#addGroupModal .modal-body #testReference').val();
			console.log("$('#addGroupModal .modal-body #groupNameModal').val() == " + groupNameModal);
			console.log("$('#addGroupModal .modal-body #testReference').val() == " + groupNameModal);
			data["groupNameModal"] = groupNameModal;
			data["testReference"] = testReference;*/

			// The .serialize() method creates a text string in standard URL-encoded notation
			$.ajax({
				url: URLWithContextPath + "/tests/doAddGroupAjax",
				data: $(form).serialize(),
				type: "POST",
				success: function(data) {
					console.log("Status: " + data.status);
					console.log("Result: " + data.result);

					$("#testDiv").append(data.result.groupDiv);
					$("#numberOfGroups").text(data.result.numberOfGroupsForTest);
					$("#modificationDate").text(data.result.modificationDate);
					$("#modifiedBy").text(data.result.modifiedBy);
				}
			});

			$("#addGroupModal").modal('hide');
		}
	});

	$('#addGroupBtn').on('click', function(event) {
		console.log("$('#addGroupBtn').on('click')");
	});
	
	$('#addGroupModal').on('show.bs.modal', function(event) {
		console.log("$('#addGroupModal').on('show.bs.modal')");

		var button = $(event.relatedTarget);
		var testId = button.data('test-reference');
		
		$(this).find('.modal-body #testReference').val(testId);
	});

	$('#addGroupModal').on('hide.bs.modal', function(event) {
		console.log("$('#addGroupModal').on('hide.bs.modal')");

		$('#addGroupModal .modal-body #groupNameModal').val("");
		$(this).find('.modal-body #testReference').val("");
		
		validatorAddGroup.resetForm();
	});

	$('#addGroupModal .modal-footer #addGroupBtnModal').on('click', function(event) {
		console.log("$('#addGroupModal .modal-footer #addGroupBtnModal').on('click')");

		$('#addGroupFormModal').submit();
	});

	/***************************************************************************************/
	/************** Adding validation and handling events for "editTestModal" **************/
	/***************************************************************************************/
	
	jQuery.validator.addMethod("checkTestName", function(value, element) {
		var isSuccess = false;
		console.log("isSuccess value before ajax call = " + isSuccess.toString());

		var data = {};
		data[csrfParameter] = csrfToken;
		data["testName"] = value;
		data["subjectId"] = $('#editTestModal').find('.modal-body #subjectReference').val();

		if($('#editTestModal').find('.modal-body #testReference').val() != "") {
			data["testId"] = $('#editTestModal').find('.modal-body #testReference').val();
		}
		
		console.log("testName value = " + value);
	    
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
	
	var validatorEditTest = $('#editTestFormModal').validate({
		rules: {
			testNameModal: {
				required: true,
				maxlength: 200,
				checkTestName: true
			},
			timeForTestModal: {
				required: true,
			},
			mark2Modal: {
				required: true,
			},
			mark3Modal: {
				required: true,
			},
			mark3_5Modal: {
				required: true,
			},
			mark4Modal: {
				required: true,
			},
			mark4_5Modal: {
				required: true,
			},
			mark5Modal: {
				required: true,
			}
		},
		messages: {
			testNameModal: {
				required: "Test name text field is required.", 
				maxlength: "Given test name is to long, please change it."
			},
			timeForTestModal: {
				required: "Time for test number field is required."
			},
			mark2Modal: {
				required: "Mark 2 number field is required."
			},
			mark3Modal: {
				required: "Mark 3 number field is required."
			},
			mark3_5Modal: {
				required: "Mark 3+ number field is required."
			},
			mark4Modal: {
				required: "Mark 4 number field is required."
			},
			mark4_5Modal: {
				required: "Mark 4+ number field is required."
			},
			mark5Modal: {
				required: "Mark 5 number field is required."
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

			$.ajax({
				url: URLWithContextPath + "/tests/doEditTestAjax",
				data: $(form).serialize(),
				type: "POST",
				success: function(data) {
					console.log("***** AJAX CALL *****");
					console.log("Status: " + data.status);
					console.log("Result: " + data.result);

					var map = data.result;		// AJAX response contains map with update parameters for test
					$("#testName").text(map['testName']);
					$("#timeForTest").text(map['timeForTest']);
					$("#modificationDate").text(map['modificationDate']);
					$("#modifiedBy").text(map['modifiedBy']);

					var btn = $("#editTestBtn");
					btn.attr("data-test-name", map['testName']);
					btn.attr("data-time-for-test", map['timeForTest']);
					btn.attr("data-mark2", map['mark2']);
					btn.attr("data-mark3", map['mark3']);
					btn.attr("data-mark3_5", map['mark3_5']);
					btn.attr("data-mark4", map['mark4']);
					btn.attr("data-mark4_5", map['mark4_5']);
					btn.attr("data-mark5", map['mark5']);
				}
			});

			$("#editTestModal").modal('hide');
		}
	});
	
	$('button[name="editTestBtn"]').on('click', function(event) {
		console.log("$('button[name=\"editTestBtn\"]').on('click')");
	});
	
	$('#editTestModal').on('show.bs.modal', function(event) {
		console.log("$('#editTestModal').on('show.bs.modal')");

		var button = $(event.relatedTarget);
		var testName = button.attr('data-test-name');
		var timeForTest = button.attr('data-time-for-test');
		var mark2 = button.attr('data-mark2');
		var mark3 = button.attr('data-mark3');
		var mark3_5 = button.attr('data-mark3_5')
		var mark4 = button.attr('data-mark4');
		var mark4_5 = button.attr('data-mark4_5');
		var mark5 = button.attr('data-mark5');
		var subjectId = button.attr('data-subject-reference');
		var testId = button.attr('data-test-reference');
		var setId = button.attr('data-set-reference');

		$(this).find('.modal-body #testNameModal').val(testName);
		$(this).find('.modal-body #timeForTestModal').val(timeForTest);
		$(this).find('.modal-body #mark2Modal').val(mark2);
		$(this).find('.modal-body #mark3Modal').val(mark3);
		$(this).find('.modal-body #mark3_5Modal').val(mark3_5);
		$(this).find('.modal-body #mark4Modal').val(mark4);
		$(this).find('.modal-body #mark4_5Modal').val(mark4_5);
		$(this).find('.modal-body #mark5Modal').val(mark5);
		$(this).find('.modal-body #subjectReference').val(subjectId);
		$(this).find('.modal-body #testReference').val(testId);
		$(this).find('.modal-body #setReference').val(setId);

		$("#editTestModal .modal-body #mark3Modal").prop('readonly', true);
		$("#editTestModal .modal-body #mark3_5Modal").prop('readonly', true);
		$("#editTestModal .modal-body #mark4Modal").prop('readonly', true);
		$("#editTestModal .modal-body #mark4_5Modal").prop('readonly', true);
		$("#editTestModal .modal-body #mark5Modal").prop('readonly', true);
	});

	$('#editTestModal').on('hide.bs.modal', function(event) {
		console.log("$('#editTestModal').on('hide.bs.modal')");

		validatorEditTest.resetForm();
	});

	$('#editTestModal .modal-footer #editTestBtnModal').on('click', function(event) {
		console.log("$('#editTestModal .modal-footer #editTestBtnModal').on('click')");

		$('#editTestFormModal').submit();
	});

	$("#editTestModal .modal-body #mark2Modal").on('change keyup', function () {
		console.log("$(\"#editTestModal .modal-body #mark2Modal\").change()");

		if($(this).val().length != 0) {
			$("#editTestModal .modal-body #mark3Modal").prop('readonly', false);
			if(parseInt($(this).val()) != 100) {
				$("#editTestModal .modal-body #mark3Modal").attr('min', parseInt($(this).val())+1);
			} else {
				$("#editTestModal .modal-body #mark3Modal").attr('min', 100);
			}
		} else {
			$("#editTestModal .modal-body #mark3Modal").prop('readonly', true);
		}
	});

	$("#editTestModal .modal-body #mark3Modal").on('change keyup', function () {
		console.log("$(\"#editTestModal .modal-body #mark3Modal\").change()");

		if($(this).val().length != 0) {
			$("#editTestModal .modal-body #mark3_5Modal").prop('readonly', false);
			if(parseInt($(this).val()) != 100) {
				$("#editTestModal .modal-body #mark3_5Modal").attr('min', parseInt($(this).val())+1);
			} else {
				$("#editTestModal .modal-body #mark3_5Modal").attr('min', 100);
			}
		} else {
			$("#editTestModal .modal-body #mark3_5Modal").prop('readonly', true);
		}
	});

	$("#editTestModal .modal-body #mark3_5Modal").on('change keyup', function () {
		console.log("$(\"#editTestModal .modal-body #mark3Modal\").change()");

		if($(this).val().length != 0) {
			$("#editTestModal .modal-body #mark4Modal").prop('readonly', false);
			if(parseInt($(this).val()) != 100) {
				$("#editTestModal .modal-body #mark4Modal").attr('min', parseInt($(this).val())+1);
			} else {
				$("#editTestModal .modal-body #mark4Modal").attr('min', 100);
			}
		} else {
			$("#editTestModal .modal-body #mark4Modal").prop('readonly', true);
		}
	});

	$("#editTestModal .modal-body #mark4Modal").on('change keyup', function () {
		console.log("$(\"#editTestModal .modal-body #mark4Modal\").change()");

		if($(this).val().length != 0) {
			$("#editTestModal .modal-body #mark4_5Modal").prop('readonly', false);
			if(parseInt($(this).val()) != 100) {
				$("#editTestModal .modal-body #mark4_5Modal").attr('min', parseInt($(this).val())+1);
			} else {
				$("#editTestModal .modal-body #mark4_5Modal").attr('min', 100);
			}
		} else {
			$("#editTestModal .modal-body #mark4_5Modal").prop('readonly', true);
		}
	});

	$("#editTestModal .modal-body #mark4_5Modal").on('change keyup', function () {
		console.log("$(\"#editTestModal .modal-body #mark4Modal\").change()");

		if($(this).val().length != 0) {
			$("#editTestModal .modal-body #mark5Modal").prop('readonly', false);
			if(parseInt($(this).val()) != 100) {
				$("#editTestModal .modal-body #mark5Modal").attr('min', parseInt($(this).val())+1);
			} else {
				$("#editTestModal .modal-body #mark5Modal").attr('min', 100);
			}
		} else {
			$("#editTestModal .modal-body #mark5Modal").prop('readonly', true);
		}
	});
	
	/***************************************************************************************/
	/************* Adding validation and handling events for "addQuestionModal" ************/
	/***************************************************************************************/
	
	jQuery.validator.addMethod("checkContentOfQuestion", function(value, element) {
		var isSuccess = false;
		console.log("isSuccess value before ajax call = " + isSuccess.toString());

		var data = {};
		data[csrfParameter] = csrfToken;
		data["testId"] = $('button[name="editTestBtn"]').data('test-reference');
		data["contentOfQuestion"] = value;

		if($('#editQuestionModal').find('.modal-body #questionReference').val() != "") {
			data["questionId"] = $('#editQuestionModal').find('.modal-body #questionReference').val();
		}
		
		console.log("contentOfQuestion value = " + value);
	    
	    $.ajax({
			url: URLWithContextPath + "/tests/checkContentOfQuestion",
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
	}, "This question already exists! Please enter another question.");

	/*jQuery.validator.addMethod("checkQuestionPictureName", function(value, element) {
		var isSuccess = false;
		
		var inputImageForQuestion = null;
		console.log("isSuccess value before ajax call = " + isSuccess.toString());

		if($('#addQuestionModal .modal-body #inputImageForQuestionModal').val() != "") {
			inputImageForQuestion = $('#addQuestionModal .modal-body #inputImageForQuestionModal');
		} else {
			inputImageForQuestion = $('#editQuestionModal .modal-body #inputImageForQuestionModal');
		}
		
		if(inputImageForQuestion.val() != "") {			
			var data = {};
			data[csrfParameter] = csrfToken;
			data["pictureName"] = inputImageForQuestion[0].files[0].name;

			if($('#editQuestionModal').find('.modal-body #questionReference').val() != "") {
				data["questionId"] = $('#editQuestionModal').find('.modal-body #questionReference').val();
			}
			
			console.log("pictureName value = " + value);
		    
		    $.ajax({
				url: URLWithContextPath + "/tests/checkPictureName",
				type: "POST",
				async: false,
				data: data,
				success: function(data) {
					console.log("Data in response: " + data);
					isSuccess = data === "SUCCESS" ? true : false;
					console.log("isSuccess value after retrieving response = " + isSuccess.toString());
				}
			});
		} else {
			isSuccess = true;
		}
		
	    console.log("isSuccess value before return statement = " + isSuccess.toString());
	    return isSuccess;
	}, "This picture name already exists! Please choose another picture.");*/
	
	var validatorAddQuestion = $("#addQuestionFormModal").validate({
		rules: {
			contentOfQuestionModal: {
				required: true,
				maxlength: 2000,
				checkContentOfQuestion: true,
			}/*,
			inputImageForQuestionModal: {
				checkQuestionPictureName: true
			}*/
		},
		messages: {
			contentOfQuestionModal: {
				required: "Content of question textarea is required.",
				maxlength: "Given content of question is too long, please change it."
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

			/*var fd = new FormData();
			fd.append("contentOfQuestionModal", $('#addQuestionModal .modal-body #contentOfQuestionModal').val());
			console.log("\t\t\t" + $('#addQuestionModal .modal-body #contentOfQuestionModal').val());

			if($('#addQuestionModal .modal-body #inputImageForQuestionModal').val() != ''){	
				fd.append("inputImageForQuestionModal", $('#addQuestionModal .modal-body #inputImageForQuestionModal')[0].files[0]);
				console.log("\t\t\t" + $('#addQuestionModal .modal-body #inputImageForQuestionModal')[0].files[0]);
			}
			
			fd.append("groupReference", $('#addQuestionModal .modal-body #groupReference').val());
			console.log("\t\t\t" + $('#addQuestionModal .modal-body #groupReference').val());
			fd.append("_csrf", $("#addQuestionModal .modal-body input[name='_csrf']").val());
			console.log("\t\t\t" + $("#addQuestionModal .modal-body input[name='_csrf']").val());

			$.ajax({
				url: URLWithContextPath + '/tests/doAddQuestionAjax',
			    data: fd,
			    dataType: 'text',
			    processData: false,
			    contentType: false,
			    cache: false,
			    type: 'POST',
			    success: function(data){
			    	console.log("***** AJAX CALL *****");
			    }
			});*/

			$(form).ajaxSubmit({
				url: URLWithContextPath + "/tests/doAddQuestionAjax",
				dataType: "json",
				success: function(data) { 
					console.log("********* AJAX CALL *********");
					console.log("Status: " + data.status);
					console.log("Result: " + data.result);

					var groupId = $('#addQuestionModal .modal-body #groupReference').val()
					var groupDivName = "#group_" + groupId;
					console.log("groupDiv = " + groupDivName);
					
					var ariaExpanded = $("#testDiv").find(groupDivName).find('#collapseGroup').attr("aria-expanded");
					console.log("ariaExpanded = " + ariaExpanded);

					$("#addQuestionModal").modal('hide');

					// Update group and test 
					$("#testDiv").find(groupDivName).find('#numberOfQuestions').text(data.result.numberOfQuestionsForGroup);
					$("#numberOfQuestions").text(data.result.numberOfQuestionsForTest);
					$("#modificationDate").text(data.result.modificationDate);
					$("#modifiedBy").text(data.result.modifiedBy);
					
					// We have to check if collapse is expanded
					if(ariaExpanded === "true") {
						console.log("collapse is expanded");

						var collapseDivName = "#collapseGroup_" + groupId;
						var collapseDiv = $(collapseDivName);
						console.log("collapseDivName = " + collapseDivName);

						var tableName = "#question_table_" + groupId;
						console.log("tableName = " + tableName);

						if (collapseDiv.find(tableName).length == 1) {
							console.log("Table was foud");

							var table = collapseDiv.find(tableName).DataTable();
							
							var rowNode = table.row.add( [
								data.result.contentOfQuestion,
							    data.result.numberOfAnswers,
							    data.result.numberOfCorrectAnswers,
							    data.result.picture,
							    data.result.addAnswer,
							    data.result.editQuestion,
							    data.result.deleteQuestion,
							    data.result.detailsControl
							] ).draw( false ).node();

							$(rowNode).addClass("success");
							$(rowNode).attr("data-key", data.result.datakey);
							$(rowNode).find('td').eq(7).addClass('details-control');
							
						} else {
							console.log("Table wasn't found");

							var param = {};
							param[csrfParameter] = csrfToken;
							param["groupId"] = groupId;
								
							$.ajax({
								url: URLWithContextPath + "/tests/getQuestionsByGroup",    
								data: param,
								dataType: 'json',
								type: "POST",
								success: function(data){
									console.log(data);
									if(data.status == "FAIL"){
										console.log("Error in retrieving data from response");
										collapseDiv.html(data.result);
									} else if(data.status == "SUCCESS") {
										console.log("Correct response");
										collapseDiv.html(data.result);
									}
								}        
							});
						}
							
					} else {
						console.log("collapse is not expanded");
					}
				}
			});
		}
	});
	
	$('#testDiv').on('click', '#addQuestionBtn', function(e) {
		console.log("$('#testDiv').on('click', '#addQuestionBtn')");
    });
	
	$('#addQuestionModal').on('show.bs.modal', function(event) {
		console.log("$('#addQuestionModal').on('show.bs.modal')");

		var button = $(event.relatedTarget);
		var groupId = button.data('group-reference');
		var testId = $('button[name="editTestBtn"]').data('test-reference');
		
		$(this).find('.modal-body #groupReference').val(groupId);
		$(this).find('.modal-body #testReference').val(testId);
	});

	$('#addQuestionModal').on('hide.bs.modal', function(event) {
		console.log("$('#addQuestionModal').on('hide.bs.modal')");

		$('#addQuestionModal .modal-body #contentOfQuestionModal').css('height', '54px');
		$('#addQuestionModal .modal-body #contentOfQuestionModal').css('width', '568px');
		
		$('#addQuestionModal .modal-body #contentOfQuestionModal').val("");
		$('#addQuestionModal .modal-body #inputImageForQuestionModal').val("");
		$('#addQuestionModal .modal-body #imageForQuestionModal').attr('hidden', true);

		$(this).find('.modal-body #groupReference').val("");
		$(this).find('.modal-body #testReference').val("");

		validatorAddQuestion.resetForm();
	});

	$('#addQuestionModal .modal-footer #addQuestionBtnModal').on('click', function(event) {
		console.log("$('#addQuestionModal .modal-footer #addQuestionBtnModal').on('click')");

		$('#addQuestionFormModal').submit();
	});

	$('#addQuestionModal .modal-body #inputImageForQuestionModal').on('change', function(event) {
		console.log("$('#addQuestionModal .modal-body #inputImageForQuestionModal').on('change')");
		readURL(this, $("#addQuestionModal .modal-body #imageForQuestionModal"));

		console.log("inputImageForQuestionModal value = " + $(this)[0].files[0].name);
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
	
	/***************************************************************************************/
	/************** Adding validation and handling events for "editGroupModal" *************/
	/***************************************************************************************/
	
	var validatorEditGroup = $('#editGroupFormModal').validate({
		rules: {
			groupNameModal: {
				required: true,
				maxlength: 200,
				checkGroupName: true
			}
		},
		messages: {
			groupNameModal: {
				required: "Group name text field is required.", 
				maxlength: "Given group name is to long, please change it."
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

			$.ajax({
				url: URLWithContextPath + "/tests/doEditGroupAjax",
				data: $(form).serialize(),
				type: "POST",
				success: function(data) {
					console.log("***** AJAX CALL *****");
					console.log("Status: " + data.status);
					console.log("Result: " + data.result);

					var groupId = $("#editGroupModal .modal-body #groupReference").val();
					console.log("groupId = " + groupId);
					var map = data.result;

					$("#editGroupModal").modal('hide');

					var groupDiv = $("#group_" + groupId);
					console.log("groupDiv.attr(\"id\") = " + groupDiv.attr("id"));
					console.log("groupName = " + map['groupName']);
					console.log("numberOfQuestions = " + map['numberOfQuestions']);
					groupDiv.find("#groupName").text(map['groupName']);
					groupDiv.find("#numberOfQuestions").text(map['numberOfQuestions']);

					var editGroupBtn = groupDiv.find("#editGroupBtn");
					editGroupBtn.attr("data-group-name", map['groupName']);
					editGroupBtn.attr("data-group-reference", map['groupReference']);

					$("#modificationDate").text(data.result.modificationDate);
					$("#modifiedBy").text(data.result.modifiedBy);
				}
			});
		}
	});
	
	$('#testDiv').on('click', '#editGroupBtn', function(e) {
		console.log("$('#testDiv').on('click', '#editGroupBtn')");
    });

	$('#editGroupModal').on('show.bs.modal', function(event) {
		console.log("$('#editGroupModal').on('show.bs.modal')");

		var button = $(event.relatedTarget);
		var groupName = button.attr('data-group-name');
		var testId = button.attr('data-test-reference');
		var groupId = button.attr('data-group-reference');

		$(this).find('.modal-body #groupNameModal').val(groupName);
		$(this).find('.modal-body #testReference').val(testId);
		$(this).find('.modal-body #groupReference').val(groupId);
	});

	$('#editGroupModal').on('hide.bs.modal', function(event) {
		console.log("$('#editGroupModal').on('hide.bs.modal')");

		$(this).find('.modal-body #testReference').val("");
		$(this).find('.modal-body #groupReference').val("");
		validatorEditGroup.resetForm();
	});

	$('#editGroupModal .modal-footer #editGroupBtnModal').on('click', function(event) {
		console.log("$('#editGroupModal .modal-footer #editGroupBtnModal').on('click')");

		$('#editGroupFormModal').submit();
	});
	
	/***************************************************************************************/
	/************ Adding validation and handling events for "confirmDeleteGroup" ***********/
	/***************************************************************************************/
	
	var validatorDeleteGroup = $('#deleteGroupFormModal').validate({
		submitHandler: function(form) {
			console.log("***** submitHandler *****");

			$.ajax({
				url: URLWithContextPath + "/tests/doDeleteGroupAjax",
				data: $(form).serialize(),
				type: "POST",
				success: function(data) {
					console.log("***** AJAX CALL *****");
					console.log("Status: " + data.status);
					console.log("Result: " + data.result);

					$("#confirmDeleteGroup").modal('hide');

					console.log("data.result.groupDiv = " + data.result.groupDiv);
					$("#testDiv").find(data.result.groupDiv).remove();
					$("#numberOfGroups").text(data.result.numberOfGroupsForTest);
					$("#numberOfQuestions").text(data.result.numberOfQuestionsForTest);
					$("#modificationDate").text(data.result.modificationDate);
					$("#modifiedBy").text(data.result.modifiedBy);
				}
			});
		}
	});
	
	$('#testDiv').on('click', '#deleteGroupBtn', function(e) {
		console.log("$('#testDiv').on('click', '#deleteGroupBtn')");
    });
	
	$('#confirmDeleteGroup').on('show.bs.modal', function (event) {
		console.log("$('#confirmDeleteGroup').on('show.bs.modal')");

		var button = $(event.relatedTarget);
		var testId = button.data('test-reference');
		var groupId = button.data('group-reference');
		var message = button.data('message');

		$(this).find('.modal-body #testReference').val(testId);
		$(this).find('.modal-body #groupReference').val(groupId);
		$(this).find('.modal-body #confirmDeleteGroupLabel').text(message);
	});

	$('#confirmDeleteGroup .modal-footer #confirmDeleteGroupBtnModal').on('click', function(){
		console.log("$('#confirmDeleteGroup .modal-footer #confirmDeleteGroupBtnModal').on('click')");
   
		$('#deleteGroupFormModal').submit();
 	});


    /***************************************************************************************/
    /************ Adding validation and handling events for "confirmEnableTest" ***********/
    /***************************************************************************************/

    var validatorEnableTest = $('#enableTestFormModal').validate({
        submitHandler: function(form) {
            console.log("***** submitHandler *****");

            $.ajax({
                url: URLWithContextPath + "/tests/doEnableTestAjax",
                data: $(form).serialize(),
                type: "POST",
                success: function(data) {
                    console.log("***** AJAX CALL *****");
                    console.log("Status: " + data.status);
                    console.log("Result: " + data.result);

                    $("#confirmEnableTest").modal('hide');
                    location.reload(true);

                    console.log("data.result.testDiv = " + data.result.testDiv);
                    $("#testDiv").find(data.result.testDiv).update();

                }
            });
        }
    });

    $('#testDiv').on('click', '#enableTestBtn', function(e) {
        console.log("$('#testDiv').on('click', '#enableTestBtn')");
    });

    $('#confirmEnableTest').on('show.bs.modal', function (event) {
        console.log("$('#confirmEnableTest').on('show.bs.modal')");

        var button = $(event.relatedTarget);
        var testId = button.data('test-reference');
        var message = button.data('message');

        $(this).find('.modal-body #testReference').val(testId);
        $(this).find('.modal-body #confirmEnableTestLabel').text(message);
    });

    $('#confirmEnableTest .modal-footer #confirmEnableTestBtnModal').on('click', function(){
        console.log("$('#confirmEnableTest .modal-footer #confirmEnableTestBtnModal').on('click')");

        $("#confirmEnableTest").modal('hide');

        $('#enableTestFormModal').submit();

    });


    /***************************************************************************************/
    /************ Adding validation and handling events for "confirmDisableTest" ***********/
    /***************************************************************************************/

    var validatorDisableTest = $('#disableTestFormModal').validate({
        submitHandler: function(form) {
            console.log("***** submitHandler *****");

            $.ajax({
                url: URLWithContextPath + "/tests/doDisableTestAjax",
                data: $(form).serialize(),
                type: "POST",
                success: function(data) {
                    console.log("***** AJAX CALL *****");
                    console.log("Status: " + data.status);
                    console.log("Result: " + data.result);

                    $("#confirmDisableTest").modal('hide');
                    location.reload(true);

                    console.log("data.result.testDiv = " + data.result.testDiv);
                    $("#testDiv").find(data.result.testDiv).update();

                }
            });
        }
    });

    $('#testDiv').on('click', '#disableTestBtn', function(e) {
        console.log("$('#testDiv').on('click', '#disableTestBtn')");
    });

    $('#confirmDisableTest').on('show.bs.modal', function (event) {
        console.log("$('#confirmDisableTest').on('show.bs.modal')");

        var button = $(event.relatedTarget);
        var testId = button.data('test-reference');
        var message = button.data('message');

        $(this).find('.modal-body #testReference').val(testId);
        $(this).find('.modal-body #confirmDisableTestLabel').text(message);
    });

    $('#confirmDisableTest .modal-footer #confirmDisableTestBtnModal').on('click', function(){
        console.log("$('#confirmDisableTest .modal-footer #confirmDisableTestBtnModal').on('click')");

        $("#confirmDisableTest").modal('hide');

        $('#disableTestFormModal').submit();

    });


    /***************************************************************************************/
	/************** Adding validation and handling events for "addAnswerModal" *************/
	/***************************************************************************************/

	jQuery.validator.addMethod("checkContentOfAnswer", function(value, element) {
		var isSuccess = false;
		console.log("isSuccess value before ajax call = " + isSuccess.toString());

		var data = {};
		data[csrfParameter] = csrfToken;

		if($('#addAnswerModal .modal-body #questionReference').val() != "") {
			console.log("addAnswerModal");
			data["questionId"] = $('#addAnswerModal .modal-body #questionReference').val();
		} else {
			console.log("editAnswerModal");
			data["questionId"] = $('#editAnswerModal .modal-body #questionReference').val();
		}

		if($('#editAnswerModal').find('.modal-body #answerReference').val() != "") {
			data["answerId"] = $('#editAnswerModal').find('.modal-body #answerReference').val();
		}
		
		//data["questionId"] = $('#addAnswerModal .modal-body #questionReference').val();
		data["contentOfAnswer"] = value;

		console.log("contentOfAnswer value = " + value);
	    
	    $.ajax({
			url: URLWithContextPath + "/tests/checkContentOfAnswer",
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
	}, "This answer already exists! Please enter another answer.");

	/*jQuery.validator.addMethod("checkAnswerPictureName", function(value, element) {
		var isSuccess = false;
		//var inputImageForAnswer = $('#addAnswerModal .modal-body #inputImageForAnswerModal');
		var inputImageForAnswer = null;
		console.log("isSuccess value before ajax call = " + isSuccess.toString());

		if($('#addAnswerModal .modal-body #inputImageForAnswerModal').val() != "") {
			inputImageForAnswer = $('#addAnswerModal .modal-body #inputImageForAnswerModal');
		} else {
			inputImageForAnswer = $('#editAnswerModal .modal-body #inputImageForAnswerModal');
		}
		
		if(inputImageForAnswer.val() != "") {			
			var data = {};
			data[csrfParameter] = csrfToken;
			data["pictureName"] = inputImageForAnswer[0].files[0].name;

			if($('#editAnswerModal').find('.modal-body #answerReference').val() != "") {
				data["answerId"] = $('#editAnswerModal').find('.modal-body #answerReference').val();
			}
	
			console.log("pictureName value = " + value);
		    
		    $.ajax({
				url: URLWithContextPath + "/tests/checkPictureName",
				type: "POST",
				async: false,
				data: data,
				success: function(data) {
					console.log("Data in response: " + data);
					isSuccess = data === "SUCCESS" ? true : false;
					console.log("isSuccess value after retrieving response = " + isSuccess.toString());
				}
			});
		} else {
			isSuccess = true;
		}
		
	    console.log("isSuccess value before return statement = " + isSuccess.toString());
	    return isSuccess;
	}, "This picture name already exists! Please choose another picture.");*/
	
	var validatorAddAnswer = $("#addAnswerFormModal").validate({
		rules: {
			contentOfAnswerModal: {
				required: true,
				maxlength: 2000, 
				checkContentOfAnswer: true
			},
			whetherCorrect: {
				required: true
			}/*,
			inputImageForAnswerModal: {
				checkAnswerPictureName: true
			}*/
		},
		messages: {
			contentOfAnswerModal: {
				required: "Content of answer textarea is required.",
				maxlength: "Given content of answer is too long, please change it."
			},
			whetherCorrect: {
				required: "Please choose true or false to current answer."
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
				url: URLWithContextPath + "/tests/doAddAnswerAjax",
				dataType: "json",
				success: function(data) { 
					console.log("********* AJAX CALL *********");
					console.log("Status: " + data.status);
					console.log("Result: " + data.result);

					var questionId = $('#addAnswerModal .modal-body #questionReference').val();
					var groupId = $('#addAnswerModal .modal-body #groupReference').val();
					console.log("questionId = " + questionId);
					console.log("groupId = " + groupId);

					var questionTable = $("#collapseGroup_" + groupId).find("#question_table_" + groupId);
					var questionRow = questionTable.find('tr[data-key="' + questionId +'"]');
					console.log("questionRow.data('key') = " + questionRow.data('key'));

					$("#addAnswerModal").modal('hide');

					// Update question
					var questionRowDT = questionTable.DataTable().row(questionRow);
					var cellsData = questionRowDT.data();
					cellsData[1] = data.result.numberOfAnswers;
					cellsData[2] = data.result.numberOfCorrectAnswers;
					questionRowDT.data(cellsData);

					$("#modificationDate").text(data.result.modificationDate);
					$("#modifiedBy").text(data.result.modifiedBy);

					if(questionRow.hasClass("shown")) {
						console.log("question details is shown");
						
						var nextRow = questionRow.next('tr');

						if (nextRow.find("#answer_table_" + questionId).length == 1) {
							console.log("next row contains answer table");

							var answersTable = nextRow.find("#answer_table_" + questionId).DataTable();

							var rowNode = answersTable.row.add( [
								data.result.contentOfAnswer,
							    data.result.whetherCorrect,
							    data.result.picture,
							    data.result.editAnswer,
							    data.result.deleteAnswer
							] ).draw( false ).node();

							$(rowNode).addClass("warning");
							$(rowNode).attr("id", data.result.answerId);
							
						} else {
							console.log("next row doesn't contain answer table");	

							var data = {};
							data[csrfParameter] = csrfToken;
							data["questionId"] = questionId;
							
							$.ajax({
								url: URLWithContextPath + "/tests/getAnswersByQuestion",
								data: data,
								type: "POST",
								success: function(data){
								console.log(data);
									if(data.status == "FAIL"){
										console.log("Error in retrieving data from response");
									} else if(data.status == "SUCCESS") {
										console.log("Correct response");
									}
									
									// returns DataTables API instance with selected row in the result set
									var questionRowDT = questionTable.DataTable().row(questionRow);
									questionRowDT.child( data.result ).show();
									$('div.slider', questionRowDT.child()).slideDown();

									
								}
							});
						}
						
					} else {
						console.log("question details is not shown");
					}
				}
			});
		}
	});
	
	$('#testDiv').on('click', '.collapse .col-sm-12 .table-responsive #addAnswerBtn', function(event) {
		console.log("$('#testDiv').on('click', '.collapse .col-sm-12 .table-responsive #addAnswerBtn')");
	});

	$('#addAnswerModal').on('show.bs.modal', function(event) {
		console.log("$('#addAnswerModal').on('show.bs.modal')");

		var button = $(event.relatedTarget);
		var groupId = button.data('group-reference');
		var questionId = button.data('question-reference');
		//var testId = $('button[name="editTestBtn"]').data('test-reference');
		
		$(this).find('.modal-body #questionReference').val(questionId);
		$(this).find('.modal-body #groupReference').val(groupId);
		//$(this).find('.modal-body #testReference').val(testId);
	});

	$('#addAnswerModal').on('hide.bs.modal', function(event) {
		console.log("$('#addAnswerModal').on('hide.bs.modal')");

		$('#addAnswerModal .modal-body #contentOfAnswerModal').css('height', '54px');
		$('#addAnswerModal .modal-body #contentOfAnswerModal').css('width', '568px');
		
		$('#addAnswerModal .modal-body #contentOfAnswerModal').val("");
		$('#addAnswerModal .modal-body #whetherCorrect').attr('checked', false);
		$('#addAnswerModal .modal-body #inputImageForAnswerModal').val("");
		$('#addAnswerModal .modal-body #imageForAnswerModal').attr('hidden', true);

		$(this).find('.modal-body #questionReference').val("");
		$(this).find('.modal-body #groupReference').val("");
		//$(this).find('.modal-body #testReference').val("");

		validatorAddAnswer.resetForm();
	});

	$('#addAnswerModal .modal-footer #addAnswerBtnModal').on('click', function(event) {
		console.log("$('#addAnswerModal .modal-footer #addAnswerBtnModal').on('click')");

		$('#addAnswerFormModal').submit();
	});

	$('#addAnswerModal .modal-body #inputImageForAnswerModal').on('change', function(event) {
		console.log("$('#addAnswerModal .modal-body #inputImageForAnswerModal').on('change')");
		readURL(this, $("#addAnswerModal .modal-body #imageForAnswerModal"));

		console.log("inputImageForAnswerModal value = " + $(this)[0].files[0].name);
	});
	
	/***************************************************************************************/
	/************ Adding validation and handling events for "editQuestionModal" ************/
	/***************************************************************************************/
	
	var validatorEditQuestion = $("#editQuestionFormModal").validate({
		rules: {
			contentOfQuestionModal: {
				required: true,
				maxlength: 2000,
				checkContentOfQuestion: true,
			}/*,
			inputImageForQuestionModal: {
				checkQuestionPictureName: true
			}*/
		},
		messages: {
			contentOfQuestionModal: {
				required: "Content of question textarea is required.",
				maxlength: "Given content of question is too long, please change it."
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
				url: URLWithContextPath + "/tests/doEditQuestionAjax",
				dataType: "json",
				success: function(data) { 
					console.log("********* AJAX CALL *********");
					console.log("Status: " + data.status);
					console.log("Result: " + data.result);

					var groupId = $('#editQuestionModal .modal-body #groupReference').val()
					var questionId = $('#editQuestionModal .modal-body #questionReference').val();
					console.log("groupId = " + groupId);
					console.log("questionId = " + questionId);
					var collapseDiv = $("#collapseGroup_" + groupId);
					var questionTable = collapseDiv.find("#question_table_" + groupId);
					var questionRow = questionTable.find('tr[data-key="' + questionId +'"]');

					$("#editQuestionModal").modal('hide');
					
					// returns DataTables API instance with selected row in the result set
					var questionRowDT = questionTable.DataTable().row(questionRow);

					console.log( "questionRowDT = " + questionRowDT.data() );
					console.log( "questionRowDT.data()[0] = " + questionRowDT.data()[0] );
					console.log( "questionRowDT.data()[1] = " + questionRowDT.data()[1] );
					console.log( "questionRowDT.data()[2] = " + questionRowDT.data()[2] );
					console.log( "questionRowDT.data()[3] = " + questionRowDT.data()[3] );
					console.log( "questionRowDT.data()[4] = " + questionRowDT.data()[4] );
					console.log( "questionRowDT.data()[5] = " + questionRowDT.data()[5] );
					console.log( "questionRowDT.data()[6] = " + questionRowDT.data()[6] );
					console.log( "questionRowDT.data()[7] = " + questionRowDT.data()[7] );

					var cellsData = questionRowDT.data();
					cellsData[0] = data.result.contentOfQuestion;
					cellsData[3] = data.result.picture;
					cellsData[5] = data.result.editQuestion;
					cellsData[6] = data.result.deleteQuestion;
					questionRowDT.data(cellsData);

					$("#modificationDate").text(data.result.modificationDate);
					$("#modifiedBy").text(data.result.modifiedBy);
				}
			});
		}
	});

	$('#testDiv').on('click', '.collapse .col-sm-12 .table-responsive #editQuestionBtn', function(event) {
		console.log("$('#testDiv').on('click', '.collapse .col-sm-12 .table-responsive #editQuestionBtn')");
	});

	$('#editQuestionModal').on('show.bs.modal', function(event) {
		console.log("$('#editQuestionModal').on('show.bs.modal')");

		var button = $(event.relatedTarget);
		var questionId = button.data('question-reference');
		var groupId = button.data('group-reference');
		var testId = $('button[name="editTestBtn"]').data('test-reference');
		var contentOfQuestion = button.data('content-of-question');
		var pictureSrc = button.data('picture-src');

		$(this).find('.modal-body #questionReference').val(questionId);
		$(this).find('.modal-body #groupReference').val(groupId);
		$(this).find('.modal-body #testReference').val(testId);
		$(this).find('.modal-body #contentOfQuestionModal').val(contentOfQuestion);

		if(pictureSrc != "") {
			$(this).find('.modal-body #imageForQuestionModal').attr('src', pictureSrc);
			$('#editQuestionModal .modal-body #imageForQuestionModal').attr('hidden', false);
		}
	});

	$('#editQuestionModal').on('hide.bs.modal', function(event) {
		console.log("$('#editQuestionModal').on('hide.bs.modal')");

		$('#editQuestionModal .modal-body #contentOfQuestionModal').css('height', '54px');
		$('#editQuestionModal .modal-body #contentOfQuestionModal').css('width', '568px');
		
		$('#editQuestionModal .modal-body #contentOfQuestionModal').val("");
		$('#editQuestionModal .modal-body #inputImageForQuestionModal').val("");
		$('#editQuestionModal .modal-body #imageForQuestionModal').attr('hidden', true);

		$(this).find('.modal-body #questionReference').val("");
		$(this).find('.modal-body #groupReference').val("");
		$(this).find('.modal-body #testReference').val("");

		validatorEditQuestion.resetForm();
	});

	$('#editQuestionModal .modal-footer #editQuestionBtnModal').on('click', function(event) {
		console.log("$('#editQuestionModal .modal-footer #editQuestionBtnModal').on('click')");

		$('#editQuestionFormModal').submit();
	});

	$('#editQuestionModal .modal-body #inputImageForQuestionModal').on('change', function(event) {
		console.log("$('#editQuestionModal .modal-body #inputImageForQuestionModal').on('change')");
		readURL(this, $("#editQuestionModal .modal-body #imageForQuestionModal"));

		console.log("inputImageForQuestionModal value = " + $(this)[0].files[0].name);
	});
	
	/***************************************************************************************/
	/********** Adding validation and handling events for "confirmDeleteQuestion" **********/
	/***************************************************************************************/
	
	var validatorDeleteQuestion = $('#deleteQuestionFormModal').validate({
		submitHandler: function(form) {
			console.log("***** submitHandler *****");

			$.ajax({
				url: URLWithContextPath + "/tests/doDeleteQuestionAjax",
				data: $(form).serialize(),
				type: "POST",
				success: function(data) {
					console.log("***** AJAX CALL *****");
					console.log("Status: " + data.status);
					console.log("Result: " + data.result);

					$("#confirmDeleteQuestion").modal('hide');

					var groupId = $('#confirmDeleteQuestion .modal-body #groupReference').val()
					var questionId = $('#confirmDeleteQuestion .modal-body #questionReference').val();
					console.log("groupId = " + groupId);
					console.log("questionId = " + questionId);
					var collapseDiv = $("#collapseGroup_" + groupId);
					var questionTable = collapseDiv.find("#question_table_" + groupId);
					var questionRow = questionTable.find('tr[data-key="' + questionId +'"]');
					
					// returns DataTables API instance with selected row in the result set
					var questionRowDT = questionTable.DataTable().row(questionRow);
					questionRowDT.remove().draw();

					// Update group and test 
					$("#testDiv").find("#group_" + groupId).find('#numberOfQuestions').text(data.result.numberOfQuestionsForGroup);
					$("#numberOfQuestions").text(data.result.numberOfQuestionsForTest);
					$("#modificationDate").text(data.result.modificationDate);
					$("#modifiedBy").text(data.result.modifiedBy);
				}
			});
		}
	});
	
	$('#testDiv').on('click', '.collapse .col-sm-12 .table-responsive #deleteQuestionBtn', function(event) {
		console.log("$('#testDiv').on('click', '.collapse .col-sm-12 .table-responsive #deleteQuestionBtn')");
	});
	
	$('#confirmDeleteQuestion').on('show.bs.modal', function (event) {
		console.log("$('#confirmDeleteQuestion').on('show.bs.modal')");

		var button = $(event.relatedTarget);
		var testId = button.data('test-reference');
		var groupId = button.data('group-reference');
		var questionId = button.data('question-reference');
		var message = button.data('message');

		$(this).find('.modal-body #testReference').val(testId);
		$(this).find('.modal-body #groupReference').val(groupId);
		$(this).find('.modal-body #questionReference').val(questionId);
		$(this).find('.modal-body #confirmDeleteQuestionLabel').text(message);
	});

	$('#confirmDeleteQuestion .modal-footer #confirmDeleteQuestionBtnModal').on('click', function(){
		console.log("$('#confirmDeleteQuestion .modal-footer #confirmDeleteQuestionBtnModal').on('click')");
   
		$('#deleteQuestionFormModal').submit();
 	});

	/***************************************************************************************/
	/************* Adding validation and handling events for "editAnswerModal" *************/
	/***************************************************************************************/

	var validatorEditAnswer = $("#editAnswerFormModal").validate({
		rules: {
			contentOfAnswerModal: {
				required: true,
				maxlength: 2000, 
				checkContentOfAnswer: true
			},
			whetherCorrect: {
				required: true
			}/*,
			inputImageForAnswerModal: {
				checkAnswerPictureName: true
			}*/
		},
		messages: {
			contentOfAnswerModal: {
				required: "Content of answer textarea is required.",
				maxlength: "Given content of answer is too long, please change it."
			},
			whetherCorrect: {
				required: "Please choose true or false to current answer."
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
				url: URLWithContextPath + "/tests/doEditAnswerAjax",
				dataType: "json",
				success: function(data) { 
					console.log("********* AJAX CALL *********");
					console.log("Status: " + data.status);
					console.log("Result: " + data.result);

					var answerId = $('#editAnswerModal .modal-body #answerReference').val();
					var questionId = $('#editAnswerModal .modal-body #questionReference').val();
					var groupId = $('#editAnswerModal .modal-body #groupReference').val();
					console.log("questionId = " + questionId);
					console.log("groupId = " + groupId);
					console.log("answerId = " + answerId);

					$("#editAnswerModal").modal('hide');

					var questionTable = $("#collapseGroup_" + groupId).find("#question_table_" + groupId);
					var questionRow = questionTable.find('tr[data-key="' + questionId +'"]');
					console.log("questionRow.data('key') = " + questionRow.data('key'));

					var nextRow = questionRow.next('tr');
					console.log("nextRow.find(\"#answer_table_\" + questionId).length = " + nextRow.find("#answer_table_" + questionId).length);
					var answerTable = nextRow.find("#answer_table_" + questionId);
					console.log("answerId of row = " + "#answer_" + answerId);
					console.log("answerTable.find(\"#answer_\" + answerId).length = " + answerTable.find("#answer_" + answerId).length)
					var answerRow = answerTable.find("#answer_" + answerId);
					console.log("answerRow.attr(\"id\") = " + answerRow.attr("id"));

					// returns DataTables API instance with selected row in the result set
					var answerRowDT = answerTable.DataTable().row(answerRow);

					console.log( "answerRowDT = " + answerRowDT.data() );
					console.log( "answerRowDT.data()[0] = " + answerRowDT.data()[0] );
					console.log( "answerRowDT.data()[1] = " + answerRowDT.data()[1] );
					console.log( "answerRowDT.data()[2] = " + answerRowDT.data()[2] );
					console.log( "answerRowDT.data()[3] = " + answerRowDT.data()[3] );
					console.log( "answerRowDT.data()[4] = " + answerRowDT.data()[4] );

					var cellsData = answerRowDT.data();
					cellsData[0] = data.result.contentOfAnswer;
					cellsData[1] = data.result.whetherCorrect;
					cellsData[2] = data.result.picture;
					cellsData[3] = data.result.editAnswer;
					cellsData[4] = data.result.deleteAnswer;
					answerRowDT.data(cellsData);

					// Update question
					var questionRowDT = questionTable.DataTable().row(questionRow);
					console.log( "questionRowDT = " + questionRowDT.data() );
					var cellsDataForQuestion = questionRowDT.data();
					cellsDataForQuestion[2] = data.result.numberOfCorrectAnswers;
					questionRowDT.data(cellsDataForQuestion);

					$("#modificationDate").text(data.result.modificationDate);
					$("#modifiedBy").text(data.result.modifiedBy);
				}
			});
		}
	});

	$('#testDiv').on('click', '.collapse .col-sm-12 .table-responsive #editAnswerBtn', function(event) {
		console.log("$('#testDiv').on('click', '.collapse .col-sm-12 .table-responsive #editAnswerBtn')");
	});

	$('#editAnswerModal').on('show.bs.modal', function(event) {
		console.log("$('#editAnswerModal').on('show.bs.modal')");

		var button = $(event.relatedTarget);
		var answerId = button.data('answer-reference');
		var questionId = button.data('question-reference');
		var groupId = button.data('group-reference');
		//var testId = $('button[name="editTestBtn"]').data('test-reference');
		var contentOfAnswer = button.data('content-of-answer');
		var whetherCorrect = button.data('whether-correct')
		var pictureSrc = button.data('picture-src');

		$(this).find('.modal-body #answerReference').val(answerId);
		$(this).find('.modal-body #questionReference').val(questionId);
		$(this).find('.modal-body #groupReference').val(groupId);
		//$(this).find('.modal-body #testReference').val(testId);
		$(this).find('.modal-body #contentOfAnswerModal').val(contentOfAnswer);

		console.log("whetherCorrect value = " + whetherCorrect);
		$(this).find("input[name=whetherCorrect][value=" + whetherCorrect + "]").prop('checked', true);

		if(pictureSrc != "") {
			$(this).find('.modal-body #imageForAnswerModal').attr('src', pictureSrc);
			$('#editAnswerModal .modal-body #imageForAnswerModal').attr('hidden', false);
		}
	});

	$('#editAnswerModal').on('hide.bs.modal', function(event) {
		console.log("$('#editAnswerModal').on('hide.bs.modal')");

		$('#editAnswerModal .modal-body #contentOfAnswerModal').css('height', '54px');
		$('#editAnswerModal .modal-body #contentOfAnswerModal').css('width', '568px');
		
		$('#editAnswerModal .modal-body #contentOfAnswerModal').val("");
		$('#editAnswerModal .modal-body #whetherCorrect').attr('checked', false);
		$('#editAnswerModal .modal-body #inputImageForAnswerModal').val("");
		$('#editAnswerModal .modal-body #imageForAnswerModal').attr('hidden', true);

		$(this).find('.modal-body #groupReference').val("");
		$(this).find('.modal-body #questionReference').val("");		
		$(this).find('.modal-body #answerReference').val("");

		validatorEditAnswer.resetForm();
	});

	$('#editAnswerModal .modal-footer #editAnswerBtnModal').on('click', function(event) {
		console.log("$('#editAnswerModal .modal-footer #editAnswerBtnModal').on('click')");

		$('#editAnswerFormModal').submit();
	});

	$('#editAnswerModal .modal-body #inputImageForAnswerModal').on('change', function(event) {
		console.log("$('#editAnswerModal .modal-body #inputImageForAnswerModal').on('change')");
		readURL(this, $("#editAnswerModal .modal-body #imageForAnswerModal"));

		console.log("inputImageForAnswerModal value = " + $(this)[0].files[0].name);
	});

	/***************************************************************************************/
	/*********** Adding validation and handling events for "confirmDeleteAnswer" ***********/
	/***************************************************************************************/

	var validatorDeleteAnswer = $('#deleteAnswerFormModal').validate({
		submitHandler: function(form) {
			console.log("***** submitHandler *****");

			$.ajax({
				url: URLWithContextPath + "/tests/doDeleteAnswerAjax",
				data: $(form).serialize(),
				type: "POST",
				success: function(data) {
					console.log("***** AJAX CALL *****");
					console.log("Status: " + data.status);
					console.log("Result: " + data.result);

					$("#confirmDeleteAnswer").modal('hide');

					var answerId = $('#confirmDeleteAnswer .modal-body #answerReference').val();
					var questionId = $('#confirmDeleteAnswer .modal-body #questionReference').val();
					var groupId = $('#confirmDeleteAnswer .modal-body #groupReference').val();
					console.log("answerId = " + answerId);
					console.log("questionId = " + questionId);
					console.log("groupId = " + groupId);

					var questionTable = $("#collapseGroup_" + groupId).find("#question_table_" + groupId);
					var questionRow = questionTable.find('tr[data-key="' + questionId +'"]');
					console.log("questionRow.data('key') = " + questionRow.data('key'));

					var nextRow = questionRow.next('tr');
					var answerTable = nextRow.find("#answer_table_" + questionId);
					var answerRow = answerTable.find("#answer_" + answerId);

					// returns DataTables API instance with selected row in the result set
					var answerRowDT = answerTable.DataTable().row(answerRow);
					console.log("answerRowDT.data() = " + answerRowDT.data())
					answerRowDT.remove().draw();
					
					// Update question
					var questionRowDT = questionTable.DataTable().row(questionRow);
					var cellsData = questionRowDT.data();
					cellsData[1] = data.result.numberOfAnswers;
					cellsData[2] = data.result.numberOfCorrectAnswers;
					questionRowDT.data(cellsData);

					$("#modificationDate").text(data.result.modificationDate);
					$("#modifiedBy").text(data.result.modifiedBy);
				}
			});
		}
	});
	
	$('#testDiv').on('click', '.collapse .col-sm-12 .table-responsive #deleteAnswerBtn', function(event) {
		console.log("$('#testDiv').on('click', '.collapse .col-sm-12 .table-responsive #deleteAnswerBtn')");
	});
	
	$('#confirmDeleteAnswer').on('show.bs.modal', function (event) {
		console.log("$('#confirmDeleteAnswer').on('show.bs.modal')");

		var button = $(event.relatedTarget);
		var groupId = button.data('group-reference');
		var questionId = button.data('question-reference');
		var answerId = button.data('answer-reference');
		var message = button.data('message');

		$(this).find('.modal-body #groupReference').val(groupId);
		$(this).find('.modal-body #questionReference').val(questionId);
		$(this).find('.modal-body #answerReference').val(answerId);
		$(this).find('.modal-body #confirmDeleteAnswerLabel').text(message);
	});

	$('#confirmDeleteAnswer .modal-footer #confirmDeleteAnswerBtnModal').on('click', function(){
		console.log("$('#confirmDeleteAnswer .modal-footer #confirmDeleteAnswerBtnModal').on('click')");
   
		$('#deleteAnswerFormModal').submit();
 	});
	
	/***************************************************************************************/
	/*********************************** Other functions ***********************************/
	/***************************************************************************************/
	
	$('#testDiv').on('mouseenter', '.collapse .col-sm-12 .table-responsive #questionImage', function(event) {
		console.log("$('#testDiv').on('mouseenter', '.collapse .col-sm-12 .table-responsive #questionImage')");
       	
        var position = $(this).offset();
        console.log("position.left = " + position.left);
        console.log("position.top = " + position.top); 
        
        var src = $(this).attr('src');
        console.log("src = " + src);
		
		setTimeout(function(){
			$("#hiddenDiv").css({top: position.top-100, left: position.left-100});
			$("#hiddenImg").attr("src", src);
			$("#hiddenDiv").show();
			$("#hiddenImg").stop().animate({ width: 300, height: 300, top: -100, left: -100}, 300);	
    	}, 300);
	});

	$('#testDiv').on('mouseenter', '.collapse .col-sm-12 .table-responsive #answerImage', function(event) {
		console.log("$('#testDiv').on('mouseenter', '.collapse .col-sm-12 .table-responsive #answerImage')");
       	
        var position = $(this).offset();
        console.log("position.left = " + position.left);
        console.log("position.top = " + position.top); 
        
        var src = $(this).attr('src');
        console.log("src = " + src);
		
		setTimeout(function(){
			$("#hiddenDiv").css({top: position.top-100, left: position.left-100});
			$("#hiddenImg").attr("src", src);
			$("#hiddenDiv").show();
			$("#hiddenImg").stop().animate({ width: 300, height: 300, top: -100, left: -100}, 300);	
    	}, 300);
	});

	$('#hiddenDiv').on({
		mouseleave: function(event) {
			console.log("$('#hiddenDiv').on(mouseleave)");
			$("#hiddenImg").stop().animate({ width: 100, height: 100, top: 0, left: 0}, 300);
			setTimeout(function(){
				$("#hiddenDiv").hide();
			}, 300);
		}
	});
	
	$('#testDiv').on('click', '.col-sm-12 .collapse .col-sm-12 .table-responsive .details-control', function() {
		console.log("$('#testDiv').on('click', '.col-sm-12 .collapse .col-sm-12 .table-responsive .details-control')");

		var tr = $(this).closest('tr');
		var table = $(this).closest('table').DataTable();
		var row = table.row(tr);
	
		if (row.child.isShown()) {
			$('div.slider', row.child()).slideUp( function () {
				row.child.hide();
				tr.removeClass('shown');
			} );
		} else {
			var data = {};
			data[csrfParameter] = csrfToken;
			data["questionId"] = tr.data('key');
			console.log("tr.data('key') = " + tr.data('key'));
			
			$.ajax({
				url: URLWithContextPath + "/tests/getAnswersByQuestion",
				data: data,
				type: "POST",
				success: function(data){
					console.log(data);
					if(data.status == "FAIL"){
						console.log("Error in retrieving data from response");
					} else if(data.status == "SUCCESS") {
						console.log("Correct response");
					}
					row.child( data.result ).show();
					tr.addClass('shown');
					$('div.slider', row.child()).slideDown();
				}
			});
		}
	});

	$('#testDiv').on('shown.bs.collapse', '.col-sm-12 .collapse', function() {
		console.log("$('#testDiv').on('shown.bs.collapse', '.col-sm-12 .collapse')");

		var collapseId = "#" + $(this).attr('id');
		var groupId = $(this).data('reference');
		
		console.log("collapseId = " + collapseId);
		console.log("groupId = " + groupId);
		
		var data = {};
		data[csrfParameter] = csrfToken;
		data["groupId"] = groupId;
		
		$.ajax({
			url: URLWithContextPath + "/tests/getQuestionsByGroup",    
			data: data,
			dataType: 'json',
			type: "POST",
			success: function(data){
				console.log(data);
				if(data.status == "FAIL"){
					console.log("Error in retrieving data from response");
					$(collapseId).html(data.result);
				} else if(data.status == "SUCCESS") {
					console.log("Correct response");
					$(collapseId).html(data.result);
				}
			}        
		});
	});
</script>

<%@ include file="partials/footer.jsp" %>