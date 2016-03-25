<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="modelos.Usuario"%>
<%@page import="java.util.Calendar"%>
<%@page import="modelos.MUsuariosConectados"%>
<%@page import="modelos.Partida"%>
<%@page import="java.util.HashMap"%>
<%@page import="modelos.MUsuarios"%>
<%@page import="modelos.MPartidas"%>
<%@page import="java.sql.Connection"%>
<%@page import="helpers.RecordarmeHelper" %>
<%@page import="modelos.SesionUsuario"%>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>

<%!
	boolean existeUsuario = false;
%>

<%
	SesionUsuario s = (SesionUsuario) session.getAttribute("sesion");
	Connection conexion = (Connection) session.getAttribute("conexion");
	MUsuarios mdlUsuarios = new MUsuarios(conexion);
	Usuario usuario = new Usuario();

	Calendar fechaConexion = Calendar.getInstance(),
			 fechaRegistro = Calendar.getInstance();

	SimpleDateFormat sdf = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy 'a las' H:m:s '('z')'", new Locale("es", "ES"));
	String strFechaConexion = "", strFechaRegistro = "";
	
	long uid;
	boolean estaUsuarioConectado = false;
	
	try
	{	
		if (request.getParameter("uid") == null)
		{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
			
		} else {
			
			uid = Long.valueOf(request.getParameter("uid"));
		}
	
	} catch (NumberFormatException x) {
		
		response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		return;
	}
	
	mdlUsuarios.getUsuarioByUid(uid);

	if (mdlUsuarios.getProximoUsuario())
	{
		existeUsuario = true;

		usuario = new Usuario(mdlUsuarios); 
		
		fechaConexion.setTimeInMillis(usuario.fechaConexion);
		fechaRegistro.setTimeInMillis(usuario.fechaRegistro);

		strFechaConexion = sdf.format(fechaConexion.getTime());
		strFechaRegistro = sdf.format(fechaRegistro.getTime());
		
		MUsuariosConectados mdlUsuariosConectados = new MUsuariosConectados(conexion);
		estaUsuarioConectado = mdlUsuariosConectados.estaUsuarioConectado(usuario.uid);
	}
	
	if (existeUsuario)
	{
		request.setAttribute("titulo-pagina", "Perfil de " + usuario.nombre);
		
	} else {
		
		request.setAttribute("titulo-pagina", "No existe el usuario");
	}
	
	request.setAttribute("iniciado", true);
%>

	<%@ include file="check-cookie.jsp" %>

	<%@ include file="layout/header.jsp" %>

		<div class="container">
			<div class="row">
				<div class="col-sm-7 col-sm-offset-1 col-xs-12">
					
					<% if (existeUsuario) { %>

					<h1><i class="fa fa-arrow-right"></i> Perfil de <%= usuario.nombre %> <span class="flag-icon flag-icon-<%= usuario.pais.toLowerCase() %>"></span></h1>
					
					<h2><i class="fa fa-clock-o"></i> Datos horarios</h2>
						<div id="perfil-datos-fechas">
						<p><strong>Fecha última conexión:</strong> <%= (estaUsuarioConectado) ? "<span class='text-success'>Actualmente conectado</span>" : strFechaConexion %></p>
						<p><strong>Fecha de registro:</strong> <%= strFechaRegistro %></p>
					</div>

					<h2><i class="fa fa-gamepad"></i> Datos de Partidas</h2>
					<div id="perfil-datos-partidas">
						<p><strong>Partidas jugadas:</strong> <%= usuario.datosPartidas[Usuario.JUGADAS]  %></p>
						<p><strong>Partidas ganadas:</strong> <%= usuario.datosPartidas[Usuario.GANADAS] %></p>
						<p><strong>Partidas perdidas:</strong> <%= usuario.datosPartidas[Usuario.PERDIDAS] %></p>
						<p><strong>Partidas empatadas:</strong> <%= usuario.datosPartidas[Usuario.EMPATADAS] %></p>
					</div>


					<h2><i class="fa fa-user"></i> Datos de usuario</h2>
					<div id="perfil-datos-usuario">
						<p><strong>Nombre:</strong> <%= usuario.nombre %></p>
						<p><strong>País:</strong>
							<select class="form-control" disabled>
									<option value="AF"<%= (usuario.pais.equalsIgnoreCase("AF")) ? " selected" : "" %>>Afganistán
									<option value="AL"<%= (usuario.pais.equalsIgnoreCase("AL")) ? " selected" : "" %>>Albania
									<option value="DE"<%= (usuario.pais.equalsIgnoreCase("DE")) ? " selected" : "" %>>Alemania
									<option value="AD"<%= (usuario.pais.equalsIgnoreCase("AD")) ? " selected" : "" %>>Andorra
									<option value="AO"<%= (usuario.pais.equalsIgnoreCase("AO")) ? " selected" : "" %>>Angola
									<option value="AI"<%= (usuario.pais.equalsIgnoreCase("AI")) ? " selected" : "" %>>Anguilla
									<option value="AQ"<%= (usuario.pais.equalsIgnoreCase("AQ")) ? " selected" : "" %>>Antártida
									<option value="AG"<%= (usuario.pais.equalsIgnoreCase("AG")) ? " selected" : "" %>>Antigua y Barbuda
									<option value="AN"<%= (usuario.pais.equalsIgnoreCase("AN")) ? " selected" : "" %>>Antillas Holandesas
									<option value="SA"<%= (usuario.pais.equalsIgnoreCase("SA")) ? " selected" : "" %>>Arabia Saudí
									<option value="DZ"<%= (usuario.pais.equalsIgnoreCase("DZ")) ? " selected" : "" %>>Argelia
									<option value="AR"<%= (usuario.pais.equalsIgnoreCase("AR")) ? " selected" : "" %>>Argentina
									<option value="AM"<%= (usuario.pais.equalsIgnoreCase("AM")) ? " selected" : "" %>>Armenia
									<option value="AW"<%= (usuario.pais.equalsIgnoreCase("AW")) ? " selected" : "" %>>Aruba
									<option value="AU"<%= (usuario.pais.equalsIgnoreCase("AU")) ? " selected" : "" %>>Australia
									<option value="AT"<%= (usuario.pais.equalsIgnoreCase("AT")) ? " selected" : "" %>>Austria
									<option value="AZ"<%= (usuario.pais.equalsIgnoreCase("AZ")) ? " selected" : "" %>>Azerbaiyán
									<option value="BS"<%= (usuario.pais.equalsIgnoreCase("BS")) ? " selected" : "" %>>Bahamas
									<option value="BH"<%= (usuario.pais.equalsIgnoreCase("BH")) ? " selected" : "" %>>Bahrein
									<option value="BD"<%= (usuario.pais.equalsIgnoreCase("BD")) ? " selected" : "" %>>Bangladesh
									<option value="BB"<%= (usuario.pais.equalsIgnoreCase("BB")) ? " selected" : "" %>>Barbados
									<option value="BE"<%= (usuario.pais.equalsIgnoreCase("BE")) ? " selected" : "" %>>Bélgica
									<option value="BZ"<%= (usuario.pais.equalsIgnoreCase("BZ")) ? " selected" : "" %>>Belice
									<option value="BJ"<%= (usuario.pais.equalsIgnoreCase("BJ")) ? " selected" : "" %>>Benin
									<option value="BM"<%= (usuario.pais.equalsIgnoreCase("BM")) ? " selected" : "" %>>Bermudas
									<option value="BY"<%= (usuario.pais.equalsIgnoreCase("BY")) ? " selected" : "" %>>Bielorrusia
									<option value="MM"<%= (usuario.pais.equalsIgnoreCase("MM")) ? " selected" : "" %>>Birmania
									<option value="BO"<%= (usuario.pais.equalsIgnoreCase("BO")) ? " selected" : "" %>>Bolivia
									<option value="BA"<%= (usuario.pais.equalsIgnoreCase("BA")) ? " selected" : "" %>>Bosnia y Herzegovina
									<option value="BW"<%= (usuario.pais.equalsIgnoreCase("BW")) ? " selected" : "" %>>Botswana
									<option value="BR"<%= (usuario.pais.equalsIgnoreCase("BR")) ? " selected" : "" %>>Brasil
									<option value="BN"<%= (usuario.pais.equalsIgnoreCase("BN")) ? " selected" : "" %>>Brunei
									<option value="BG"<%= (usuario.pais.equalsIgnoreCase("BG")) ? " selected" : "" %>>Bulgaria
									<option value="BF"<%= (usuario.pais.equalsIgnoreCase("BF")) ? " selected" : "" %>>Burkina Faso
									<option value="BI"<%= (usuario.pais.equalsIgnoreCase("BI")) ? " selected" : "" %>>Burundi
									<option value="BT"<%= (usuario.pais.equalsIgnoreCase("BT")) ? " selected" : "" %>>Bután
									<option value="CV"<%= (usuario.pais.equalsIgnoreCase("CV")) ? " selected" : "" %>>Cabo Verde
									<option value="KH"<%= (usuario.pais.equalsIgnoreCase("KH")) ? " selected" : "" %>>Camboya
									<option value="CM"<%= (usuario.pais.equalsIgnoreCase("CM")) ? " selected" : "" %>>Camerún
									<option value="CA"<%= (usuario.pais.equalsIgnoreCase("CA")) ? " selected" : "" %>>Canadá
									<option value="TD"<%= (usuario.pais.equalsIgnoreCase("TD")) ? " selected" : "" %>>Chad
									<option value="CL"<%= (usuario.pais.equalsIgnoreCase("CL")) ? " selected" : "" %>>Chile
									<option value="CN"<%= (usuario.pais.equalsIgnoreCase("CN")) ? " selected" : "" %>>China
									<option value="CY"<%= (usuario.pais.equalsIgnoreCase("CY")) ? " selected" : "" %>>Chipre
									<option value="VA"<%= (usuario.pais.equalsIgnoreCase("VA")) ? " selected" : "" %>>Ciudad del Vaticano (Santa Sede)
									<option value="CO"<%= (usuario.pais.equalsIgnoreCase("CO")) ? " selected" : "" %>>Colombia
									<option value="KM"<%= (usuario.pais.equalsIgnoreCase("KM")) ? " selected" : "" %>>Comores
									<option value="CG"<%= (usuario.pais.equalsIgnoreCase("CG")) ? " selected" : "" %>>Congo
									<option value="CD"<%= (usuario.pais.equalsIgnoreCase("CD")) ? " selected" : "" %>>Congo, República Democrática del
									<option value="KR"<%= (usuario.pais.equalsIgnoreCase("KR")) ? " selected" : "" %>>Corea
									<option value="KP"<%= (usuario.pais.equalsIgnoreCase("KP")) ? " selected" : "" %>>Corea del Norte
									<option value="CI"<%= (usuario.pais.equalsIgnoreCase("CI")) ? " selected" : "" %>>Costa de Marfíl
									<option value="CR"<%= (usuario.pais.equalsIgnoreCase("CR")) ? " selected" : "" %>>Costa Rica
									<option value="HR"<%= (usuario.pais.equalsIgnoreCase("HR")) ? " selected" : "" %>>Croacia (Hrvatska)
									<option value="CU"<%= (usuario.pais.equalsIgnoreCase("CU")) ? " selected" : "" %>>Cuba
									<option value="DK"<%= (usuario.pais.equalsIgnoreCase("DK")) ? " selected" : "" %>>Dinamarca
									<option value="DJ"<%= (usuario.pais.equalsIgnoreCase("DJ")) ? " selected" : "" %>>Djibouti
									<option value="DM"<%= (usuario.pais.equalsIgnoreCase("DM")) ? " selected" : "" %>>Dominica
									<option value="EC"<%= (usuario.pais.equalsIgnoreCase("EC")) ? " selected" : "" %>>Ecuador
									<option value="EG"<%= (usuario.pais.equalsIgnoreCase("EG")) ? " selected" : "" %>>Egipto
									<option value="SV"<%= (usuario.pais.equalsIgnoreCase("SV")) ? " selected" : "" %>>El Salvador
									<option value="AE"<%= (usuario.pais.equalsIgnoreCase("AE")) ? " selected" : "" %>>Emiratos Árabes Unidos
									<option value="ER"<%= (usuario.pais.equalsIgnoreCase("ER")) ? " selected" : "" %>>Eritrea
									<option value="SI"<%= (usuario.pais.equalsIgnoreCase("SI")) ? " selected" : "" %>>Eslovenia
									<option value="ES"<%= (usuario.pais.equalsIgnoreCase("ES")) ? " selected" : "" %>>España
									<option value="US"<%= (usuario.pais.equalsIgnoreCase("US")) ? " selected" : "" %>>Estados Unidos
									<option value="EE"<%= (usuario.pais.equalsIgnoreCase("EE")) ? " selected" : "" %>>Estonia
									<option value="ET"<%= (usuario.pais.equalsIgnoreCase("ET")) ? " selected" : "" %>>Etiopía
									<option value="FJ"<%= (usuario.pais.equalsIgnoreCase("FJ")) ? " selected" : "" %>>Fiji
									<option value="PH"<%= (usuario.pais.equalsIgnoreCase("PH")) ? " selected" : "" %>>Filipinas
									<option value="FI"<%= (usuario.pais.equalsIgnoreCase("FI")) ? " selected" : "" %>>Finlandia
									<option value="FR"<%= (usuario.pais.equalsIgnoreCase("FR")) ? " selected" : "" %>>Francia
									<option value="GA"<%= (usuario.pais.equalsIgnoreCase("GA")) ? " selected" : "" %>>Gabón
									<option value="GM"<%= (usuario.pais.equalsIgnoreCase("GM")) ? " selected" : "" %>>Gambia
									<option value="GE"<%= (usuario.pais.equalsIgnoreCase("GE")) ? " selected" : "" %>>Georgia
									<option value="GH"<%= (usuario.pais.equalsIgnoreCase("GH")) ? " selected" : "" %>>Ghana
									<option value="GI"<%= (usuario.pais.equalsIgnoreCase("GI")) ? " selected" : "" %>>Gibraltar
									<option value="GD"<%= (usuario.pais.equalsIgnoreCase("GD")) ? " selected" : "" %>>Granada
									<option value="GR"<%= (usuario.pais.equalsIgnoreCase("GR")) ? " selected" : "" %>>Grecia
									<option value="GL"<%= (usuario.pais.equalsIgnoreCase("GL")) ? " selected" : "" %>>Groenlandia
									<option value="GP"<%= (usuario.pais.equalsIgnoreCase("GP")) ? " selected" : "" %>>Guadalupe
									<option value="GU"<%= (usuario.pais.equalsIgnoreCase("GU")) ? " selected" : "" %>>Guam
									<option value="GT"<%= (usuario.pais.equalsIgnoreCase("GT")) ? " selected" : "" %>>Guatemala
									<option value="GY"<%= (usuario.pais.equalsIgnoreCase("GY")) ? " selected" : "" %>>Guayana
									<option value="GF"<%= (usuario.pais.equalsIgnoreCase("GF")) ? " selected" : "" %>>Guayana Francesa
									<option value="GN"<%= (usuario.pais.equalsIgnoreCase("GN")) ? " selected" : "" %>>Guinea
									<option value="GQ"<%= (usuario.pais.equalsIgnoreCase("GQ")) ? " selected" : "" %>>Guinea Ecuatorial
									<option value="GW"<%= (usuario.pais.equalsIgnoreCase("GW")) ? " selected" : "" %>>Guinea-Bissau
									<option value="HT"<%= (usuario.pais.equalsIgnoreCase("HT")) ? " selected" : "" %>>Haití
									<option value="HN"<%= (usuario.pais.equalsIgnoreCase("HN")) ? " selected" : "" %>>Honduras
									<option value="HU"<%= (usuario.pais.equalsIgnoreCase("HU")) ? " selected" : "" %>>Hungría
									<option value="IN"<%= (usuario.pais.equalsIgnoreCase("IN")) ? " selected" : "" %>>India
									<option value="ID"<%= (usuario.pais.equalsIgnoreCase("ID")) ? " selected" : "" %>>Indonesia
									<option value="IQ"<%= (usuario.pais.equalsIgnoreCase("IQ")) ? " selected" : "" %>>Irak
									<option value="IR"<%= (usuario.pais.equalsIgnoreCase("IR")) ? " selected" : "" %>>Irán
									<option value="IE"<%= (usuario.pais.equalsIgnoreCase("IE")) ? " selected" : "" %>>Irlanda
									<option value="BV"<%= (usuario.pais.equalsIgnoreCase("BV")) ? " selected" : "" %>>Isla Bouvet
									<option value="CX"<%= (usuario.pais.equalsIgnoreCase("CX")) ? " selected" : "" %>>Isla de Christmas
									<option value="IS"<%= (usuario.pais.equalsIgnoreCase("IS")) ? " selected" : "" %>>Islandia
									<option value="KY"<%= (usuario.pais.equalsIgnoreCase("KY")) ? " selected" : "" %>>Islas Caimán
									<option value="CK"<%= (usuario.pais.equalsIgnoreCase("CK")) ? " selected" : "" %>>Islas Cook
									<option value="CC"<%= (usuario.pais.equalsIgnoreCase("CC")) ? " selected" : "" %>>Islas de Cocos o Keeling
									<option value="FO"<%= (usuario.pais.equalsIgnoreCase("FO")) ? " selected" : "" %>>Islas Faroe
									<option value="HM"<%= (usuario.pais.equalsIgnoreCase("HM")) ? " selected" : "" %>>Islas Heard y McDonald
									<option value="FK"<%= (usuario.pais.equalsIgnoreCase("FK")) ? " selected" : "" %>>Islas Malvinas
									<option value="MP"<%= (usuario.pais.equalsIgnoreCase("MP")) ? " selected" : "" %>>Islas Marianas del Norte
									<option value="MH"<%= (usuario.pais.equalsIgnoreCase("MH")) ? " selected" : "" %>>Islas Marshall
									<option value="UM"<%= (usuario.pais.equalsIgnoreCase("UM")) ? " selected" : "" %>>Islas menores de Estados Unidos
									<option value="PW"<%= (usuario.pais.equalsIgnoreCase("PW")) ? " selected" : "" %>>Islas Palau
									<option value="SB"<%= (usuario.pais.equalsIgnoreCase("SB")) ? " selected" : "" %>>Islas Salomón
									<option value="SJ"<%= (usuario.pais.equalsIgnoreCase("SJ")) ? " selected" : "" %>>Islas Svalbard y Jan Mayen
									<option value="TK"<%= (usuario.pais.equalsIgnoreCase("TK")) ? " selected" : "" %>>Islas Tokelau
									<option value="TC"<%= (usuario.pais.equalsIgnoreCase("TC")) ? " selected" : "" %>>Islas Turks y Caicos
									<option value="VI"<%= (usuario.pais.equalsIgnoreCase("VI")) ? " selected" : "" %>>Islas Vírgenes (EEUU)
									<option value="VG"<%= (usuario.pais.equalsIgnoreCase("VG")) ? " selected" : "" %>>Islas Vírgenes (Reino Unido)
									<option value="WF"<%= (usuario.pais.equalsIgnoreCase("WF")) ? " selected" : "" %>>Islas Wallis y Futuna
									<option value="IL"<%= (usuario.pais.equalsIgnoreCase("IL")) ? " selected" : "" %>>Israel
									<option value="IT"<%= (usuario.pais.equalsIgnoreCase("IT")) ? " selected" : "" %>>Italia
									<option value="JM"<%= (usuario.pais.equalsIgnoreCase("JM")) ? " selected" : "" %>>Jamaica
									<option value="JP"<%= (usuario.pais.equalsIgnoreCase("JP")) ? " selected" : "" %>>Japón
									<option value="JO"<%= (usuario.pais.equalsIgnoreCase("JO")) ? " selected" : "" %>>Jordania
									<option value="KZ"<%= (usuario.pais.equalsIgnoreCase("KZ")) ? " selected" : "" %>>Kazajistán
									<option value="KE"<%= (usuario.pais.equalsIgnoreCase("KE")) ? " selected" : "" %>>Kenia
									<option value="KG"<%= (usuario.pais.equalsIgnoreCase("KG")) ? " selected" : "" %>>Kirguizistán
									<option value="KI"<%= (usuario.pais.equalsIgnoreCase("KI")) ? " selected" : "" %>>Kiribati
									<option value="KW"<%= (usuario.pais.equalsIgnoreCase("KW")) ? " selected" : "" %>>Kuwait
									<option value="LA"<%= (usuario.pais.equalsIgnoreCase("LA")) ? " selected" : "" %>>Laos
									<option value="LS"<%= (usuario.pais.equalsIgnoreCase("LS")) ? " selected" : "" %>>Lesotho
									<option value="LV"<%= (usuario.pais.equalsIgnoreCase("LV")) ? " selected" : "" %>>Letonia
									<option value="LB"<%= (usuario.pais.equalsIgnoreCase("LB")) ? " selected" : "" %>>Líbano
									<option value="LR"<%= (usuario.pais.equalsIgnoreCase("LR")) ? " selected" : "" %>>Liberia
									<option value="LY"<%= (usuario.pais.equalsIgnoreCase("LY")) ? " selected" : "" %>>Libia
									<option value="LI"<%= (usuario.pais.equalsIgnoreCase("LI")) ? " selected" : "" %>>Liechtenstein
									<option value="LT"<%= (usuario.pais.equalsIgnoreCase("LT")) ? " selected" : "" %>>Lituania
									<option value="LU"<%= (usuario.pais.equalsIgnoreCase("LU")) ? " selected" : "" %>>Luxemburgo
									<option value="MK"<%= (usuario.pais.equalsIgnoreCase("MK")) ? " selected" : "" %>>Macedonia, Ex-República Yugoslava de
									<option value="MG"<%= (usuario.pais.equalsIgnoreCase("MG")) ? " selected" : "" %>>Madagascar
									<option value="MY"<%= (usuario.pais.equalsIgnoreCase("MY")) ? " selected" : "" %>>Malasia
									<option value="MW"<%= (usuario.pais.equalsIgnoreCase("MW")) ? " selected" : "" %>>Malawi
									<option value="MV"<%= (usuario.pais.equalsIgnoreCase("MV")) ? " selected" : "" %>>Maldivas
									<option value="ML"<%= (usuario.pais.equalsIgnoreCase("ML")) ? " selected" : "" %>>Malí
									<option value="MT"<%= (usuario.pais.equalsIgnoreCase("MT")) ? " selected" : "" %>>Malta
									<option value="MA"<%= (usuario.pais.equalsIgnoreCase("MA")) ? " selected" : "" %>>Marruecos
									<option value="MQ"<%= (usuario.pais.equalsIgnoreCase("MQ")) ? " selected" : "" %>>Martinica
									<option value="MU"<%= (usuario.pais.equalsIgnoreCase("MU")) ? " selected" : "" %>>Mauricio
									<option value="MR"<%= (usuario.pais.equalsIgnoreCase("MR")) ? " selected" : "" %>>Mauritania
									<option value="YT"<%= (usuario.pais.equalsIgnoreCase("YT")) ? " selected" : "" %>>Mayotte
									<option value="MX"<%= (usuario.pais.equalsIgnoreCase("MX")) ? " selected" : "" %>>México
									<option value="FM"<%= (usuario.pais.equalsIgnoreCase("FM")) ? " selected" : "" %>>Micronesia
									<option value="MD"<%= (usuario.pais.equalsIgnoreCase("MD")) ? " selected" : "" %>>Moldavia
									<option value="MC"<%= (usuario.pais.equalsIgnoreCase("MC")) ? " selected" : "" %>>Mónaco
									<option value="MN"<%= (usuario.pais.equalsIgnoreCase("MN")) ? " selected" : "" %>>Mongolia
									<option value="MS"<%= (usuario.pais.equalsIgnoreCase("MS")) ? " selected" : "" %>>Montserrat
									<option value="MZ"<%= (usuario.pais.equalsIgnoreCase("MZ")) ? " selected" : "" %>>Mozambique
									<option value="NA"<%= (usuario.pais.equalsIgnoreCase("NA")) ? " selected" : "" %>>Namibia
									<option value="NR"<%= (usuario.pais.equalsIgnoreCase("NR")) ? " selected" : "" %>>Nauru
									<option value="NP"<%= (usuario.pais.equalsIgnoreCase("NP")) ? " selected" : "" %>>Nepal
									<option value="NI"<%= (usuario.pais.equalsIgnoreCase("NI")) ? " selected" : "" %>>Nicaragua
									<option value="NE"<%= (usuario.pais.equalsIgnoreCase("NE")) ? " selected" : "" %>>Níger
									<option value="NG"<%= (usuario.pais.equalsIgnoreCase("NG")) ? " selected" : "" %>>Nigeria
									<option value="NU"<%= (usuario.pais.equalsIgnoreCase("NU")) ? " selected" : "" %>>Niue
									<option value="NF"<%= (usuario.pais.equalsIgnoreCase("NF")) ? " selected" : "" %>>Norfolk
									<option value="NO"<%= (usuario.pais.equalsIgnoreCase("NO")) ? " selected" : "" %>>Noruega
									<option value="NC"<%= (usuario.pais.equalsIgnoreCase("NC")) ? " selected" : "" %>>Nueva Caledonia
									<option value="NZ"<%= (usuario.pais.equalsIgnoreCase("NZ")) ? " selected" : "" %>>Nueva Zelanda
									<option value="OM"<%= (usuario.pais.equalsIgnoreCase("OM")) ? " selected" : "" %>>Omán
									<option value="NL"<%= (usuario.pais.equalsIgnoreCase("NL")) ? " selected" : "" %>>Países Bajos
									<option value="PA"<%= (usuario.pais.equalsIgnoreCase("PA")) ? " selected" : "" %>>Panamá
									<option value="PG"<%= (usuario.pais.equalsIgnoreCase("PG")) ? " selected" : "" %>>Papúa Nueva Guinea
									<option value="PK"<%= (usuario.pais.equalsIgnoreCase("PK")) ? " selected" : "" %>>Paquistán
									<option value="PY"<%= (usuario.pais.equalsIgnoreCase("PY")) ? " selected" : "" %>>Paraguay
									<option value="PE"<%= (usuario.pais.equalsIgnoreCase("PE")) ? " selected" : "" %>>Perú
									<option value="PN"<%= (usuario.pais.equalsIgnoreCase("PN")) ? " selected" : "" %>>Pitcairn
									<option value="PF"<%= (usuario.pais.equalsIgnoreCase("PF")) ? " selected" : "" %>>Polinesia Francesa
									<option value="PL"<%= (usuario.pais.equalsIgnoreCase("PL")) ? " selected" : "" %>>Polonia
									<option value="PT"<%= (usuario.pais.equalsIgnoreCase("PT")) ? " selected" : "" %>>Portugal
									<option value="PR"<%= (usuario.pais.equalsIgnoreCase("PR")) ? " selected" : "" %>>Puerto Rico
									<option value="QA"<%= (usuario.pais.equalsIgnoreCase("QA")) ? " selected" : "" %>>Qatar
									<option value="UK"<%= (usuario.pais.equalsIgnoreCase("UK")) ? " selected" : "" %>>Reino Unido
									<option value="CF"<%= (usuario.pais.equalsIgnoreCase("CF")) ? " selected" : "" %>>República Centroafricana
									<option value="CZ"<%= (usuario.pais.equalsIgnoreCase("CZ")) ? " selected" : "" %>>República Checa
									<option value="ZA"<%= (usuario.pais.equalsIgnoreCase("ZA")) ? " selected" : "" %>>República de Sudáfrica
									<option value="DO"<%= (usuario.pais.equalsIgnoreCase("DO")) ? " selected" : "" %>>República Dominicana
									<option value="SK"<%= (usuario.pais.equalsIgnoreCase("SK")) ? " selected" : "" %>>República Eslovaca
									<option value="RE"<%= (usuario.pais.equalsIgnoreCase("RE")) ? " selected" : "" %>>Reunión
									<option value="RW"<%= (usuario.pais.equalsIgnoreCase("RW")) ? " selected" : "" %>>Ruanda
									<option value="RO"<%= (usuario.pais.equalsIgnoreCase("RO")) ? " selected" : "" %>>Rumania
									<option value="RU"<%= (usuario.pais.equalsIgnoreCase("RU")) ? " selected" : "" %>>Rusia
									<option value="EH"<%= (usuario.pais.equalsIgnoreCase("EH")) ? " selected" : "" %>>Sahara Occidental
									<option value="KN"<%= (usuario.pais.equalsIgnoreCase("KN")) ? " selected" : "" %>>Saint Kitts y Nevis
									<option value="WS"<%= (usuario.pais.equalsIgnoreCase("WS")) ? " selected" : "" %>>Samoa
									<option value="AS"<%= (usuario.pais.equalsIgnoreCase("AS")) ? " selected" : "" %>>Samoa Americana
									<option value="SM"<%= (usuario.pais.equalsIgnoreCase("SM")) ? " selected" : "" %>>San Marino
									<option value="VC"<%= (usuario.pais.equalsIgnoreCase("VC")) ? " selected" : "" %>>San Vicente y Granadinas
									<option value="SH"<%= (usuario.pais.equalsIgnoreCase("SH")) ? " selected" : "" %>>Santa Helena
									<option value="LC"<%= (usuario.pais.equalsIgnoreCase("LC")) ? " selected" : "" %>>Santa Lucía
									<option value="ST"<%= (usuario.pais.equalsIgnoreCase("ST")) ? " selected" : "" %>>Santo Tomé y Príncipe
									<option value="SN"<%= (usuario.pais.equalsIgnoreCase("SN")) ? " selected" : "" %>>Senegal
									<option value="SC"<%= (usuario.pais.equalsIgnoreCase("SC")) ? " selected" : "" %>>Seychelles
									<option value="SL"<%= (usuario.pais.equalsIgnoreCase("SL")) ? " selected" : "" %>>Sierra Leona
									<option value="SG"<%= (usuario.pais.equalsIgnoreCase("SG")) ? " selected" : "" %>>Singapur
									<option value="SY"<%= (usuario.pais.equalsIgnoreCase("SY")) ? " selected" : "" %>>Siria
									<option value="SO"<%= (usuario.pais.equalsIgnoreCase("SO")) ? " selected" : "" %>>Somalia
									<option value="LK"<%= (usuario.pais.equalsIgnoreCase("LK")) ? " selected" : "" %>>Sri Lanka
									<option value="PM"<%= (usuario.pais.equalsIgnoreCase("PM")) ? " selected" : "" %>>St Pierre y Miquelon
									<option value="SZ"<%= (usuario.pais.equalsIgnoreCase("SZ")) ? " selected" : "" %>>Suazilandia
									<option value="SD"<%= (usuario.pais.equalsIgnoreCase("SD")) ? " selected" : "" %>>Sudán
									<option value="SE"<%= (usuario.pais.equalsIgnoreCase("SE")) ? " selected" : "" %>>Suecia
									<option value="CH"<%= (usuario.pais.equalsIgnoreCase("CH")) ? " selected" : "" %>>Suiza
									<option value="SR"<%= (usuario.pais.equalsIgnoreCase("SR")) ? " selected" : "" %>>Surinam
									<option value="TH"<%= (usuario.pais.equalsIgnoreCase("TH")) ? " selected" : "" %>>Tailandia
									<option value="TW"<%= (usuario.pais.equalsIgnoreCase("TW")) ? " selected" : "" %>>Taiwán
									<option value="TZ"<%= (usuario.pais.equalsIgnoreCase("TZ")) ? " selected" : "" %>>Tanzania
									<option value="TJ"<%= (usuario.pais.equalsIgnoreCase("TJ")) ? " selected" : "" %>>Tayikistán
									<option value="TF"<%= (usuario.pais.equalsIgnoreCase("TF")) ? " selected" : "" %>>Territorios franceses del Sur
									<option value="TP"<%= (usuario.pais.equalsIgnoreCase("TP")) ? " selected" : "" %>>Timor Oriental
									<option value="TG"<%= (usuario.pais.equalsIgnoreCase("TG")) ? " selected" : "" %>>Togo
									<option value="TO"<%= (usuario.pais.equalsIgnoreCase("TO")) ? " selected" : "" %>>Tonga
									<option value="TT"<%= (usuario.pais.equalsIgnoreCase("TT")) ? " selected" : "" %>>Trinidad y Tobago
									<option value="TN"<%= (usuario.pais.equalsIgnoreCase("TN")) ? " selected" : "" %>>Túnez
									<option value="TM"<%= (usuario.pais.equalsIgnoreCase("TM")) ? " selected" : "" %>>Turkmenistán
									<option value="TR"<%= (usuario.pais.equalsIgnoreCase("TR")) ? " selected" : "" %>>Turquía
									<option value="TV"<%= (usuario.pais.equalsIgnoreCase("TV")) ? " selected" : "" %>>Tuvalu
									<option value="UA"<%= (usuario.pais.equalsIgnoreCase("UA")) ? " selected" : "" %>>Ucrania
									<option value="UG"<%= (usuario.pais.equalsIgnoreCase("UG")) ? " selected" : "" %>>Uganda
									<option value="UY"<%= (usuario.pais.equalsIgnoreCase("UY")) ? " selected" : "" %>>Uruguay
									<option value="UZ"<%= (usuario.pais.equalsIgnoreCase("UZ")) ? " selected" : "" %>>Uzbekistán
									<option value="VU"<%= (usuario.pais.equalsIgnoreCase("VU")) ? " selected" : "" %>>Vanuatu
									<option value="VE"<%= (usuario.pais.equalsIgnoreCase("VE")) ? " selected" : "" %>>Venezuela
									<option value="VN"<%= (usuario.pais.equalsIgnoreCase("VN")) ? " selected" : "" %>>Vietnam
									<option value="YE"<%= (usuario.pais.equalsIgnoreCase("YE")) ? " selected" : "" %>>Yemen
									<option value="YU"<%= (usuario.pais.equalsIgnoreCase("YU")) ? " selected" : "" %>>Yugoslavia
									<option value="ZM"<%= (usuario.pais.equalsIgnoreCase("ZM")) ? " selected" : "" %>>Zambia
									<option value="ZW"<%= (usuario.pais.equalsIgnoreCase("ZW")) ? " selected" : "" %>>Zimbabue
								</select>
							</p>
						</div>
					<% } else { %>
						
						<h1 class="text-center text-danger">Usuario Inexistente</h1>

					<% } %>

				</div>

				<%@ include file="layout/barra-lateral.jsp" %>

			</div>
		</div>


	<%@ include file="layout/footer.jsp" %>