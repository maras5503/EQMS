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
                <h4 class="modal-title" id="addStudentTitleModal">Add new student</h4>
            </div>
            <div class="modal-body">
                <form action="<c:url value="/students/doAddStudentAjax"/>" method="POST" id="addStudentFormModal">
                    <div class="form-group">
                        <label for="studentFirstnameModal" class="control-label">Firstname:</label>
                        <input type="text" class="form-control" id="studentFirstnameModal" name="studentFirstnameModal" placeholder="Student firstname" maxlength="100" aria-describedby="inputAddStudentFirstnameError">
                        <span id="glyphiconErrorAddStudentFirstname" class="form-control-feedback" aria-hidden="true"></span>
                        <span id="inputAddStudentFirstnameError" class="sr-only">(error)</span>
                    </div>
                    <div class="form-group">
                        <label for="studentLastnameModal" class="control-label">Lastname:</label>
                        <input type="text" class="form-control" id="studentLastnameModal" name="studentLastnameModal" placeholder="Student lastname" maxlength="100" aria-describedby="inputAddStudentLastnameError">
                        <span id="glyphiconErrorAddStudentLastname" class="form-control-feedback" aria-hidden="true"></span>
                        <span id="inputAddStudentLastnameError" class="sr-only">(error)</span>
                    </div>
                    <div class="form-group">
                        <label for="studentEmailModal" class="control-label">E-mail:</label>
                        <input type="text" class="form-control" id="studentEmailModal" name="studentEmailModal" placeholder="Student email" maxlength="100" aria-describedby="inputAddStudentEmailError">
                        <span id="glyphiconErrorAddStudentEmail" class="form-control-feedback" aria-hidden="true"></span>
                        <span id="inputAddStudentEmailError" class="sr-only">(error)</span>
                    </div>
                    <input type="hidden" name="studentGroupReferenceModal" id="studentGroupReferenceModal" />
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
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

    /**************************************************************/
    /*** Adding validation and handling events for "addStudent" ***/
    /**************************************************************/

    jQuery.validator.addMethod("checkStudentEmail", function(value, element) {
        var isSuccess = false;
        console.log("isSuccess value before ajax call = " + isSuccess.toString());

        var data = {};
        data[csrfParameter] = csrfToken;
        data["studentEmail"] = value;

        console.log("studentEmail value = " + value);

        $.ajax({
            url: URLWithContextPath + "/students/checkStudentEmail",
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
    }, "This email already exists! Please enter another email name.");


    var validatorAddStudent = $("#addStudentFormModal").validate({
        rules: {
            studentFirstnameModal: {
                required: true,
                maxlength: 200
            },
            studentLastnameModal: {
                required: true,
                maxlength: 200
            },
            studentEmailModal: {
                required: true,
                maxlength: 30,
                checkStudentEmail: true

            }
        },
        messages: {
            studentFirstnameModal: {
                required: "Firstname text field is required.",
                maxlength: "Given firstname is too long, please change it."
            },
            studentLastnameModal: {
                required: "Lastname text field is required.",
                maxlength: "Given lastname is too long, please change it."
            },
            studentEmailModal: {
                required: "Email text field is required.",
                maxlength: "Given Email is too long, please change it."
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
                url: URLWithContextPath + "/students/doAddStudentAjax",
                data: $(form).serialize(),
                type: "POST",
                success: function(data) {
                    console.log("********* AJAX CALL *********");
                    console.log("Status: " + data.status);
                    console.log("Result: " + data.result);

                    var studentsTableDT = $('#students_table').DataTable();
                    var rowNode = studentsTableDT.row.add( [
                        data.result.studentFirstname,
                        data.result.studentLastname,
                        data.result.studentEmail,
                        data.result.editStudent,
                        data.result.deleteStudent
                    ] ).draw( false ).node();

                    $(rowNode).addClass("success");
                    $(rowNode).attr("id", data.result.studentId);

                    $("#addStudentModal").modal('hide');
                }
            });
        }
    });

    $('#addStudentBtnModal').on('click', function() {
        console.log("$('#addStudentBtnModal').on('click')");
    });

    $('#addStudentBtn').on('click', function() {
        console.log("$('#addStudentBtn').on('click')");
        $('#addStudentFormModal').find('#studentGroupReferenceModal').val($("#studentGroupsDropDown").val());
    });

    $('#addStudentModal').on('show.bs.modal', function(event) {
        console.log("$('#addStudentModal').on('show.bs.modal')");
    });

    $('#addStudentModal').on('hide.bs.modal', function(event) {
        console.log("$('#addStudentModal').on('hide.bs.modal')");

        validatorAddStudent.resetForm();
    });

    $('#addStudentModal .modal-footer #addStudentBtnModal').on('click', function(event) {
        console.log("$('#addStudentModal .modal-footer #addStudentBtnModal').on('click')");

        $('#addStudentFormModal').submit();
    });

</script>

<%@ include file="partials/footer.jsp" %>