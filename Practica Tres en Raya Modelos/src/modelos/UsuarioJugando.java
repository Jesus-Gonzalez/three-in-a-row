package modelos;

public class UsuarioJugando
extends Usuario
{
	public
		int	ganadasActual = 0,
			perdidasActual = 0,
			empatesActual = 0;
	
	public
		boolean turno,
				ganador;
	
	public UsuarioJugando(){}
	
	public UsuarioJugando(Usuario usuario)
	{
		uid = usuario.uid;
		nombre = usuario.nombre;
		correo = usuario.correo;
		pais = usuario.pais;
		contrasena = usuario.contrasena;
		activado = usuario.activado;
		datosPartidas = usuario.datosPartidas;
	}
	
	public UsuarioJugando(MUsuarios mdlUsuarios)
	{
		super(mdlUsuarios);
	}
}
