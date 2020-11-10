<%@include file="partials/header.jsp"%>
<c:choose>
    <c:when test="${isTestEnabledModel eq false}">
    <div class="well">
        Test is not enabled
    </div>
    </c:when>
    <c:otherwise>
        <div class="page-header" align="center" style="margin-top: 10px;">
            <h1>Take the test</h1>
        </div>
        <div class="col-sm-12 well">
            <div class="col-sm-12">
                <label for="testName" class="col-sm-3 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Test name:</label>
                <label class="col-sm-5 control-label" id="testName" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${currentTestModel.testName}</label>
                <div class="col-sm-4" style="text-align: right;">
                    <form action="<c:url value="/exam/startExam"/>" method="POST" id="startExamForm">
                        <input type="hidden" name="testReference" id="testReference" value="${currentTestModel.testId}"/>
                        <input type="hidden" name="groupReference" id="groupReference" value="${currentGroupModel.groupId}"/>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <button type="submit" class="btn btn-success" id="startExamBtn" style="margin-right:5px; margin-top: 15px;"><span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span> Start the test</button>
                    </form>
                </div>
                <label for="timeForTest" class="col-md-3 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Time for test:</label>
                <label class="col-sm-9 control-label" id="timeForTest" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${currentTestModel.timeForTest} min</label>

                <label for="numberOfQuestions" class="col-md-3 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Number of questions:</label>
                <label class="col-sm-9 control-label" id="numberOfQuestions" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${currentTestModel.numberOfQuestions}</label>


            </div>
        </div>

    </c:otherwise>

</c:choose>

<script type="text/javascript">

    $('#startExamBtn').on('click', function() {
        console.log("$('#startExamBtn').on('click')");
    });

    $('#startExamForm').on('submit', function(event) {
        if(submit === false) {
            event.preventDefault(); // if you want to disable the action
            return false;
        } else {
            return true;
        }
    });

    var validatorStartExam = $('#startExamForm').validate({

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
<%@include file="partials/footer.jsp"%>
