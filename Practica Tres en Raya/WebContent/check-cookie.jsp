<%@page import="helpers.RecordarmeHelper"%>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%
	if (request.getAttribute("iniciado") == null)
	{
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
		return;
	}

	// Comprobar cookie
	String forward = String.valueOf(request.getAttribute("forward"));
	
	RecordarmeHelper hlpRecordarme = new RecordarmeHelper();
	if (hlpRecordarme.comprobarCookieRecordarme(request, response, session))
	{
		if (! forward.equals("null"))
		{
			response.sendRedirect(application.getContextPath() + "/" + forward);
			return;
		}
	}
%>
