appTresEnRaya
	   .controller('usuariosCtrl', ['$scope', '$http', '$window', function($scope, $http, $window){

	   		$scope.usuarios = [];

	   		$scope.desafiar = function(uid){
	   		
				$http.post('partidas/crear', { uid: uid })
				.then(
					function(res){

						console.log(res)

					},
					function(res){

						// Alertar
						console.log(res);
					});	
	   		};

	   		$scope.verPerfil = function(uid){

	   			$window.location.href = "ver-perfil.jsp?uid=" + uid; 
	   		};

	   		function actualizaUsuariosConectados(){
	   			$http.post('usuarios/conectados', {tipo: 'registrados'})
				 .then(
				 	function(res){

				 		$scope.usuarios = res.data.usuarios;
					 
					 },
					 function(res){
					 	console.log(res);
					 });
	   		}

	   		actualizaUsuariosConectados();
			setInterval(actualizaUsuariosConectados, 5678);

		}]);