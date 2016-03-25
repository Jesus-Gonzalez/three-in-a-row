package utils;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import modelos.MPartidas;

public class TagPartidasActivas
extends SimpleTagSupport
{

	@Override
	public void doTag()
	throws JspException, IOException
	{
		JspContext context = getJspContext();
		JspWriter out = context.getOut();
		
		Connection conexion = (Connection) context.getAttribute("conexion", PageContext.SESSION_SCOPE);
		
		MPartidas mdlPartidas = new MPartidas(conexion);
		
		int count = mdlPartidas.countPartidasActivas();
		
		out.println(count);
		
	}
	
}
