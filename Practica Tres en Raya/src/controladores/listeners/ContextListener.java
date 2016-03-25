package controladores.listeners;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import modelos.Conexion;
import modelos.MPartidas;
import modelos.MUsuariosConectados;
import modelos.Partida;
import utils.DaemonEliminarPartidas;

public class ContextListener
implements ServletContextListener
{	
	private DaemonEliminarPartidas dEliminarPartidas;
	
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		ServletContext context = sce.getServletContext();
		context.setAttribute("usuarios.conectados", 0);
//		Crea mapa de partidas activas
		HashMap<String, Partida> mapaPartidas = new HashMap<String, Partida>();
		context.setAttribute("partidas.activas", mapaPartidas);
//		Elimina tabla usuarios
//		Crea conexión y almacena el objeto en la sesión del usuario
//		Datos conexión PostgreSQL
		Connection conexion = new Conexion("localhost", "tresenraya", "user", "password").creaConexion();

		MUsuariosConectados mdlUsuariosConectados = new MUsuariosConectados(conexion);
		MPartidas mdlPartidas = new MPartidas(conexion);

		mdlUsuariosConectados.vaciarTablaUsuariosConectados();
		mdlPartidas.vaciarTablaPartidas();

		dEliminarPartidas = new DaemonEliminarPartidas(conexion, mapaPartidas);
		dEliminarPartidas.start();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{	
		ServletContext context = sce.getServletContext();
		context.setAttribute("partidas.activas", null);
		
		dEliminarPartidas.acabarEjecucion();
	}
}
