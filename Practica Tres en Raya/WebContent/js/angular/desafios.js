appTresEnRaya
	   .controller('desafiosCtrl', ['$scope', '$http', '$window', function($scope, $http, $window, $desafios){


	   		$scope.desafiosEnviados = [];
	   		$scope.desafiosAceptados = [];
	   		$scope.desafiosRecibidos = [];
	   		$scope.desafiosRechazados = [];
	   		
	   		$scope.aceptar = function($index, desafio){
	   		
				$http.post('desafios/procesar', {tipo: 'aceptar', pid: desafio.pid})
				.then(
					function(res){

						console.log("aceptar", res.data)
						
						if (res.data.success)
						{
							$scope.desafiosRecibidos.splice($index, 1);

							$window.location.href = 'partida.jsp?pid=' + desafio.pid;
						}

						// Redireccionar a la partida
					},
					function(res){

						// Alertar
						console.log(res);
					});
	   			
				
	   		};

			$scope.rechazar = function($index, desafio){

				$http.post('desafios/procesar', {tipo: 'rechazar', pid: desafio.pid})
				.then(
					function(res){

						console.log("rechazar", res)
						
						if (res.data.success)

							$scope.desafiosRecibidos.splice($index, 1);

						// Redireccionar a la partida
					},
					function(res){

						// Alertar
						console.log(res);
					});
			};

			$scope.eliminarDesafio = function($index, desafio){

				$http.post('desafios/procesar', {tipo: 'eliminar', pid: desafio.pid})
				.then(
					function(res){

						console.log("eliminar", res)
						
						if (res.data.success)
							
							$scope.desafiosRechazados.splice($index, 1);

						// Redireccionar a la partida
					},
					function(res){

						// Alertar
						console.log(res);
					});

			};

			$scope.ir = function(pid){

				$window.location.href = "partida.jsp?pid=" + pid;

			}


			function comprobarDesafiosEntrantes()
			{
				$http.post('partidas/comprobar', {tipo: 'desafios'})
				.then(
					function(res){
						
						console.log("entrantes", res)

						$scope.desafiosRecibidos = res.data;
						// Redireccionar a la partida
					},
					function(res){

						// Alertar
						console.log(res);
					});
			}

			function getDesafiosEnviados(){
		   		$http.post('partidas/comprobar', {tipo: 'desafios.enviados'})
		   		.then(
		   			function(res){

		   				console.log("desafios enviados", res);
		   				
		   				$scope.desafiosEnviados = res.data;
		   			},
		   			function(res){
		   				console.log(res);
		   			});
	   		}

			function comprobarDesafiosRechazados()
			{
				$http.post('partidas/comprobar', {tipo: 'desafios.rechazados'})
				.then(
					function(res){

						console.log("rechazados", res)
						
						$scope.desafiosRechazados = res.data;

						// Redireccionar a la partida
					},
					function(res){

						// Alertar
						console.log(res);
					});
			}

			function comprobarDesafiosAceptados()
			{
				$http.post('partidas/comprobar', {tipo: 'desafios.aceptados'})
				.then(
					function(res){

						console.log("aceptados", res);
						
						$scope.desafiosAceptados = res.data;
					},
					function(res){

						// Alertar
						console.log(res);
					});	
			}

			getDesafiosEnviados();
			comprobarDesafiosEntrantes();
			comprobarDesafiosAceptados();
			comprobarDesafiosRechazados();
			
			setInterval(getDesafiosEnviados, 1234);
			setInterval(comprobarDesafiosEntrantes, 2345);
			setInterval(comprobarDesafiosAceptados, 2345);
			setInterval(comprobarDesafiosRechazados, 2345);
	   }])