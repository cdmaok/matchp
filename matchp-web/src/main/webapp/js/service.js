var app = angular.module("matchp",['ngResource'])

app.controller('queryForm',['$scope','$location','$resource',function($scope,$location,$resource){
  $scope.clock = new Date();
  $location.path("../result.html")
  $scope.search = function(){
    console.log("search is " + $scope.queryText);
    var Entrys = $resource('/matchp-web/query?q=:text',{text:$scope.queryText});
    $scope.results = Entrys.query(function(){
      console.log($scope.results);
    });
    var path = $location.path("/result.html").replace();
	console.log(path)
  };
}])
