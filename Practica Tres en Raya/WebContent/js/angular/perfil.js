appTresEnRaya.controller('perfilCtrl', ['$scope', '$http', function($scope, $http){
	
	$scope.usuario = {};

	$scope.success = false;
	$scope.activacion = false;
	$scope.error = {}

	$http.get('usuarios/perfil')
	.then(
		function(res){

			if (res.data.success)
			{		
				$scope.usuario =  res.data.usuario;

			}
		},
		function(res){
			console.log(res)
		})

	$scope.modificar = function(v){
		
		$scope.error = {};

		$http.post('usuarios/perfil', {tipo: 'modificar', perfil: $scope.usuario})
		.then(
			function(res){
			
				if (res.data.success)
				{
					$scope.success = true;

					setTimeout(function(){

						$scope.success = false;
						
					}, 12500)

					if (res.data.activacion)
					{
						$scope.activacion = true;

						setTimeout(function(){

							$scope.activacion = false;
						
						}, 15000)
					}

				} else if (res.data.error) {

					$scope.error = res.data.error;
				}

			},
			function(res){
				console.log(res)
			})
	};

}])