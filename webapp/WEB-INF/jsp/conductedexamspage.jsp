<%@include file="partials/header.jsp"%>


<div class="row">

    <div class="col-sm-12">

        <c:choose>
            <c:when test="${not empty allConductedExams}">

                <div class="page-header" style="margin-top: 10px;">
                    <div class="col-sm-offset-3 col-sm-6" align="center">
                        <h1>History of conducted exams</h1>
                    </div>
                    <div class="clearfix"></div>
                </div>
                <div class="table-responsive" style="margin-bottom: 0px">
                    <table class="table table-bordered" id="conducted_exams_table">
                        <thead>
                        <tr class="success">
                            <th>TEST NAME</th>
                            <th>SUBJECT NAME</th>
                            <th>STUDENT GROUP</th>
                            <th>DATE</th>
                            <th></th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach varStatus="loop" var="exam" items="${allConductedExams}">
                            <tr class="success" id="${exam.conductedExamId}">
                                <td><c:out value="${exam.testName}" /></td>
                                <td><c:out value="${exam.subjectName}" /></td>
                                <td><c:out value="${exam.groupsOfStudents.studentgroupName}" /></td>
                                <td><c:out value="${exam.examDate}"/></td>
                                <td>
                                    <form action="<c:url value="/history/examResults"/>" method="POST"   id="examResultsForm">
                                        <input type="hidden" name="studentGroupReference" value="${exam.groupsOfStudents.studentgroupId}"/>
                                        <input type="hidden" name="conductedExamReference" value="${exam.conductedExamId}" />
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                        <button type="submit" class="btn btn-info btn-block btn-sm" id="resultsBtn" name="resultsBtn"><span class="glyphicon glyphicon-stats" aria-hidden="true"></span> Results</button>
                                    </form>
                                </td>
                                <td>
                                    <button type="button" class="btn btn-danger btn-block btn-sm" id="deleteConductedExamBtn" name="deleteConductedExamBtn" data-toggle="modal" data-target="#confirmDeleteConductedExam" data-title="Delete" data-message="Are you sure you want to delete conducted exam history?" data-conductedexam-reference="${exam.conductedExamId}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete</button>
                                </td>
                            </tr>

                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="well">
                    There are no conducted exams.
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="modal fade" id="confirmDeleteConductedExam" tabindex="-1" role="dialog" aria-labelledby="confirmDeleteConductedExamLabelModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="confirmDeleteConductedExamTitleModal">Delete</h4>
            </div>
            <div class="modal-body">
                <div class="form-group" id="confirmDeleteConductedExamLabel">
                </div>
                <form action="<c:url value="/history/deleteConductedExam"/>" method="POST" id="deleteConductedExamFormModal" >
                    <input type="hidden" name="conductedExamReference" id="conductedExamReference" />
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-danger" id="confirmDeleteConductedExamBtnModal">Delete</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    $('#conducted_exams_table').DataTable( {
        "paging": true,
        "searching": true,
        "ordering":  true,
        "lengthMenu": [ [5, 10, 25, 50, -1], [5, 10, 25, 50, "All"] ],
        "pageLength": 10,
        "columns": [
            null,
            null,
            null,
            null,
            { "orderable": false },
            { "orderable": false }
        ],
        "language": {
            "info": "Showing _START_ to _END_ of _TOTAL_ conducted exams",		// default is: Showing _START_ to _END_ of _TOTAL_ entries
            "infoEmpty": "Showing 0 to 0 of 0 conducted exams", 				// default is: Showing 0 to 0 of 0 entries
            "infoFiltered": "(filtered from _MAX_ total conducted exams)",		// default is: (filtered from _MAX_ total entries)
            "lengthMenu": "Show _MENU_ conducted exams"						// default is: Show _MENU_ entries
        }
    } );

    var validatorDeleteGroup = $('#deleteConductedExamFormModal').validate({
        submitHandler: function(form) {
            console.log("********* submitHandler *********");

            $.ajax({
                url: URLWithContextPath + "/history/deleteConductedExam",
                data: $(form).serialize(),
                type: "POST",
                success: function(data) {
                    console.log("********* AJAX CALL *********");
                    console.log("Status: " + data.status);
                    console.log("Result: " + data.result);

                    var conductedExamId = $("#confirmDeleteConductedExam").find('.modal-body #conductedExamReference').val();
                    var conductedExamsTable = $('#conducted_exams_table');

                    var conductedExamRow = conductedExamsTable.find('#' + conductedExamId);

                    // returns DataTables API instance with selected row in the result set
                    var conductedExamRowDT = conductedExamsTable.DataTable().row(conductedExamRow);
                    conductedExamRowDT.remove().draw();

                    $("#confirmDeleteConductedExam").modal('hide');
                }
            });
        }
    });

    $('#confirmDeleteConductedExam').on('show.bs.modal', function (event) {
        console.log("$('#confirmDeleteConductedExam').on('show.bs.modal')");

        var button = $(event.relatedTarget);
        var conductedExamId = button.data('conductedexam-reference');
        var message = button.data('message');

        $(this).find('.modal-body #conductedExamReference').val(conductedExamId);
        $(this).find('.modal-body #confirmDeleteConductedExamLabel').text(message);
    });

    $('#confirmDeleteConductedExam .modal-footer #confirmDeleteConductedExamBtnModal').on('click', function(){
        console.log("$('#confirmDeleteConductedExam .modal-footer #confirmDeleteConductedExamBtnModal').on('click')");

        $('#deleteConductedExamFormModal').submit();
    });



</script>

<%@include file="partials/footer.jsp"%>