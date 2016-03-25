import java.sql.Connection;
import java.util.Calendar;

import modelos.Conexion;
import modelos.MActivaciones;
import modelos.MUsuarios;
import utils.Correo;

public class Main
{	
	public static void main(String[] args)
	{	
		Connection conexion = new Conexion("localhost", "database", "user", "password").creaConexion();
		Correo correo;
		MUsuarios mdlUsuarios = new MUsuarios(conexion);
		MActivaciones mdlActivaciones = new MActivaciones(conexion);

		int usuariosFechaLimite = 0, usuariosParaEliminar = 0;

		mdlActivaciones.getActivacionesFechaLimite(Calendar.getInstance().getTimeInMillis());

		while (mdlActivaciones.getProximaActivacion())
		{
			mdlUsuarios.getUsuarioByUid(mdlActivaciones.uid);

			if (mdlUsuarios.getProximoUsuario()) {
				correo = new Correo(mdlUsuarios.correo);
				correo.enviarCorreoAviso();

				usuariosFechaLimite++;
			}

			mdlActivaciones.avisado = true;

			mdlActivaciones.actualizarActivacion();
		}

		// Eliminar activaciones
		mdlActivaciones.getActivacionesParaEliminar(Calendar.getInstance().getTimeInMillis());

		while (mdlActivaciones.getProximaActivacion())
		{
			mdlUsuarios.getUsuarioByUid(mdlActivaciones.uid);

			if (mdlUsuarios.getProximoUsuario()) {
				correo = new Correo(mdlUsuarios.correo);
				correo.enviarCorreoCuentaEliminada();

				mdlUsuarios.eliminarUsuario();

				usuariosParaEliminar++;
			}

			mdlActivaciones.borraActivacion();
		}

		System.out.println("-------------------------------------------------------");
		System.out.println(Calendar.getInstance().getTime());
		System.out.println("Se han avisado a " + usuariosFechaLimite + " usuarios.");
		System.out.println("Se han eliminado " + usuariosParaEliminar + " usuarios.");
		System.out.println("Ejecución finalizada");
		System.out.println("-------------------------------------------------------");
	}

}
