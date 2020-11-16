<%@ include file="partials/header.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
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
            </ol>

            <!-- Questions and answers -->

            <form action="<c:url value="/exam/processExam"/>" id="questionForm" method="POST">

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
                                    <input type="radio" id="answerReference" name="answer" value="${a.answerId}" > ${a.contentOfAnswer}
                                </label>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <div hidden="hidden" id="submitDiv"><button class="btn btn-success btn-lg btn-block" id="btnSubmit">Submit</button></div>
            </form>
            <div class="pull-left" style="width: 50%">
                <form id="previousQuestionForm" action="<c:url value="/exam/previousQuestion"/>" method="POST">
                    <input type="hidden" name="previousQuestionReference" id="previousQuestionReference" value="0"/>
                    <input type="hidden" name="groupReference" id="groupReference" value="${currentGroupModel.groupId}"/>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <button type="submit" class="btn btn-link btn-lg btn-block" id="btnPrevious" disabled="true" >Previous</button>
                </form>
            </div>
            <div class="pull-left" style="width: 50%">
            <form id="nextQuestionForm" action="<c:url value="/exam/nextQuestion"/>" method="POST" >
                <input type="hidden" name="nextQuestionReference" id="nextQuestionReference" value="0"/>
                <input type="hidden" name="groupReference" id="groupReference" value="${currentGroupModel.groupId}"/>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <button type="submit" class="btn btn-primary btn-lg btn-block" id="btnNext">Next</button>
            </form>
            </div>


        </div>
    </div>
</div>

<script type="text/javascript">

    var submit=true;

    $('#nextQuestionForm').on('submit', function(event) {
        if(submit === false) {
            event.preventDefault(); // if you want to disable the action
            return false;
        } else {
            return true;
        }
    });

    var validatorNextQuestion = $('#nextQuestionForm').validate({
        submitHandler: function(form) {
            console.log("********* submitHandler *********");
            $(form).ajaxSubmit({
                url: URLWithContextPath + "/exam/nextQuestion",
                dataType: "json",
                type: "post",
                success: function(data) {
                    console.log("********* AJAX CALL *********");
                    console.log("Status: " + data.status);
                    console.log("Result: " + data.result);
                    $("#questiontext").html(data.result.contentOfQuestion);
                    $("#nextQuestionReference").attr("value",data.result.questionReference);
                    $("#previousQuestionReference").attr("value",data.result.questionReference);
                    $("#answersloop").html(data.result.resultsuccess);
                    $("#btnPrevious").attr("disabled",false);
                    $("#questionImageDiv").html(data.result.image);

                    if(data.result.isQuestionLast){
                        $("#btnNext").attr("disabled",true);
                        $("#submitDiv").show();
                    }

                }
            });
        }
    });

    $('#previousQuestionForm').on('submit', function(event) {
        if(submit === false) {
            event.preventDefault(); // if you want to disable the action
            return false;
        } else {
            return true;
        }
    });

    var validatorPreviousQuestion = $('#previousQuestionForm').validate({
        submitHandler: function(form) {
            console.log("********* submitHandler *********");
            $(form).ajaxSubmit({
                url: URLWithContextPath + "/exam/previousQuestion",
                dataType: "json",
                type: "post",
                success: function(data) {
                    console.log("********* AJAX CALL *********");
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
                    $("#submitDiv").hide();

                }
            });
        }
    });
</script>



<%@include file="partials/footer.jsp"%>
