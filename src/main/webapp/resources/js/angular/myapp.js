var app = angular.module('app', []);

app.controller('artistSearchController', function ($scope, $http) {
	
	$scope.searchName;
	$scope.artists;
	
	$scope.searchArtist = function(searchName) {
		console.log("search for " + searchName);
		$http.get("rest/artists?name=" + searchName)
			.success(function(response){
				$scope.artist=null;
				$scope.artists = response;	
					console.log("Nb artist found " + $scope.artists.length);
			});
	}
	
	$scope.moreInfo = function(artistURI) {

		console.log("get more info for : " + artistURI);
		
		$http.get("rest/artist?uri=" + artistURI)
		.success(function(response){
			$scope.artist = response;	
				console.log("Artist " + $scope.artist.name);
		});
		
	}
});