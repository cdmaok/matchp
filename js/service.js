var app = angular.module("matchp",['ngResource'])

app.controller('queryForm',['$scope','$location','$resource',function($scope,$location,$resource){
  $scope.clock = new Date();
  //$location.path("../result.html")
  $scope.search = function(){
    console.log("search is " + $scope.queryText);
    var Entrys = $resource('/matchp-web/api/query?q=:text',{text:$scope.queryText});
	var test  = Entrys.query(function(data){
		console.log('the length is ', data.length)
		$scope.results = data;
	},function(err){
		console.log("query failed " + err);
	});	
   // var path = $location.path("/result.html").replace();
	//console.log(path)
  };
}])