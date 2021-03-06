<%@ include file="partials/header.jsp"%>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Exam</title>

</head>


<body id="page-top">




<div id="wrapper">


    <div id="content-wrapper">

        <div class="container-fluid">

            <!-- Breadcrumbs-->
            <ol class="breadcrumb">
                <li class="breadcrumb-item">
                    <a href="#">${currentStudentModel.studentFirstname} ${currentStudentModel.studentLastname}</a>
                </li>
                <li class="breadcrumb-item active"><c:out value='${currentTestModel.testName}' /></li>
                <div class="pull-right">
                    <div id=time class="pull-right"></div>
                </div>
            </ol>

            <!-- Questions and answers -->
            <form id="QuestionForm"  method="POST">

                <div class="qList">
                    <div align="center" id="questionImageDiv">
                        ${image} <br><br>
                    </div>

                    <div class="jumbotron">
                        <h1 class="display-4" id="questiontext">${question.contentOfQuestion}</h1>
                        <hr class="my-4">
                        <div id="answersloop">
                            <c:forEach var="a" items="${answersModel}">
                                <label class="container">
                                    <input type="checkbox" id="answerReference" name="answer" value="${a.answerId}"> ${a.contentOfAnswer}
                                </label>
                                <c:if test="${a.pictures.pictureId != null}">
                                <img src="${URLwithContextPath}/tests/image/${a.pictures.pictureId}" alt="questionImage" name="questionImage" id="questionImage"/></label><br><br>
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </div>


                <input type="hidden" name="previousQuestionReference" id="previousQuestionReference" value="0"/>
                <input type="hidden" name="nextQuestionReference" id="nextQuestionReference" value="0"/>
                <input type="hidden" name="groupReference" id="groupReference" value="${currentGroupModel.groupId}"/>
                <input type="hidden" name="questionIDsReference" id="questionIDsReference" value="${questionIDsModel}"/>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                <div class="pull-left" style="width: 50%">
                    <input type="submit" class="btn btn-link btn-lg btn-block" id="btnPrevious" name="btnPrevious" disabled="true" value="Previous" />
                </div>

                <div class="pull-left" style="width: 50%">
                    <input type="submit" class="btn btn-primary btn-lg btn-block" id="btnNext" name="btnNext" value="Next"/>
                </div>
                <div id="finishDiv">
                    <input type="submit" class="btn btn-danger btn-lg btn-block" style="margin-top: 10px; display:inline-block"  id="btnFinish" name="btnFinish" value="Finish exam" data-toggle="modal" data-target="#confirmFinishExam"/>
                </div>

                </form>

            <form id="saveTimeForm" method="POST" action="<c:url value="/exam/saveTime"/>">
                <input type="hidden" name="groupReference" id="groupReference" value="${currentGroupModel.groupId}"/>
                <input type="hidden" name="timeReference" id="timeReference"/>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>




        </div>
    </div>
</div>

<div class="modal fade" id="confirmFinishExam" tabindex="-1" role="dialog" aria-labelledby="confirmFinishExamLabelModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="confirmFinishExamTitleModal">Finish exam</h4>
            </div>
            <div class="modal-body">
                <div class="form-group" id="confirmFinishExamLabel">
                    Are you sure you want to finish the exam?
                </div>
                <form action="<c:url value="/exam/finishExam"/>" method="get" id="finishExamForm">
                    <input type="hidden" name="groupReference" id="groupReference" value="${currentGroupModel.groupId}"/>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-danger" id="confirmFinishExamBtnModal">Finish</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    /* jQuery(window).bind('beforeunload', function(e) {
        var message = "Why are you leaving?";
        e.returnValue = message;
        return message;
    });

    window.onunload = function () {
        $("#timeReference").attr("value", timer2);
        $("#saveTimeForm").submit();
    } */

    $("#btnNext").click(function(){
        $("#QuestionForm").attr("action", "<c:url value="/exam/nextQuestion"/>");
    });
    $("#btnPrevious").click(function(){
        $("#QuestionForm").attr("action", "<c:url value="/exam/previousQuestion"/>");
    });
    $("#confirmFinishExamBtnModal").click(function () {
        $("#QuestionForm").attr("action", "<c:url value="/exam/saveLastAnswer"/>");
        $("#QuestionForm").submit();

    });

    var submit=true;

    $('#QuestionForm').on('submit', function(event) {
        if(submit === false) {
            event.preventDefault(); // if you want to disable the action
            return false;
        } else {
            return true;
        }
    });


    var validatorQuestion = $('#QuestionForm').validate({
        submitHandler: function(form) {
            console.log("********* submitHandler *********");
            if ($("#QuestionForm").attr("action") === "<c:url value="/exam/nextQuestion"/>") {
                $(form).ajaxSubmit({
                    url: URLWithContextPath + "/exam/nextQuestion",
                    dataType: "json",
                    type: "post",
                    success: function (data) {
                        console.log("********* AJAX CALL 1*********");
                        console.log("Status: " + data.status);
                        console.log("Result: " + data.result);
                        $("#questiontext").html(data.result.contentOfQuestion);
                        $("#nextQuestionReference").attr("value", data.result.questionReference);
                        $("#previousQuestionReference").attr("value", data.result.questionReference);
                        $("#answersloop").html(data.result.resultsuccess);
                        $("#btnPrevious").attr("disabled", false);
                        $("#questionImageDiv").html(data.result.image);

                        if (data.result.isQuestionLast) {
                            $("#btnNext").attr("disabled", true);
                        }

                    }
                });
            }
            if ($("#QuestionForm").attr("action") === "<c:url value="/exam/previousQuestion"/>") {

                $(form).ajaxSubmit({
                    url: URLWithContextPath + "/exam/previousQuestion",
                    dataType: "json",
                    type: "post",
                    success: function (data) {
                        console.log("********* AJAX CALL 2*********");
                        console.log("Status: " + data.status);
                        console.log("Result: " + data.result);
                        $("#questiontext").html(data.result.question);
                        $("#nextQuestionReference").attr("value", data.result.questionReference);
                        $("#previousQuestionReference").attr("value", data.result.questionReference);
                        $("#answersloop").html(data.result.resultsuccess);
                        $("#questionImageDiv").html(data.result.image);

                        if (data.result.isQuestionFirst) {
                            $("#btnPrevious").attr("disabled", true);
                        }

                        if ($("#btnNext").prop("disabled")) {
                            $("#btnNext").attr("disabled", false);
                        }

                    }
                });
            }
            if ($("#QuestionForm").attr("action") === "<c:url value="/exam/saveLastAnswer"/>") {

                $(form).ajaxSubmit({
                    url: URLWithContextPath + "/exam/saveLastAnswer",
                    dataType: "json",
                    type: "post",
                    success: function (data) {
                        console.log("********* AJAX CALL 3*********");
                        console.log("Status: " + data.status);
                        console.log("Result: " + data.result);
                        $("#finishExamForm").submit();
                    }
                });
            }
        }
    });



    var time1="${time}";
    var time2=time1.split(':');
    var hours = parseInt(time2[0], 10);
    var minutes = parseInt(time2[1], 10);
    var seconds = parseInt(time2[2], 10);

    var finishDate=new Date();
    finishDate.setHours(hours);
    finishDate.setMinutes(minutes);//January has index 0!!!
    finishDate.setSeconds(seconds);

    var timeField =$("#time");

    var t=setInterval(loop,1000); //(handler,intervall in ms)
    function loop(){
        //get current Date (now)
        var now=new Date();
        //get difference
        var diff=finishDate - now;
        //if the time has come ;-)
        if(diff<=0){
            clearInterval(t);
            $("#QuestionForm").attr("action", "<c:url value="/exam/saveLastAnswer"/>");
            $("#QuestionForm").submit();
            return false;
        }
        //if not
        var str= "Time left: "+parseMs(diff);
        timeField.text(str);
    }

    function parseMs(ms){
        var hours = Math.floor(ms/(3600000))
        ms = ms % 3600000;
        var minutes = Math.floor(ms / 60000 );
        ms = ms % 60000;
        var seconds = Math.floor(ms / 1000);
        ms = ms % 1000;

        var hoursStr = checkForZero(hours);
        var minutesStr = checkForZero(minutes);
        var secondStr = checkForZero(seconds);

        var timeString = hoursStr + ":" + minutesStr + ":" + secondStr;
        return timeString;
    }

    function checkForZero(i){
        //write "0" before ?
        str=i+"";
        if (parseInt(i,10) < 10) str = "0" + str;
        return str;
    }






</script>



<%@include file="partials/footer.jsp"%>
