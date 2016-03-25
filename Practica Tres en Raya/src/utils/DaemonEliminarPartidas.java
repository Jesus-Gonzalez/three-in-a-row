package utils;

import java.sql.Connection;
import java.util.Calendar;
import java.util.HashMap;

import modelos.MPartidas;
import modelos.Partida;

public class DaemonEliminarPartidas
extends Thread
{
	private Connection conexion;
	
	private HashMap<String, Partida> mapaPartidas;
	
	private boolean webAppEjecutando;
	
	public DaemonEliminarPartidas(Connection conexion, HashMap<String, Partida> mapaPartidas)
	{
		this.conexion = conexion;
		this.mapaPartidas = mapaPartidas;
		webAppEjecutando = true;
	}
	
	public void acabarEjecucion()
	{
		webAppEjecutando = false;
	}
	
	@Override
	public void run()
	{
		try
		{
			while (webAppEjecutando)
			{
				Partida partida;
				MPartidas mdlPartidas = new MPartidas(conexion);
				
				mdlPartidas.getAllPartidas();
				
				while (mdlPartidas.getProximaPartida())
				{
					partida = mapaPartidas.get(mdlPartidas.pid);
					
					if (partida != null)
					{
						if (  (partida.estado == Partida.FIN && (Calendar.getInstance().getTimeInMillis() - partida.timeUltimoMovimiento) > 120000) ||
							  (partida.estado == Partida.TERMINADA) ||
							  (partida.estado == Partida.DESAFIADO && ((Calendar.getInstance().getTimeInMillis() - partida.fechaCreacion ) > 300000))) // 5 minutos = 300000 milisegundos
						{
							mapaPartidas.remove(mdlPartidas.pid);
							mdlPartidas.borrarPartida();
						}

					}
				}

				Thread.sleep(21234);
			}

		} catch(InterruptedException x) {
			
			System.err.println("Eliminar Partidas Daemon Interrumpido -> Gravedad: baja");
			x.printStackTrace();
			
		}
	}
	
}
