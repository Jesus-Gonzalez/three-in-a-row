package modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MPartidas
{
	private Connection conexion;
	private ResultSet rs;
	
	/**
	 * PID = Partida ID
	 */
	public String pid;
	
	public
		long desafiante, desafiado;
	
	public
		boolean aceptado;
	
	public MPartidas(Connection conexion)
	{
		this.conexion = conexion;
	}
	
	public boolean getProximaPartida()
	{
		try
		{
			if (rs.next())
			{
				pid = rs.getString("pid");
				desafiante = rs.getLong("desafiante");
				desafiado = rs.getLong("desafiado");
				aceptado = rs.getBoolean("aceptado");
				
				return true;

			} else {
			
				return false;
			}
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MPartidas:getProximaPartida()");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-40000);
		}
		
		return false;
	}
	
	public void creaPartida(String pid, long uidDesafiante, long uidDesafiado)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("INSERT INTO tresenraya.partidas (pid, desafiante, desafiado) VALUES (?, ?, ?)");
			
			ps.setString(1, pid);
			ps.setLong(2, uidDesafiante);
			ps.setLong(3, uidDesafiado);
			
			ps.executeUpdate();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MPartidas:creaPartida(String, long, long)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-40001);
		}
	}
	
	public void borrarPartidaByPid(String pid)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("DELETE FROM tresenraya.partidas WHERE pid = ?");
			
			ps.setString(1, pid);
			
			ps.executeUpdate();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MPartidas:borrarPartidaByPid(String)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-40002);
		}
	}
	
	public void borrarPartida()
	{
		borrarPartidaByPid(pid);
	}
	
	public void getMisPartidasActivas(long uid)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM tresenraya.partidas WHERE (desafiado = ? OR desafiante = ?) AND aceptado = 1");
			ps.setLong(1, uid);
			ps.setLong(2, uid);
			
			rs = ps.executeQuery();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MPartidas:getMisPartidasActivas(long)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-40003);
		}
	}
	
	public void buscarDesafiosByUid(long uid)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM tresenraya.partidas WHERE desafiado = ? AND aceptado = 0");
			ps.setLong(1, uid);
			
			rs = ps.executeQuery();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MPartidas:buscarDesafiosByUid(long)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-40004);
		}
	}
	
	public void comprobarMisDesafiosEnviados(long uid)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM tresenraya.partidas WHERE desafiante = ? AND aceptado = ?");
			ps.setLong(1, uid);
			ps.setShort(2, (short) 0);
			
			rs = ps.executeQuery();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MPartidas:comprobarMisDesafiosEnviados(long)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-40005);
		}
	}
	
	public void comprobarMisDesafiosRechazados(long uid)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM tresenraya.partidas WHERE desafiante = ? AND aceptado = ?");
			ps.setLong(1, uid);
			ps.setShort(2, (short) -1);
			
			rs = ps.executeQuery();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MPartidas:comprobarMisDesafiosRechazados(long)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-40006);
		}
	}
	
	public void comprobarMisDesafiosAceptados(long uid)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM tresenraya.partidas WHERE desafiante = ? AND aceptado = ?");
			ps.setLong(1, uid);
			ps.setShort(2, (short) 1);
			
			rs = ps.executeQuery();
			
		} catch (SQLException x) {

			System.err.println("Error SQL -> MPartidas:comprobarMisDesafiosAceptados(long)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-40007);
		}
	}
	
	public void getAllPartidas()
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM tresenraya.partidas");
			
			rs = ps.executeQuery();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MPartidas:getAllPartidas(void)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-40008);
		}
	}
	
	public void buscarPartidas()
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM tresenraya.partidas WHERE aceptado = ?");
			ps.setShort(1, (short) 1);
			
			rs = ps.executeQuery();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MPartidas:buscarPartidas()");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-40009);
		}
	}
	
	public void aceptarDesafio(String pid)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("UPDATE tresenraya.partidas SET aceptado = ? WHERE pid = ?");
			ps.setShort(1, (short) 1);
			ps.setString(2, pid);
			
			ps.executeUpdate();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MPartidas:aceptarDesafio(String)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-40010);
		}
	}
	
	public void rechazarDesafio(String pid)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("UPDATE tresenraya.partidas SET aceptado = ? WHERE pid = ?");
			ps.setShort(1, (short) -1);
			ps.setString(2, pid);
			
			ps.executeUpdate();
			
		} catch (SQLException x) {
	
			System.err.println("Error SQL -> MPartidas:rechazarDesafio(String)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-40011);
		}
	}
	
	public int countPartidasActivas()
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("SELECT COUNT(*) AS total FROM tresenraya.partidas WHERE aceptado = ?");
			ps.setShort(1, (short) 1);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
			{
				return rs.getInt("total");
			}
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MPartidas:countPartidasActivas(void)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-40012);
		}
		
		return 0;
	}
	
	public void vaciarTablaPartidas()
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("TRUNCATE TABLE tresenraya.partidas");
			
			ps.executeUpdate();
			
		} catch (SQLException x) {
		
			System.err.println("Error SQL -> MPartidas:vaciarTablaPartidas(void)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-40013);
		}
	}
}

