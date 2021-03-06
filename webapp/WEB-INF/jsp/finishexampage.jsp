<%@include file="partials/header.jsp"%>

<div class="page-header" align="center" style="margin-top: 10px;">
    <h1>Exam result</h1>
</div>
<div class="col-sm-12 well">
    <div class="col-sm-12">
        <label for="score" class="col-sm-3 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Score:</label>
        <label class="col-sm-5 control-label" id="score" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${result}</label>
        <br><br>
        <label for="numberOfQuestions" class="col-md-3 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Max. score:</label>
        <label class="col-sm-9 control-label" id="numberOfQuestions" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${group.numberOfQuestions}</label>
        <br><br>
        <label for="percentageResult" class="col-md-3 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Percentage result:</label>
        <label class="col-sm-9 control-label" id="percentageResult" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${percentageResult} %</label>
        <br><br>
        <label for="mark" class="col-md-3 control-label" style="padding-top: 7px; margin-bottom: 0px; text-align: right;">Mark:</label>
        <label class="col-sm-9 control-label" id="mark" style="padding-top: 7px; margin-bottom: 0px; text-align: left;">${mark}</label>

    </div>
</div>

<%@include file="partials/footer.jsp"%>