<%@include file= "partials/header.jsp"%>
<div class="row">

    <div class="col-sm-12" style="padding-left: 0px; padding-right: 0px;">

        <div class="page-header" align="center" style="margin-top: 10px;">
            <h1>Students</h1>
        </div>

        <div class="col-sm-offset-3 col-sm-6" id="studentGroupSelect" align="center" style="padding-bottom: 20px">
            <select class="form-control" id="studentGroupsDropDown" name="studentGroupsDropDown">
                <option value="" selected="selected">Select group</option>

                <c:forEach var="studentgroup" items="${allStudentGroupsModel}">
                    <option value="${studentgroup.studentgroupId}">${studentgroup.studentgroupName}</option>
                </c:forEach>
            </select>
        </div>

        <div id="addStudentDiv" class="span3" align="right" hidden="hidden">
            <form method="POST" id="addStudentForm" >
                <input type="hidden" name="studentGroupReference" id="studentGroupReference" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>
            <button type="button" class="btn btn-success" id="addStudentBtn" data-toggle="modal" data-target="#addStudentModal"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add new student</button>
        </div>

        <div class="col-sm-12" id="studentsDiv" style="padding-right: 0px; padding-left: 0px;">
        </div>

    </div>

</div>

<div class="modal fade" id="addStudentModal" tabindex="-1" role="dialog" aria-labelledby="addStudentLabelModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="addStudentGroupTitleModal">Add new student</h4>
            </div>
            <div class="modal-body">
                <form action="<c:url value="/students/doAddStudentAjax"/>" method="POST" id="addStudentFormModal">
                    <div class="form-group">
                        <label for="studentFirstnameModal" class="control-label">Firstname:</label>
                        <input type="text" class="form-control" id="studentFirstnameModal" name="studentFirstnameModal" placeholder="Firstname" maxlength="100" aria-describedby="inputAddStudentError">
                        <span id="glyphiconErrorAddStudentFirstname" class="form-control-feedback" aria-hidden="true"></span>
                        <span id="inputAddStudentFirstnameError" class="sr-only">(error)</span>
                    </div>
                    <div class="form-group">
                        <label for="studentLastnameModal" class="control-label">Lastname:</label>
                        <input type="text" class="form-control" id="studentLastnameModal" name="studentLastnameModal" placeholder="Lastname" maxlength="100" aria-describedby="inputAddStudentError">
                        <span id="glyphiconErrorAddStudentLastname" class="form-control-feedback" aria-hidden="true"></span>
                        <span id="inputAddStudentLastnameError" class="sr-only">(error)</span>
                    </div>
                    <div class="form-group">
                        <label for="studentFirstnameModal" class="control-label">E-mail:</label>
                        <input type="email" class="form-control" id="studentEmailModal" name="studentEmailModal" placeholder="Email address" maxlength="35" required="required"  data-toggle="popover_email" data-placement="right" data-html="true">
                        <span id="glyphiconErrorAddStudentEmail" class="form-control-feedback" aria-hidden="true"></span>
                        <span id="inputAddStudentEmailError" class="sr-only">(error)</span>
                    </div>
                    <div>
                        <input type="hidden" name="studentGroupReferenceModal" id="studentGroupReferenceModal" />
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" id="addStudentBtnModal">Add student</button>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">


    $("#studentGroupsDropDown").change(function(){

        console.log("csrfParameter = " + csrfParameter);
        console.log("csrfToken = " + csrfToken);

        var data = {};
        data[csrfParameter] = csrfToken;
        data["studentgroupId"] = $(this).val();

        if($(this).val() != "") {

            $('#addStudentDiv').show();
            $('#addStudentForm').find('#studentGroupReference').val($(this).val());

            $.ajax({
                url: URLWithContextPath + "/students/getStudentsByStudentGroup",
                data: data,
                dataType: 'json',
                type: "POST",
                success: function(data){
                    console.log(data);

                    if(data.status == "FAIL"){
                        console.log("Error in retrieving data from response");
                    } else if(data.status == "SUCCESS") {
                        console.log("Correct response");
                        $("#studentsDiv").html(data.result);

                        var table = $('#students_table').DataTable( {
                            "paging": true,
                            "searching": true,
                            "ordering":  true,
                            "lengthMenu": [ [1, 5, 10, 25, 50, -1], [1, 5, 10, 25, 50, "All"] ],
                            "pageLength": 10,
                            "columns": [
                                null,
                                null,
                                null,
                                { "orderable": false },
                                { "orderable": false }
                            ],
                            "language": {
                                "info": "Showing _START_ to _END_ of _TOTAL_ students",		// default is: Showing _START_ to _END_ of _TOTAL_ entries
                                "infoEmpty": "Showing 0 to 0 of 0 students", 					// default is: Showing 0 to 0 of 0 entries
                                "infoFiltered": "(filtered from _MAX_ total students)",		// default is: (filtered from _MAX_ total entries)
                                "lengthMenu": "Show _MENU_ students"							// default is: Show _MENU_ entries
                            }
                        } );
                    }
                }
            });
        } else {
            $('#studentsDiv').html('');
            $('#addStudentDiv').hide();
            $('#addStudentForm').find('#studentGroupReference').val("");
        }
    });
</script>

<%@ include file="partials/footer.jsp" %>