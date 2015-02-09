<!DOCTYPE html>
<html lang="en">
<head>

<title>DBPedia Artist Finder</title>

<!-- Bootstrap core CSS -->
<link href="resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<!-- Bootstrap theme -->
<link href="resources/bootstrap/css/bootstrap-theme.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="resources/stylesheet/main.css" rel="stylesheet">

</head>

<body>


	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container-fluid">
			<div>
				<ul class="nav navbar-nav  pull-left">

					<li><a class="navbar-brand" href="./">DBPedia Artist Finder</a></li>
					<li><a href="#">Page 1</a></li>
				</ul>
				<ul class="nav navbar-nav  pull-right">
					<li><a href="https://github.com/sergepeter" target="_blank">Sources hosted on Github</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container theme-showcase" role="main">

		<div class="page-header">
			<h1>Search</h1>
		</div>


	
			
<div class="input-group">
      <input type="text" class="form-control" placeholder="Search for...">
      <span class="input-group-btn">
        <button class="btn btn-default" type="button">Go!</button>
      </span>
    </div><!-- /input-group -->

	

		<div class="page-header">
			<h1>Results</h1>
		</div>


		<div class="col-md-6">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<th>#</th>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Username</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>1</td>
						<td>Mark</td>
						<td>Otto</td>
						<td>@mdo</td>
					</tr>
					<tr>
						<td>2</td>
						<td>Jacob</td>
						<td>Thornton</td>
						<td>@fat</td>
					</tr>
					<tr>
						<td>3</td>
						<td>Larry</td>
						<td>the Bird</td>
						<td>@twitter</td>
					</tr>
				</tbody>
			</table>
		</div>


		<footer class="footer">
			<p>&copy; Serge Peter 2015</p>
		</footer>

	</div>



	<script src="resources/js/lib/jquery.js"></script>
	<script src="resources/bootstrap/js/bootstrap.js"></script>

</body>


</html>
