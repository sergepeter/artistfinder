<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>

	<title>DBPedia Artist Finder</title>
	
	<!-- Bootstrap core CSS -->
	<link href="resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<!-- Bootstrap theme -->
	<link href="resources/bootstrap/css/bootstrap-theme.css" rel="stylesheet">
	
	<!-- Custom styles for this template -->
	<link href="resources/stylesheet/main.css" rel="stylesheet">
	
	<script type="text/javascript" src="resources/js/lib/jquery.js"></script>
	<script type="text/javascript" src="resources/js/lib/angular.min.js"></script>
	<script type="text/javascript" src="resources/bootstrap/js/bootstrap.min.js"></script>
	
	<script src="resources/js/angular/myapp.js"></script>

</head>

<body ng-controller="artistSearchController">

	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container-fluid">
			<div>
				<ul class="nav navbar-nav  pull-left">
					<li class="active"><a class="navbar-brand" href="./">DBPedia Artist Finder</a></li>
					<li ng-repeat="page in pages">
							<a href="" ng-click="treatePage(page.type, page.what)">{{page.label}}  
							<span ng=show="page.length" class='badge'>{{page.length}}</span></a>
					</li>
				</ul>
				<ul class="nav navbar-nav  pull-right">
					<li><a href="https://github.com/sergepeter/artistfinder" target="_blank">Sources hosted on Github</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container theme-showcase" role="main">
		<div ng-if="(page.type == 'searchArtist')">

			<div class="page-header">
				<h1>Search</h1>
			</div>
			
			<div class="input-group">
				<input type="text" class="form-control" ng-model="searchName" placeholder="Search for..."> <span class="input-group-btn">
					<button class="btn btn-default" type="submit" ng-click="searchArtist(searchName)">Go!</button>
				</span>
			</div>
			<!-- /input-group -->
			
			<div class="page-header">
				<div class="alert alert-info" role="alert" ng-show="!artists.length">No result yet (Search for : "{{searchName}}").</div>
			
				<h1 ng-show="artists.length">Artist Results</h1>
			
				<table class="table table-striped table-bordered" ng-show="artists.length">
					<thead>
						<tr>
							<th width="200">Name</th>
							<th>Description</th>
							<th>Active Year</th>
							<th>Web Site</th>
							<th>More details</th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="artist in artists">
							<td>{{ artist.name }} (<a ng-href="{{artist.artistURI}}">URI</a>)</td>
							<td>{{ artist.shortDescription }}</td>
							<td>{{ artist.yearsActive }}</td>
							<td><a ng-href="{{ artist.website }}" target="_blank" />{{ artist.website }}</a></td>
							<td><button class="btn btn-default" type="button" ng-click="moreInfo(artist.artistURI)">More info</button></td>
						</tr>
					</tbody>
				</table>
				
				<h1 ng-show="bands.length">Band Results</h1>
			
				<table class="table table-striped table-bordered" ng-show="bands.length">
					<thead>
						<tr>
							<th width="200">Name</th>
							<th widht="500">Description</th>
							<th>Active Year</th>
							<th>More details</th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="band in bands">
							<td>{{ band.name }} (<a ng-href="{{band.bandURI}}">URI</a>)</td>
							<td>{{ band.description }}</td>
							<td>{{ band.activeYearsStartYear }}</td>
							<td><button class="btn btn-default" type="button" ng-click="bandInfo(band.bandURI)">More info</button></td>
						</tr>
					</tbody>
				</table>
				<div class="alert alert-info" role="alert" ng-show="loading">
					<span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span> Loading...
				</div>
		
			</div>


		</div>

		<div ng-if="(page.type == 'artist')">

			<div class="page-header">

				<h1 ng-show="artist">Artist Detailed Info</h1>

				<table class="table table-striped table-bordered" ng-show="artist">
					<tr>
						<th width=200>Name</th>
						<td>{{artist.name}} (<a ng-href="{{artist.artistURI}}" target="_blank">URI</a>)</td>
					</tr>
					<tr>
						<th width=200>Short Description</th>
						<td>{{artist.shortDescription}}</td>
					</tr>
					<tr>
						<th>Birth Date</th>
						<td>{{ artist.birthDate }}</td>
					</tr>

					<tr>
						<th>Birth Place</th>
						<td>{{artist.birthPlaceStr}} ({{artist.country}})
						</td>
					</tr>

					<tr ng-show="artist.deathDate">
						<th>Death Date</th>
						<td>{{artist.deathDate}}</td>
					</tr>

					<tr ng-show="artist.deathDate">
						<th>Death Place</th>
						<td>{{artist.deathPlaceStr}}
						</td>
					</tr>
					<tr ng-show="artist.imageURL">
						<th>Image</th>
						<td><img ng-src="{{artist.imageURL}}" width=300 /></td>
					</tr>
					<tr>
						<th>Abstract</th>
						<td>{{artist.abstractStr}}</td>
					</tr>
					<tr>
						<th>Year Active</th>
						<td>{{artist.yearsActive}}</td>
					</tr>
					<tr ng-show="artist.website">
						<th>Web Site</th>
						<td><a ng-href="{{artist.website}}" target="_blank">{{artist.website}}</a></td>
					</tr>
					<tr ng-show="artist.genres">
						<th>Genres</th>
						<td>
							<ul style="list-style-type: disc">
								<li ng-repeat="genre in artist.genres">{{genre}}</li>
							</ul>
						</td>
					</tr>

					<tr ng-show="artist.instruments">
						<th>Instruments</th>
						<td>
							<ul style="list-style-type: disc">
								<li ng-repeat="intrument in artist.instruments">{{intrument}}</li>
							</ul>
						</td>
					</tr>
					<tr ng-show="artist.associatedArtists">
						<th>Associated Artists</th>
						<td>
							<ul style="list-style-type: disc">
								<li ng-repeat="associatedArtist in artist.associatedArtists"><a href="" ng-click="treatePage('artist', associatedArtist.artistURI)">{{associatedArtist.name}}</a> (<a ng-href="{{associatedArtist.artistURI}}"  target="_blank">URI</a>)</li>
							</ul>
						</td>
					</tr>
					<tr ng-show="artist.associatedBands">
						<th>Associated Bands</th>
						<td>
							<ul style="list-style-type: disc">
								<li ng-repeat="associatedBand in artist.associatedBands"><a href="" ng-click="treatePage('band', associatedBand.bandURI)">{{associatedBand.name}}</a> (<a ng-href="{{associatedBand.bandURI}}"  target="_blank">URI</a>)</li>
							</ul>
						</td>
					</tr>
				</table>
				
				<div class="alert alert-info" role="alert" ng-show="loading"><span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span> Loading...</div>
				
			</div>

		</div>


	<div ng-if="(page.type == 'band')">

			<div class="page-header">

				<h1 ng-show="band">Band Detailed Info</h1>

				<table class="table table-striped table-bordered" ng-show="band">
					<tr>
						<th width=200>Name</th>
						<td>{{band.name}} (<a ng-href="{{band.bandURI}}" target="_blank">URI</a>)
						</td>
					</tr>
					<tr>
						<th width=200>Full Description</th>
						<td>{{band.abstractStr}}</td>
					</tr>
					
					<tr>
						<th width=200>Home Town</th>
						<td>{{band.hometown}} ({{band.country}})</td>
					</tr>
					
					<tr ng-show="band.imageURL">
						<th>Image</th>
						<td><img ng-src="{{band.imageURL}}" width=300 /></td>
					</tr>
				
					<tr ng-show="band.associatedArtists">
						<th>Associated Artists</th>
						<td>
							<ul style="list-style-type: disc">
								<li ng-repeat="associatedArtist in band.associatedArtists"><a href="" ng-click="treatePage('artist', associatedArtist.artistURI)">{{associatedArtist.name}}</a> (<a ng-href="{{associatedArtist.artistURI}}" target="_blank">URI</a>)</li>
							</ul>
						</td>
					</tr>
					
					<tr ng-show="band.formerBandMembers">
						<th>Former Members</th>
						<td>
							<ul style="list-style-type: disc">
								<li ng-repeat="formerBandMember in band.formerBandMembers"><a href="" ng-click="treatePage('artist', formerBandMember.artistURI)">{{formerBandMember.name}}</a> (<a ng-href="{{formerBandMember.artistURI}}"  target="_blank">URI</a>)</li>
							</ul>
						</td>
					</tr>
					<tr ng-show="band.associatedBands">
						<th>Associated Bands</th>
						<td>
							<ul style="list-style-type: disc">
								<li ng-repeat="associatedBand in band.associatedBands"><a href="" ng-click="treatePage('band', associatedBand.bandURI)">{{associatedBand.name}}</a> (<a ng-href="{{associatedBand.bandURI}}"  target="_blank">URI</a>)</li>
							</ul>
						</td>
					</tr>
				</table>
				
				<div class="alert alert-info" role="alert" ng-show="loading"><span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span> Loading...</div>
				
			</div>

		</div>

		<footer class="footer">
			<p>Information &copy; DBpedia</p>
			<p>Application &copy; Serge Peter 2015</p>
		</footer>

	</div>

</body>


</html>
