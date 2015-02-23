var app = angular.module('app', []);

app.controller('artistSearchController', function($scope, $http) {

	$scope.searchName;
	$scope.page = {
		label : "Search",
		type : "searchArtist",
		what : null,
		id : 1
	};

	$scope.pages = [];

	$scope.artists;

	/**
	 * search for artist by name
	 */
	$scope.searchArtist = function(searchName) {
		console.log("search for " + searchName);
		$http.get("rest/artists?name=" + searchName).success(
				function(response) {
					$scope.artist = null;
					$scope.artists = response;

					console.log("Nb artist found " + $scope.artists.length);

					$scope.searchName = searchName;

					$scope.page = {
						label : "Search: " + searchName,
						type : "searchArtist",
						what : searchName,
						id : $scope.pages.lenght + 1,
						length : $scope.artists.length
					};
					$scope.pages.push($scope.page);

				});
	}

	$scope.moreInfo = function(artistURI) {

		console.log("get more info for : " + artistURI);

		$http.get("rest/artist?uri=" + artistURI).success(function(response) {
			$scope.artist = response;

			console.log("Artist " + $scope.artist.name);

			$scope.page = {
				label : "Info : " + $scope.artist.name,
				type : "artist",
				what : artistURI,
				id : $scope.pages.lenght + 1
			};
			
			$scope.pages.push($scope.page);

		});

	}
	
	$scope.bandInfo = function(bandURI) {

		console.log("get more info for : " + bandURI);

		$http.get("rest/band?uri=" + bandURI).success(function(response) {
			$scope.band = response;

			console.log("Artist " + $scope.band.name);

			$scope.page = {
				label : "Info : " + $scope.band.name,
				type : "band",
				what : bandURI,
				id : $scope.pages.lenght + 1
			};
			
			$scope.pages.push($scope.page);

		});

	}

	$scope.treatePage = function(type, what) {
		console.log(type + " / " + what);

		if (type == "searchArtist") {
			$scope.searchArtist(what);
		} else if (type == "artist") {
			$scope.moreInfo(what);
		} else if (type == "band") {
			$scope.bandInfo(what);
		}

	}
});