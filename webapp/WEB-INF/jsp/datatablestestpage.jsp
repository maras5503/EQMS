<%@ include file="partials/header.jsp" %>

<div class="page-header" align="center" style="margin-top: 10px;">	            
    <h1><spring:message code="test.message" text="Hello World" /></h1>
</div>

Language : <a href="?locale=en">English</a>|<a href="?locale=pl">Polish</a>

<br>

Current Locale : ${pageContext.response.locale}

<br><br><br>

<img src="<c:url value="/flags/poland_32x18.png" />">
<img src="<c:url value="/flags/usa_32x17.png" />">
<img src="<c:url value="/flags/poland_52x32.png" />" style="border:1px solid black;">
<img src="<c:url value="/flags/usa_52x32.png" />" style="border:1px solid black;">

<br><br><br>

<div class="row">
    <div class="col-sm-12">
    	
    	<div class="table-responsive" style="margin-bottom: 0px;">
			<table class="table table-bordered" id=tests_table> <!-- <table id="example" class="display nowrap" cellspacing="0" width="100%"> -->
			    <thead>
			       <tr class="success">
						<th>NAME</th>
						<th>GROUPS</th>
						<th>QUESTIONS</th>
						<th>TIME</th>
						<th>CREATION DATE</th>
						<th>CREATED BY</th>
						<th>MODIFICATION DATE</th>
						<th>MODIFIED BY</th>
						<th></th>
						<th></th>
						<th></th>
					</tr>
			    </thead>
			    <tbody>
			      	<c:forEach var="test" items="${tests}">
						<tr class="success" data-key="${test.testId}">
							<td>${test.testName}</td>
							<td>${test.numberOfGroups}</td>
							<td>${test.numberOfQuestions}</td>
							<td>${test.timeForTest}</td>
							<td>${test.creationDate}</td>
							<td data-toggle="tooltip" data-placement="left" title="Tooltip on left">${test.createdBy}</td>
							<td>${test.modificationDate}</td>
							<td>${test.modifiedBy}</td>
							<td>
								<a href="#" class="btn btn-info btn-block btn-sm" role="button"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit</a>
							</td>
							<td>
								<a href="#" class="btn btn-danger btn-block btn-sm" role="button"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Remove</a>
							</td>
							<td class="details-control"></td>
						</tr>
					</c:forEach>
			    </tbody>
			</table>
		</div>
		
	</div>
</div>
			

<script type="text/javascript">
	function format ( key ) {
		console.log(key.toString());
		var str = key.toString();
		console.log(str);
		
		var htmls = '<div class="slider">'+
    	'<table class=\"table table-bordered warning\" style=\"margin-bottom: 0px\">'+
			'<thead>'+
    			'<tr class="warning">'+
    				'<th>GROUP NAME</th>'+
    				'<th>NUMBER OF QUESTIONS</th>'+
    				'<th></th>'+
    				'<th></th>'+
    			'</tr>'+
			'</thead>'+
			'<tbody>'+
				'<c:forEach var="group" items="${groups}">'+
					'<c:if test="${\'' + key.toString() +  '\' == group.getTests().getTestId().toString()}">'+
						'<tr class="warning">'+
							'<td>${group.groupName}</td>'+
							'<td>${group.numberOfQuestions}</td>'+
							'<td>'+
							'<a href="#" class="btn btn-info btn-block btn-sm" role="button"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit</a>'+
							'</td>'+
							'<td>'+
								'<a href="#" class="btn btn-info btn-block btn-sm" role="button"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span> Enable</a>'+
							'</td>'+
							'<td>'+
								'<a href="#" class="btn btn-danger btn-block btn-sm" role="button"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Remove</a>'+
							'</td>'+
						'</tr>'+
					'</c:if>'+
				'</c:forEach>'+
			'</tbody>'+
			'</table>'+
			'</div>';

		
		    			
		    console.log(htmls);			
			    
	    return htmls; 
	}
	
	$(function () {

      var table = $('#tests_table').DataTable({});

      // Add event listener for opening and closing details
      $('#tests_table').on('click', 'td.details-control', function () {
          var tr = $(this).closest('tr');
          var row = table.row(tr);

          if (row.child.isShown()) {
              // This row is already open - close it
        	  $('div.slider', row.child()).slideUp( function () {
                  row.child.hide();
                  tr.removeClass('shown');
              } );
          } else {
              // Open this row
              var key = tr.data('key');
              /*var list = ${groups};

              list.forEach(function(entry) {
            	    console.log(entry);
              });*/
              
              //console.log("row.data() = " + row.data());
              console.log(key);

              row.child( format( key ) ).show();
              tr.addClass('shown');

              $('div.slider', row.child()).slideDown();
          }
      });
  });
</script>


<%@ include file="partials/footer.jsp" %>