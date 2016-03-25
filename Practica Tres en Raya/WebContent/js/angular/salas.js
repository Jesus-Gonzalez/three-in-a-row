appTresEnRaya.controller('salasCtrl', ['$scope', '$http', '$window', function($scope, $http, $window){
	
	$scope.partidas = [];

	$scope.ir = function(pid){

		if (pid)
				$window.location.href = "partida.jsp?pid=" + pid;
	};

	function comprobarPartidasActivas()
	{
		$http.post('partidas/comprobar', {tipo: 'todas'})
		.then(
			function(res){
				console.log("partidas", res.data)
				$scope.partidas = res.data;
			},
			function(res){

				console.log(res);
			})
	}

	comprobarPartidasActivas();
	setInterval(comprobarPartidasActivas, 3456);

}])