<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>

<title>DBPedia Artist Finder</title>

<!-- Bootstrap core CSS -->
<link href="resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<!-- Bootstrap theme -->
<link href="resources/bootstrap/css/bootstrap-theme.css"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link href="resources/stylesheet/main.css" rel="stylesheet">

</head>

<body ng-controller="artistSearchController">

	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container-fluid">
			<div>
				<ul class="nav navbar-nav  pull-left">
					<li class="active"><a class="navbar-brand" href="./">DBPedia
							Artist Finder</a></li>
					<li><a href="#">Page 1</a></li>
				</ul>
				<ul class="nav navbar-nav  pull-right">
					<li><a href="https://github.com/sergepeter/artistfinder"
						target="_blank">Sources hosted on Github</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container theme-showcase" role="main">

		<div class="page-header">
			<h1>Search</h1>
		</div>

		<div class="input-group">
			<input type="text" class="form-control" ng-model="searchName"
				placeholder="Search for..."> <span class="input-group-btn">
				<button class="btn btn-default" type="submit"
					ng-click="searchArtist(searchName)">Go!</button>
			</span>
		</div>
		<!-- /input-group -->

		<div class="page-header">

			<div class="alert alert-info" role="alert" ng-show="!artists.length">No
				result yet (Search for : "{{searchName}}").</div>

			<h1 ng-show="artists.length">Results</h1>

			<table class="table table-striped table-bordered"
				ng-show="artists.length">
				<thead>
					<tr>
						<th>Name</th>
						<th>Description</th>
						<th>Birthdate</th>
						<th>Active Year</th>
						<th>Web Site</th>
						<th>More details</th>
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="artist in artists">
						<td>{{ artist.name }}</td>
						<td>{{ artist.shortDescription }}</td>
						<td>{{ artist.birthDate }}</td>
						<td>{{ artist.yearsActive }}</td>
						<td><a href="{{ artist.website }}" />{{ artist.website }}</a></td>
						<td><button class="btn btn-default" type="button"
								ng-click="moreInfo(artist.artistURI)">More info</button></td>
					</tr>
				</tbody>
			</table>


			<h1 ng-show="artist">Detailed info</h1>

			<table class="table table-striped table-bordered" ng-show="artist">
				<tr>
					<th>Name</th>
					<td>{{artist.name}}</td>
				</tr>
				<tr>
					<th></th>
					<td>{{artist.birthDate}}</td>
				</tr>
				<tr>
					<th>Birth Date</th>
					<td>{{artist.deathDate}}</td>
				</tr>
				<tr>
					<th>Image</th>
					<td>{{artist.imageURL}}</td>
				</tr>
				<tr>
					<th>Abstract</th>
					<td>{{artist.abstractStr}}</td>
				</tr>
				<tr>
					<th>Year Active</th>
					<td>{{artist.yearsActive}}</td>
				</tr>
				<tr>
					<th>Web Site</th>
					<td>{{artist.website}}</td>
				</tr>
				<tr>
					<th>Birth Place URI</th>
					<td>{{artist.birthPlaceURI}}</td>
				</tr>
				<tr>
					<th>Artist URI</th>
					<td>{{artist.artistURI}}</td>
				</tr>

			</table>
		</div>

		<footer class="footer">
			<p>&copy; Serge Peter 2015</p>
		</footer>

	</div>

	<script type="text/javascript" src="resources/js/lib/jquery.js"></script>
	<script type="text/javascript" src="resources/js/lib/angular.min.js"></script>
	<script type="text/javascript"
		src="resources/bootstrap/js/bootstrap.min.js"></script>
	<script src="resources/js/angular/myapp.js"></script>

</body>


</html>
