package modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class MActivaciones
{
	public
		long aid,
			 uid,
			 fechaLimite;
	
	public
		boolean avisado;
	
	public
		String clave;
	
	private Connection conexion;
	private ResultSet rs;
	
	public MActivaciones(Connection conexion)
	{
		this.conexion = conexion;
	}
	
	public boolean getProximaActivacion()
	{
		try
		{
			if (rs.next())
			{
				aid = rs.getLong("aid");
				this.uid = rs.getLong("uid");
				clave = rs.getString("clave");
				fechaLimite = rs.getTimestamp("fecha_limite").getTime();
				
				return true;
				
			} else {
				
				return false;
			}
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MActivaciones:getProximaActivacion()");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
			System.exit(-200);
		}
		
		return false;
	}
	
	public boolean getActivacionByUid(long uid)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM tresenraya.activaciones WHERE uid = ?");
			
			rs = ps.executeQuery();
			
			return getProximaActivacion();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MActivaciones:getActivacionByUid(long)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
		
			System.exit(-201);
		}
		
		return false;
	}
	
	public boolean getActivacionByAid(long aid)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM tresenraya.activaciones WHERE aid = ?");
			ps.setLong(1, aid);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
			{
				this.aid = rs.getLong("aid");
				uid = rs.getLong("uid");
//				intentos = rs.getInt("intentos");
//				fechaEnvio = rs.getLong("fecha_envio");
				clave = rs.getString("clave");
				
				return true;
				
			} else {
				
				return false;
			}
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MActivaciones:getActivacionByAid(long)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
		
			System.exit(-202);
		}
		
		return false;
	}
	
	public long registraActivacion(long uid, String clave, long fechaLimite)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("INSERT INTO tresenraya.activaciones (uid, clave, fecha_limite) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setLong(1, uid);
			ps.setString(2, clave);
			ps.setTimestamp(3, new Timestamp(fechaLimite));
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			
			return rs.getLong(1);
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MActivaciones:registraActivacion(long, String, long)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
		
			System.exit(-203);
		}
		
		return -1L;
	}
	
	public void borraActivacion()
	{
		borraActivacionByAid(aid);
	}
	
	public void borraActivacionByAid(long aid)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("DELETE FROM tresenraya.activaciones WHERE aid = ?");
			ps.setLong(1, aid);
			
			ps.executeUpdate();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MActivaciones:borraActivacionByAid(long)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
		
			System.exit(-204);
		}
	}
	
	public void getActivacionesFechaLimite(long timestamp)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM tresenraya.activaciones WHERE fecha_limite < ? AND avisado = ?");
			ps.setTimestamp(1, new Timestamp(timestamp));
			ps.setBoolean(2, false);
			
			rs = ps.executeQuery();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MActivaciones:getActivacionesFechaLimite(long)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
		
			System.exit(-205);
		}
	}
	
	public void getActivacionesParaEliminar(long timestamp)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("SELECT * FROM tresenraya.activaciones WHERE fecha_limite < ? AND avisado = ?");
			// restamos 3 días en milis a la timestamp actual, y los que sean mayor del tiempo límite, se eliminan
			ps.setTimestamp(1, new Timestamp(timestamp - (259200000))); // 3 días = 259200000 milisegundos
			ps.setBoolean(2, true);
			
			rs = ps.executeQuery();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MActivaciones:getActivacionesParaEliminar(long)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
		
			System.exit(-208);
		}
	}
	
	public void actualizarActivacion()
	{
		actualizarActivacion(aid, uid, clave, fechaLimite, avisado);
	}
	
	public void actualizarActivacion(long aid, long uid, String clave, long fechaLimite, boolean avisado)
	{
		try
		{
			PreparedStatement ps = conexion.prepareStatement("UPDATE tresenraya.activaciones SET aid = ?, uid = ?, clave = ?, fecha_limite = ?, avisado = ? WHERE aid = ?");
			ps.setLong(1, aid);
			ps.setLong(2, uid);
			ps.setString(3, clave);
			ps.setTimestamp(4, new Timestamp(fechaLimite));
			ps.setBoolean(5, avisado);
			ps.setLong(6, aid);
			
			ps.executeUpdate();
			
		} catch (SQLException x) {
			
			System.err.println("Error SQL -> MActivaciones:actualizarActivacion(long, long, String, long, boolean)");
			System.err.println("Mensaje de error -> " + x.getMessage());
			
			x.printStackTrace();
		
			System.exit(-206);
		}
	}
}
