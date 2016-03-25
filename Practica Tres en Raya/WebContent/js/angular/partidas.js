appTresEnRaya
	   .controller('partidaCtrl', ['$scope', '$http', '$window', function($scope, $http, $window){

	   		var pid = getParametroByName('pid')

	   		$scope.estado = undefined;
	   		$scope.resultado = undefined;

	   		$scope.desafiante = {};
	   		$scope.desafiado = {};

	   		$scope.movimientos = [];
	   		$scope.timeUltimoMovimiento = 0;

	   		$scope.partidasJugadas = 0;

	   		$scope.segundosRestantes = undefined;

			$scope.mover = function(casilla){
				console.log("moviendo", casilla)
				$http.post('partidas/procesar', { pid: pid, tipo: 'mover', casilla: casilla })
	   			.then(
	   					function(res){

	   						$scope.segundosRestantes = 59;
	   						getPartidaData();
	   					},

	   					function(res){
	   						
	   						console.log(res);
	   					});
			};

			$scope.reiniciar = function(){

				$http.post('partidas/procesar', { pid: pid, tipo: 'reiniciar' })
	   			.then(
	   					function(res){
	   						$scope.segundosRestantes = 59;
	   						getPartidaData();
	   					},

	   					function(res){
	   						console.log(res);
	   					});

			};

			$scope.terminar = function(){

				$http.post('partidas/procesar', { pid: pid, tipo: 'terminar' })
				.then(
					
					function(res){

						console.log("success")

						setTimeout(function(){

							$window.location.href = "index.jsp";
						
						}, 5000)
							// $window.location.href = "index.jsp";
					},

					function(res){

						console.log(res)
					});
			};

	   		function getPartidaData(){

	   			$http.post('partidas/procesar', { pid: pid, tipo: 'getDatos' })
	   			.then(
	   					function(res){
	   					
	   						$scope.estado = res.data.estado;
	   						$scope.resultado = res.data.resultado;
	   						$scope.desafiante = res.data.desafiante;
	   						$scope.desafiado = res.data.desafiado;
	   						$scope.movimientos = res.data.movimientos; 
	   						$scope.timeUltimoMovimiento = res.data.timeUltimoMovimiento;
	   						console.log("servertime", res.data.serverTime)
	   						$scope.segundosRestantes = (59 - Math.round((res.data.serverTime - $scope.timeUltimoMovimiento) / 1000));
	   						$scope.partidasJugadas = res.data.partidasJugadas;
	   					},

	   					function(res){
	   						console.log(res);
	   					});
			}

			function restarSegundos(){
	   			
	   			if ($scope.segundosRestantes > 0)

	   				$scope.segundosRestantes--;
	   			
	   			else
	   				setTimeout(function(){

	   					$window.location.href = "index.jsp";
	   				
	   				}, 120000)
	   		}

	   		function comprobarSiTerminada()
	   		{
	   			 if ($scope.estado === 3)
	   			 	
	   			 	setTimeout(function(){
	   			 		
	   			 		$window.location.href = "index.jsp";

	   			 	}, 2500)
	   		}

			getPartidaData();
	   		setInterval(getPartidaData, 678);

	   		restarSegundos();
	   		setInterval(restarSegundos, 1000);


	   }]);