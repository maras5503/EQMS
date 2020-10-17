<%@ include file="partials/header.jsp" %>

<div class="row">

    <div class="col-sm-12">

        <c:choose>
            <c:when test="${not empty allStudentGroupsModel}">

                <div class="page-header" style="margin-top: 10px;">
                    <div class="col-sm-offset-3 col-sm-6" align="center">
                        <h1>All groups</h1>
                    </div>

                    <div id="addStudentGroup" class="pull-right" >
                        <button type="button" class="btn btn-success" id="addStudentGroupBtn" name="addStudentGroupBtn" style="margin-top: 20px; margin-bottom: 10px;" data-toggle="modal" data-target="#addStudentGroupModal"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add new group</button>
                    </div>

                    <div class="clearfix"></div>
                </div>

                <!-- try http://getbootstrap.com/css/#tables-responsive -->
                <div class="table-responsive" style="margin-bottom: 0px">
                    <table class="table table-bordered" id="studentgroups_table">
                        <thead>
                        <tr class="success">
                            <th style="width: 35%;">GROUP NAME</th>
                            <th style="border-right-width: 0px;"></th>
                            <th style="border-right-width: 0px;"></th> <!-- style="empty-cells: hide" -->
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="studentgroup" items="${allStudentGroupsModel}">
                            
                            <tr class="success" id="${studentgroup.studentgroupId}">
                                <td><c:out value="${studentgroup.studentgroupName}" /></td>
                            
                                    <td>
                                        <button type="button" class="btn btn-info btn-block btn-sm" id="editStudentGroupBtn" name="editStudentGroupBtn" data-toggle="modal" data-target="#editStudentGroupModal" data-studentgroup-name="${studentgroup.studentgroupName}" data-studentgroup-reference="${studentgroup.studentgroupId}"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit</button>
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-danger btn-block btn-sm" id="deleteStudentGroupBtn" name="deleteStudentGroupBtn" data-toggle="modal" data-target="#confirmDeleteStudentGroup" data-title="Delete Group" data-message="Are you sure you want to delete group '${studentgroup.studentgroupName}'?" data-studentgroup-reference="${studentgroup.studentgroupId}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete</button>
                                    </td>
                            </tr>

                            
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="well">
                    There are no groups.
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="modal fade" id="addStudentGroupModal" tabindex="-1" role="dialog" aria-labelledby="addStudentGroupLabelModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="addStudentGroupTitleModal">Add new group</h4>
            </div>
            <div class="modal-body">
                <form action="<c:url value="/groups_of_students/doAddStudentGroupAjax"/>" method="POST" id="addStudentGroupFormModal">
                    <div class="form-group">
                        <label for="studentGroupNameModal" class="control-label">Group:</label>
                        <input type="text" class="form-control" id="studentGroupNameModal" name="studentGroupNameModal" placeholder="Group name" maxlength="100" aria-describedby="inputAddStudentGroupError">
                        <span id="glyphiconErrorAddStudentGroup" class="form-control-feedback" aria-hidden="true"></span>
                        <span id="inputAddStudentGroupError" class="sr-only">(error)</span>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" id="addStudentGroupBtnModal">Add group</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="editStudentGroupModal" tabindex="-1" role="dialog" aria-labelledby="editStudentGroupLabelModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="editStudentGroupTitleModal">Edit group</h4>
            </div>
            <div class="modal-body">
                <form action="<c:url value="/groups_of_students/doEditStudentGroupAjax"/>" method="POST" id="editStudentGroupFormModal">
                    <div class="form-group">
                        <label for="studentGroupNameModal" class="control-label">Group:</label>
                        <input type="text" class="form-control" id="studentGroupNameModal" name="studentGroupNameModal" placeholder="Group name" maxlength="100" aria-describedby="inputEditStudentGroupError">
                        <span id="glyphiconErrorEditStudentGroup" class="form-control-feedback" aria-hidden="true"></span>
                        <span id="inputEditStudentGroupError" class="sr-only">(error)</span>
                    </div>
                    <input type="hidden" name="studentGroupReference" id="studentGroupReference" />
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" id="editStudentGroupBtnModal">Edit group</button>
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

    $('#studentgroups_table').DataTable( {
        "paging": true,
        "searching": true,
        "ordering":  true,
        "lengthMenu": [ [5, 10, 25, 50, -1], [5, 10, 25, 50, "All"] ],
        "pageLength": 10,
        "columns": [
            null,
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

    jQuery.validator.addMethod("checkStudentGroupName", function(value, element) {
        var isSuccess = false;
        console.log("isSuccess value before ajax call = " + isSuccess.toString());

        var data = {};
        data[csrfParameter] = csrfToken;
        data["studentgroupName"] = value;

        console.log("studentgroupName value = " + value);

        $.ajax({
            url: URLWithContextPath + "/groups_of_students/checkStudentGroupName",
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

    var validatorAddStudentGroup = $("#addStudentGroupFormModal").validate({
        rules: {
            studentGroupNameModal: {
                required: true,
                maxlength: 200,
                checkStudentGroupName: true
            }
        },
        messages: {
            studentGroupNameModal: {
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
                url: URLWithContextPath + "/groups_of_students/doAddStudentGroupAjax",
                data: $(form).serialize(),
                type: "POST",
                success: function(data) {
                    console.log("********* AJAX CALL *********");
                    console.log("Status: " + data.status);
                    console.log("Result: " + data.result);

                    var studentgroupsTableDT = $('#studentgroups_table').DataTable();
                    var rowNode = studentgroupsTableDT.row.add( [
                        data.result.studentgroupName,
                        data.result.editStudentGroup,
                        data.result.deleteStudentGroup
                    ] ).draw( false ).node();

                    $(rowNode).addClass("success");
                    $(rowNode).attr("id", data.result.studentgroupId);

                    $("#addStudentGroupModal").modal('hide');
                }
            });
        }
    });

    $('#addStudentGroupBtn').on('click', function() {
        console.log("$('#addStudentGroupBtn').on('click')");
    });

    $('#addStudentGroupModal').on('show.bs.modal', function(event) {
        console.log("$('#addStudentGroupModal').on('show.bs.modal')");
    });

    $('#addStudentGroupModal').on('hide.bs.modal', function(event) {
        console.log("$('#addStudentGroupModal').on('hide.bs.modal')");

        validatorAddStudentGroup.resetForm();
    });

    $('#addStudentGroupModal .modal-footer #addStudentGroupBtnModal').on('click', function(event) {
        console.log("$('#addStudentGroupModal .modal-footer #addStudentGroupBtnModal').on('click')");

        $('#addStudentGroupFormModal').submit();
    });

    /***************************************************************/
    /*** Adding validation and handling events for "editSubject" ***/
    /***************************************************************/

    var validatorEditStudentGroup = $("#editStudentGroupFormModal").validate({
        rules: {
            studentGroupNameModal: {
                required: true,
                maxlength: 200,
                checkStudentGroupName: true
            }
        },
        messages: {
            studentGroupNameModal: {
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
                url: URLWithContextPath + "/groups_of_students/doEditStudentGroupAjax",
                data: $(form).serialize(),
                type: "POST",
                success: function(data) {
                    console.log("********* AJAX CALL *********");
                    console.log("Status: " + data.status);
                    console.log("Result: " + data.result);

                    var studentgroupId = $("#editStudentGroupModal").find('.modal-body #studentGroupReference').val();
                    var studentgroupsTable = $('#studentgroups_table');
                    var studentgroupRow = studentgroupsTable.find('#' + studentgroupId);

                    // returns DataTables API instance with selected row in the result set
                    var studentgroupRowDT = studentgroupsTable.DataTable().row(studentgroupRow);

                    var cellsData = studentgroupRowDT.data();
                    cellsData[0] = data.result.studentgroupName;
                    cellsData[1] = data.result.editStudentGroup;
                    cellsData[2] = data.result.deleteStudentGroup;
                    studentgroupRowDT.data(cellsData);

                    $("#editStudentGroupModal").modal('hide');
                }
            });
        }
    });

    $('#studentgroups_table #editStudentGroupBtn').on('click', function() {
        console.log("$('#studentgroups_table #editStudentGroupBtn').on('click')");
    });

    $('#editStudentGroupModal').on('show.bs.modal', function(event) {
        console.log("$('#editStudentGroupModal').on('show.bs.modal')");

        var button = $(event.relatedTarget);
        var studentgroupId = button.data('studentgroup-reference');
        var studentgroupName = button.data('studentgroup-name');

        $(this).find('.modal-body #studentGroupReference').val(studentgroupId);
        $(this).find('.modal-body #studentGroupNameModal').val(studentgroupName);
    });

    $('#editStudentGroupModal').on('hide.bs.modal', function(event) {
        console.log("$('#editStudentGroupModal').on('hide.bs.modal')");

        $(this).find('.modal-body #studentGroupReference').val("");

        validatorEditStudentGroup.resetForm();
    });

    $('#editStudentGroupModal .modal-footer #editStudentGroupBtnModal').on('click', function(event) {
        console.log("$('#editStudentModal .modal-footer #editStudentGroupBtnModal').on('click')");

        $('#editStudentGroupFormModal').submit();
    });

</script>
<%@include file="partials/footer.jsp"%>
