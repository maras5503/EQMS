<%@ include file="partials/header.jsp" %>

<div class="row">

    <div class="col-sm-12">

        <c:choose>
            <c:when test="${not empty allStudentsModel}">

                <div class="page-header" style="margin-top: 10px;">
                    <div class="col-sm-offset-3 col-sm-6" align="center">
                        <h1>Students and passwords</h1>
                    </div>
                    <div class="pull-right">
                        <button type="button" class="btn btn-warning" id="sendEmailBtn" data-toggle="modal" data-target="#confirmSendEmails" data-title="Send passwords via e-mail" data-message="Do you want to send passwords for students via e-mail?" data-passwords-reference="${passwordsModel}" data-studentgroup-reference="${studentGroupIdModel}" data-test-reference="${testModel}" style="margin-right:5px "><span class="glyphicon glyphicon-envelope" aria-hidden="true"></span> Send passwords via e-mail</button>
                    </div>
                    <div></div>
                    <div class="pull-right">
                    <form action="<c:url value="tests/printToPdf"/>" method="POST" id="printToPdfForm">
                        <input type="hidden" name="testReference" id="testReference" value="${testModel}"/>
                        <input type="hidden" name="studentGroupReference" id="studentGroupReference" value="${studentGroupIdModel}"/>
                        <input type="hidden" name="passwordsReference" id="passwordsReference" value="${passwordsModel}"/>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <button type="submit" class="btn btn-danger" id="printToPdfBtn" style="margin-right:5px; margin-top: 15px;"><span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span> Export to PDF</button>
                    </form>
                    </div>
                    <div class="clearfix"></div>



                </div>
                <div class="table-responsive" style="margin-bottom: 0px">
                    <table class="table table-bordered" id="students_passwords_table">
                        <thead>
                        <tr class="success">
                            <th>FIRSTNAME</th>
                            <th>LASTNAME</th>
                            <th>E-MAIL</th>
                            <th>PASSWORD</th>
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach varStatus="loop" var="student" items="${allStudentsModel}">
                            <tr class="success" id="${student.studentId}">
                                <td><c:out value="${student.studentFirstname}" /></td>
                                <td><c:out value="${student.studentLastname}" /></td>
                                <td><c:out value="${student.studentEmail}" /></td>
                                <td><c:out value="${passwordsModel.get(loop.index)}"/></td>
                            </tr>

                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="well">
                    There are no students in this group.
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="modal fade" id="confirmSendEmails" tabindex="-1" role="dialog" aria-labelledby="confirmSendEmailsLabelModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="confirmSendEmailsTitleModal">Send Passwords via E-mail</h4>
            </div>
            <div class="modal-body">
                <form action="<c:url value="/tests/doSendEmails"/>" method="POST" id="confirmSendEmailsFormModal" >
                    <div class="form-group" id="confirmSendEmailsLabel">
                    </div>
                    <input type="hidden" name="testReference" id="testReference"/>
                    <input type="hidden" name="groupReference" id="groupReference"/>
                    <input type="hidden" name="studentGroupReference" id="studentGroupReference"/>
                    <input type="hidden" name="passwordsReference" id="passwordsReference"/>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-warning" id="confirmSendEmailsBtnModal">Send</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    var validatorConfirmSendEmails = $('#confirmSendEmailsFormModal').validate({
        submitHandler: function(form) {
            console.log("********* submitHandler *********");

            $.ajax({
                url: URLWithContextPath + "/tests/doSendEmails",
                data: $(form).serialize(),
                type: "POST",
                success: function(data) {
                    console.log("********* AJAX CALL *********");
                    console.log("Status: " + data.status);


                    $("#confirmSendEmails").modal('hide');
                    alert("Passwords have been sent to students e-mail adresses succesfully");

                }
            });
        }
    });

    $('#confirmSendEmails').on('show.bs.modal', function (event) {
        console.log("$('#confirmSendEmails').on('show.bs.modal')");

        var button = $(event.relatedTarget);
        var studentgroupId = button.data('studentgroup-reference');
        var testId = button.data('test-reference');
        var passwords = button.data('passwords-reference')
        var message = button.data('message');

        $(this).find('.modal-body #studentGroupReference').val(studentgroupId);
        $(this).find('.modal-body #testReference').val(testId);
        $(this).find('.modal-body #passwordsReference').val(passwords);
        $(this).find('.modal-body #confirmSendEmailsLabel').text(message);
    });

    $('#confirmSendEmails .modal-footer #confirmSendEmailsBtnModal').on('click', function(){
        console.log("$('#confirmSendEmails .modal-footer #confirmSendEmails').on('click')");

        $('#confirmSendEmailsFormModal').submit();
    });

    $('#confirmSendEmails').on('hide.bs.modal', function (event) {
        console.log("$('#confirmSendEmails').on('hide.bs.modal')");
    });

    var submit=true;

    $('#printToPdfBtn').on('click', function() {
        console.log("$('#printToPdfBtn').on('click')");
    });

    $('#printToPdfForm').on('submit', function(event) {
        if(submit === false) {
            event.preventDefault(); // if you want to disable the action
            return false;
        } else {
            return true;
        }
    });

    var validatorPrintToPdf = $('#printToPdfForm').validate({

        submitHandler: function(form) {
            console.log("********* submitHandler *********");

            $(form).ajaxSubmit({
                url: URLWithContextPath + "/tests/printToPdf",
                dataType: "json",
                success: function(data) {
                    console.log("********* AJAX CALL *********");
                    console.log("Status: " + data.status);
                    console.log("Result: " + data.result);

                }
            });
        }
    });

</script>
<%@ include file="partials/footer.jsp" %>