		<br>
		<br>
		
		<footer>
        	<!--<p align="center">&copy; 2015-2016 Adrian Wojtasik - Praca dyplomowa</p>-->
			<p align="center">&copy; 2020-2021 Marek Tomczak - Praca dyplomowa</p>
      	</footer>
	
	</div>
	
	<script type="text/javascript">
		var url = window.location;
		// Will only work if string in href matches with location
		$('ul.nav a[href="'+ url +'"]').parent().addClass('active');
	
		// Will also work for relative and absolute hrefs
		$('ul.nav a').filter(function() {
		    return this.href == url;
		}).parent().addClass('active');
	</script>
	
</body>
</html>

