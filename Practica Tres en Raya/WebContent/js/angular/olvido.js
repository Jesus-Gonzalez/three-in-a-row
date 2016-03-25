appTresEnRaya.controller('olvidoCtrl', ['$scope', '$http', function($scope, $http){

	function hideAlerts()
	{
		$('.alert-box').hide();
	}

	$scope.enviarUsuario = function(){
		
		hideAlerts();

		$http.post('usuarios/olvido', {tipo: 'usuario', email: $scope.email})
		.then(
				function(res){
					
					if (res.data.success)
					{
						$('#form-olvido').hide();
						
						$('#alert-olvido-success').removeClass('hidden').show();
						
						return;
					}

					if (res.data.error.existe)
					{
						$('#olvido-alert-correo').removeClass('hidden').show();

						return;
					}
					
				},
				
				function(res){

					console.log(res)
				
				})
		
	};

	$scope.enviarContrasena = function(){

		hideAlerts();

		$http.post('usuarios/olvido', {tipo: 'contrasena', email: $scope.email})
		.then(
				function(res){
					
					if (res.data.success)
					{
						$('#form-olvido').hide();
						
						$('#alert-olvido-success').removeClass('hidden').show();
						
						return;
					}
					
					if (res.data.error.existe)
					{
						$('#olvido-alert-correo').show();

						return;
					}
					
				},
				
				function(res){

					console.log(res)
				
				})
	}
	
}]);
