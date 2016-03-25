package modelos;

/**
 * DAO para Usuarios
 * @author usuario
 *
 */
public class Usuario
{
	public static final int JUGADAS = 0,
							GANADAS = 1,
							PERDIDAS = 2,
							EMPATADAS = 3;
	
	public
		long 	uid,
				fechaRegistro,
				fechaConexion;
	
	public
		String 	nombre,
				contrasena,
				correo,
				pais,
				ip;
	
	public
		Integer[] datosPartidas = new Integer[4];
	
	public
		boolean activado;

	public Usuario(){}
	
	public Usuario(MUsuarios mdlUsuarios)
	{
		uid = mdlUsuarios.uid;
		nombre = mdlUsuarios.nombre;
		contrasena = mdlUsuarios.contrasena;
		correo = mdlUsuarios.correo;
		pais = mdlUsuarios.pais;
		activado = mdlUsuarios.activado;
		
		ip = mdlUsuarios.ip;
		
		datosPartidas = mdlUsuarios.datosPartidas;
		
		fechaRegistro = mdlUsuarios.fechaRegistro;
		fechaConexion = mdlUsuarios.fechaConexion;
	}
}
