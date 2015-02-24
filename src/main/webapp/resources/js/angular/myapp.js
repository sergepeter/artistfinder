var app = angular.module('app', []);

/**
 * Main application controller
 */
app.controller('artistSearchController', function($scope, $http) {

	// scope vars
	$scope.searchName; 				// search box
	$scope.page = {					// page info
		label : "Search",
		type : "searchArtist",
		what : null,
		id : 1
	};
	$scope.pages = [];				// page list
	$scope.artists;					// artist list (results)
	$scope.bands;					// band list	
	$scope.loading=false;			// manage loading info

	/**
	 * search for artist by name
	 */
	$scope.searchArtist = function(searchName) {
		console.log("Search for artist " + searchName);
		
		$scope.loading=true;
		
		$http.get("rest/artists?name=" + searchName).success(
				function(response) {
					
					$scope.artist = null;
					$scope.artists = response;

					console.log("Nb artist found " + $scope.artists.length);
					$scope.searchName = searchName;
					
					$http.get("rest/bands?name=" + searchName).success(
							function(response) {
								
								$scope.band = null;
								$scope.bands = response;
								
								$scope.page = {
										label : "Search: " + searchName,
										type : "searchArtist",
										what : searchName,
										id : $scope.pages.id + 1,
										length : $scope.artists.length + $scope.bands.length
								}
								$scope.addPage($scope.page);
								console.log("Nb band found " + $scope.bands.length);
							});
					
					
					$scope.loading=false;

				});
		
		
	}

	/**
	 * get more artist info
	 */
	$scope.moreInfo = function(artistURI) {

		console.log("Get more info for : " + artistURI);
		
		$scope.loading=true;

		$http.get("rest/artist?uri=" + artistURI).success(function(response) {
			$scope.artist = response;

			console.log("Artist " + $scope.artist.name);

			$scope.page = {
				label : "Info : " + $scope.artist.name,
				type : "artist",
				what : artistURI,
				id : $scope.pages.id + 1
			};
			
			// second call made for performance issue
	 		$http.get("rest/artistfull?uri=" + artistURI).success(function(response) {
				
				$scope.artist = response;
				console.log("Artist full " + $scope.artist.name);
				$scope.loading=false;
			});
			
	 		$scope.addPage($scope.page);

		});
		
	

	}
	
	/**
	 * get band info
	 */
	$scope.bandInfo = function(bandURI) {

		console.log("Get more info for : " + bandURI);
		
		$scope.loading=true;

		$http.get("rest/band?uri=" + bandURI).success(function(response) {
			
			$scope.band = response;

			console.log("Band " + $scope.band.name);

			$scope.page = {
				label : "Info : " + $scope.band.name,
				type : "band",
				what : bandURI,
				id : $scope.pages.id + 1
			};
			
			$scope.addPage($scope.page);
			
			// second call for performance issue
			$http.get("rest/bandfull?uri=" + bandURI).success(function(response) {
				
				$scope.band = response;
				console.log("Band full " + $scope.band.name);
				
				$scope.loading=false;

			});

		});

	}

	/**
	 * recall an view page (and refresh queries)
	 */
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
	
	/**
	 * Manage page info in list of pages
	 */
	$scope.addPage = function(page) {
		console.log("Add page to list " + page.label);
		var found=null;
		for	(index = 0; index < $scope.pages.length; index++) {		    
			if (page.label == $scope.pages[index].label) {
				found = index;
			}
		}
	
		if (found == null){
			
			
			if ($scope.pages.length >= 5) {
				// remove first ellement in list
				var newlist = [];
				for	(index = 0; index < $scope.pages.length; index++) {		    
					if (index > 0) {
						newlist.push($scope.pages[index]);
					}
				}
				$scope.pages = newlist;
			}
			
			// add page to page list
			$scope.pages.push($scope.page);
		}
	}
	
});

