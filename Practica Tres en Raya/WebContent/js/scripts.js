function getParametroByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}


$(document).ready(function(){

	function generarAlert(tipo, mensaje)
	{
		if (!mensaje)
		{
			mensaje = tipo;
			tipo = 'danger';
		}
		
		return 	'<div class="alert alert-danger alert-registro">' +
					'<strong>Error!</strong> ' + mensaje +
				'</div>';
	}
	
	// Formulario de Registro
	(function(){

		$('#formulario-registro').submit(function(e){
			e.preventDefault();

			var $refreshIcon = $('#reg-refresh-icon').removeClass('hidden').show(),
				$btnRegistrar = $('#btn-registrar').hide();

			$('.alert-registro').parent('.form-group').removeClass('has-error');
			$('.alert-registro').remove();
			$('.alert-registro').addClass('oculto');

			var $usuario = $('#nombre-usuario'),
				$contrasena = $('#contrasena'),
				$contrasenaConfirmar = $('#contrasena-confirmar'),
				$correo = $('#correo-electronico'),
				$pais = $('#pais'),

				usuario = $usuario.val(),
				contrasena = $contrasena.val(),
				contrasenaConfirmar = $contrasenaConfirmar.val(),
				correo = $correo.val(),
				pais = $pais.val();

			$.ajax({
				url: 'usuarios/crear',
				type: 'POST',
				// dataType: 'json',
				data: { usuario: usuario, contrasena: contrasena, contrasenaConfirmar: contrasenaConfirmar, correo: correo, pais: pais },
			})
			.done(function(data) {				

				if (data.error)
				{
					var $alert, $l;

					if (data.error.usuario)
					{
						if (data.error.usuario.vacio)
						{
							$alert = generarAlert('Debe introducir un nombre de usuario.');
							$l = $usuario.parent('.form-group');
							$l.addClass('has-error');
							$l.append($alert);

						} else if (data.error.usuario.overflow) {

							$alert = generarAlert('El nombre de usuario contiene demasiados caracteres (máximo 30)');
							$l = $usuario.parent('.form-group');
							$l.addClass('has-error');
							$l.append($alert);
						
						} else if (data.error.usuario.underflow) {
							
							$alert = generarAlert('El nombre de usuario es demasiado corto (mínimo 5)');
							$l = $usuario.parent('.form-group');
							$l.addClass('has-error');
							$l.append($alert);
							
						} else if (data.error.usuario.formato) {
						
							$alert = generarAlert('El nombre de usuario tiene un formato incorrecto. Utilice solo letras a-z y números');
							$l = $usuario.parent('.form-group');
							$l.addClass('has-error');
							$l.append($alert);
							
						} else if (data.error.usuario.existe) {

							$alert = generarAlert('El nombre de usuario ya existe. Introduzca otro.');
							$l = $usuario.parent('.form-group');
							$l.addClass('has-error');
							$l.append($alert);

						}
					}

					if (data.error.correo)
					{
						if (data.error.correo.vacio)
						{
							$alert = generarAlert('Debe introducir una dirección de correo electrónico.');
							$l = $correo.parent('.form-group');
							$l.addClass('has-error');
							$l.append($alert);

						} else if (data.error.correo.formato) {

							$alert = generarAlert('Debe introducir un correo electrónico en formato correcto.');
							$l = $correo.parent('.form-group');
							$l.addClass('has-error');
							$l.append($alert);

						} else if (data.error.correo.existe) {

							$alert = generarAlert('Este correo electrónico ya existe. Introduzca otro.');
							$l = $correo.parent('.form-group');
							$l.addClass('has-error');
							$l.append($alert);

						}
					}

					if (data.error.contrasena)
					{
						if (data.error.contrasena.vacio)
						{

							$alert = generarAlert('Debe introducir una contraseña obligatoriamente.');
							$l = $contrasena.parent('.form-group');
							$l.addClass('has-error');
							$l.append($alert);

						} else if (data.error.contrasena.overflow) {

							$alert = generarAlert('La contraseña puede tener como máximo 50 carácteres.');
							$l = $contrasena.parent('.form-group');
							$l.addClass('has-error');
							$l.append($alert);

						} else if (data.error.contrasena.underflow) {
							
							$alert = generarAlert('La contraseña es demasiado corta. (mínimo 6 carácteres)');
							$l = $contrasena.parent('.form-group');
							$l.addClass('has-error');
							$l.append($alert);
						
						} else if (data.error.contrasena.equals) {

							$alert = generarAlert('La contraseña introducida no coincide.');
							$l = $contrasenaConfirmar.parent('.form-group');
							$l.addClass('has-error');
							$l.append($alert);

						}
					}

					if (data.error.pais)
					{
						$alert = generarAlert('El país introducido no es correcto.');
						$l = pais.parent('.form-group');
						$l.addClass('has-error');
						$l.append($alert);
					}

					if (data.error.registro)
					{
						$alert = generarAlert('Se ha producido un error al registrar el usuario.');
						$('#seccion-registro').prepend($alert);
					}

				} else if (data.success) {

					$('#seccion-registro').empty();

					var
						out = '<h1 class="text-success">Registrado con Éxito</h1>' +
							  '<div class="alert alert-success">' +
							  '<p>Se ha realizado el registro exitosamente.</p>' +
							  '<p>Compruebe su correo electrónico. Recibirá un mensaje de activación de su cuenta.</p>' +
							  '<p>¿No lo ha recibido? <a href="#">Reenviar correo de activación</a></p>' +
							  '</div>';
					
					$('#seccion-registro').append(out);

				}
			})
			.fail(function(data) {
				console.log("error");
				console.log(data);
			})
			.always(function(){

				$refreshIcon.hide();
				$btnRegistrar.show();
			});
			

			return false;
		})

	})();

	// Formulario Login
	(function(){

		$('#formulario-login').submit(function(e){

			e.preventDefault();

			$('.alert-registro').parent('.form-group').removeClass('has-error');
			$('.alert-registro').remove();

			var $usuario = $('#nombre'),
				$contrasena = $('#contrasena'),
				nombre = $usuario.val(),
				contrasena = $contrasena.val(),
				recordar = $('#recordar').prop('checked');

			$.ajax({	
					url: 'usuarios/login',
					type: 'POST',
					dataType: 'json',
					data: { nombre: nombre, contrasena: contrasena, recordar: recordar },
				})
				.done(function(data) {
					
					if (data.error)
					{
						var $alert, $l;

						if (data.error.usuario)
						{
							if (data.error.usuario.vacio)
							{

								$alert = generarAlert('Debe introducir un nombre de usuario obligatoriamente.');
								$l = $usuario.parent('.form-group');
								$l.addClass('has-error');
								$l.append($alert);

							} else if (data.error.usuario.overflow) {

								$alert = generarAlert('El nombre de usuario es demasiado largo (máximo 30 caracteres).');
								$l = $usuario.parent('.form-group');
								$l.addClass('has-error');
								$l.append($alert);

							} else if (data.error.usuario.underflow) {

								$alert = generarAlert('El nombre de usuario es demasiado corto (mínimo 5 caracteres).');
								$l = $usuario.parent('.form-group');
								$l.addClass('has-error');
								$l.append($alert);

							} else if (data.error.usuario.existe) {

								$alert = generarAlert('El usuario introducido no existe.');
								$l = $usuario.parent('.form-group');
								$l.addClass('has-error');
								$l.append($alert);

							} else if (data.error.usuario.contrasena) {

								$alert = generarAlert('La contraseña introducida no es correcta.');
								$l = $contrasena.parent('.form-group');
								$l.addClass('has-error');
								$l.append($alert);

							} else if (data.error.usuario.conectado) {

								$('#registro-alert-ya-conectado').removeClass('hidden');
								$('#contrasena').val('');

							}
						}

						if (data.error.contrasena)
						{
							if (data.error.contrasena.vacio)
							{
								$alert = generarAlert('Debe introducir una contraseña obligatoriamente.');
								$l = $contrasena.parent('.form-group');
								$l.addClass('has-error');
								$l.append($alert);

							} else if (data.error.contrasena.overflow) {

								$alert = generarAlert('La contraseña es demasiado larga (máximo 50 caracteres).');
								$l = $contrasena.parent('.form-group');
								$l.addClass('has-error');
								$l.append($alert);

							} else if (data.error.contrasena.underflow) {

								$alert = generarAlert('La contraseña es demasiado corta (mínimo 6 caracteres).');
								$l = $contrasena.parent('.form-group');
								$l.addClass('has-error');
								$l.append($alert);

							}
						}

						if (data.error.cuenta && data.error.cuenta.activado)
						{
							var $reenviar = $(document.createElement("a"));
							$reenviar.attr('href', 'activacion.jsp?accion=reenviar').addClass('btn').addClass('btn-warning').text('Reenviar clave de activación');
							console.log($reenviar);
							$alert = generarAlert('Su cuenta no está activada. ' + $reenviar[0].outerHTML);
							$l = $usuario.parent('.form-group');
							$l.addClass('has-error');
							$l.append($alert);
						}
					}
					
					if (data.success)
					{
						location.href = "index.jsp";
					}
				})
				.fail(function(data) {
					console.log("error");
					console.log(data);
				});

			return false;

		});

	})();
	
	// Reenviar clave de activación
	(function(){
		
		$('#formulario-reenvio').submit(function(e){
			e.preventDefault();

			$('.alert-registro').parent('.form-group').removeClass('has-error');
			$('.alert-registro').remove();


			var $correo = $('#correo'),
				correo = $correo.val();

			$.ajax({
				url: 'usuarios/activacion/reenviar',
				type: 'POST',
				dataType: 'json',
				data: {correo: correo},
			})
			.done(function(data) {
				console.log("success");
				console.log(data);
				
				if (data.error)
				{
					if (data.error.vacio)
					{
						$alert = generarAlert('Debe introducir un correo electrónico obligatoriamente.');
						$l = $correo.parent('.form-group');
						$l.addClass('has-error');
						$l.append($alert);
						
					} else if (data.error.formato) {
						
						$alert = generarAlert('Ha introducido un correo electrónico en un formato incorrecto.');
						$l = $correo.parent('.form-group');
						$l.addClass('has-error');
						$l.append($alert);

					} else if (data.error.existe) {

						$alert = generarAlert('El correo electrónico introducido no existe.');
						$l = $correo.parent('.form-group');
						$l.addClass('has-error');
						$l.append($alert);

					} else if (data.error.activado) {

						$alert = generarAlert('La cuenta para este correo electrónico ya está activada.');
						$l = $correo.parent('.form-group');
						$l.addClass('has-error');
						$l.append($alert);
					}

				} else if (data.success) {

					console.log("reenviado");
				}
			})
			.fail(function(data) {
				console.log("error");
				console.log(data);
			});
			

			return false;
		});
		
	})();

	(function(){

		$('#aceptar-alerta-cookie').click(function(){

			$('#nav-alerta-cookie').hide();

		});

	})();

})