<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*,java.lang.*, fr.bca.model.*, fr.bca.controller.ServerController, com.jcraft.jsch.*, java.io.*, java.net.*" %>
<%!
	private final String SED_REMOVING_SPECIAL_CHARACTERS = "| sed -r 's/\\x1B\\[([0-9]{1,2}(;[0-9]{1,2})?)?[mGK]//g'";

	private Result sshConnexion(String operation, String instanceName, Result result) throws JSchException, IOException {

		JSch shell = new JSch();
		Session session = shell.getSession(SSHUserJBossAdminV3.USER_CONNECTION, SSHUserJBossAdminV3.SERVER_ADMIN, SSHUserJBossAdminV3.PORT_CONNECTION);
		session.setUserInfo(new SSHUserJBossAdminV3("jbossexp"));
	
		session.connect();
	
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand(SSHUserJBossAdminV3.SCRIPT_ADMIN + " " + operation + " " + instanceName
				+ " " + SED_REMOVING_SPECIAL_CHARACTERS);
	
		InputStream in = channel.getInputStream();
		OutputStream out = channel.getOutputStream();
		((ChannelExec) channel).setErrStream(System.err);
	
		channel.connect();
	
		out.flush();
	
		byte[] tmp = new byte[1024];
		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
				result.setResultat(new String(tmp, 0, i));
				System.out.println(result.getResultat());
			}
			if (channel.isClosed()) {
				result.setCodeRetour("exit-status: " + channel.getExitStatus());
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ee) {
			}
		}
		channel.disconnect();
		session.disconnect();
	
		return result;
	}
	
	public boolean	is_online(String address, String server)
	{
		try {
			URL url = new URL("http://" + address + ":" + String.valueOf(8080 + 100 * Integer.parseInt(server.substring(server.length() - 1))));
			System.out.println("http://" + address + ":" + String.valueOf(8080 + 100 * Integer.parseInt(server.substring(server.length() - 1))));
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			conn.setReadTimeout(500);
			System.out.println(conn.getResponseCode());
			return (conn.getResponseCode() == 200);	
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return (false);
		}
	}
%>
<%
	int	nb;
	int	logonly = 0;
	String env = request.getParameter("env");

	List<String> dev = new ArrayList<String>();
	List<String> in = new ArrayList<String>();
	List<String> val = new ArrayList<String>();
	List<String> rec = new ArrayList<String>();
	List<String> pro = new ArrayList<String>();
	Hashtable<String,Integer> control = new Hashtable<String,Integer>();
	Hashtable<String,String> address = new Hashtable<String,String>();
	Hashtable<String,List<String>> servers = new Hashtable<String,java.util.List<String>>();
	
	dev.addAll(Arrays.asList("JP_APPDEV_03_3", "172.16.250.212"));//server list by env
	in.addAll(Arrays.asList("JP_APPINT_07_7", "172.16.250.218", "JP_APPINT_08_8", "172.16.250.221"));
	val.addAll(Arrays.asList("JP_APPVAL_01_1", "", "JP_APPVAL_02_2", ""));
	rec.addAll(Arrays.asList("JP_APPREC_01_1", "", "JP_APPREC_02_2", ""));
	pro.addAll(Arrays.asList("JP_APPPRO_01_1", "", "JP_APPPRO_02_2", ""));

	control.put("dev", 1);// can see server status, start and stop them
	control.put("int", 1);
	control.put("val", 0);
	control.put("rec", 0);
	control.put("pro", 0);
	
	servers.put("dev", dev);//alias (hashtable)
	servers.put("int", in);
	servers.put("val", val);
	servers.put("rec", rec);
	servers.put("pro", pro);

	address.put("dev", "http://s00i13i/");
	address.put("int", "http://s00i13i/");
	address.put("val", "http://s00i13v/");
	address.put("rec", "http://s00i13r/");
	address.put("pro", "http://s00i13/");
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
		<title>Servers Status</title>
		<!-- <link rel="shortcut icon" href="images/logo_bca.png"> -->
		<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
		<link rel="stylesheet" type="text/css" href="css/default.css" />
		<link rel="stylesheet" type="text/css" href="css/component.css" />
		
	</head>
	<body>
		
	<div class="main">
		<div  class="aligned"><a href="#" id="refresh_link" onclick="location.reload(true);">refresh</a></div>
		<br /><br />
		<h2 class="aligned">Serveurs 

<%
	if (request.getParameter("env").equals("dev"))
		out.print("de développement ");
	else if (request.getParameter("env").equals("int"))
		out.print("d'integration ");
	else if (request.getParameter("env").equals("val"))
		out.print("de validation ");
	else if (request.getParameter("env").equals("rec"))
		out.print("de recette ");
	else if (request.getParameter("env").equals("pro"))
		out.print("de production ");
%>
		:</h2>
		<br /><br /><br />
<%
	int	i = 0;
	boolean	is_online = true;
	boolean	is_control = false;
	List<String> servlist = servers.get(request.getParameter("env"));

	if (control.get(request.getParameter("env")) > 0)
		is_control = true;
	while (i < (servlist.size() / 2))
	{
		if (is_control)
		{
			if (!servlist.get(2 * i + 1).equals(""))
				is_online = is_online(servlist.get(2 * i + 1), servlist.get(2 * i));
			else
				is_online = sshConnexion("status_instances", servlist.get(2 * i), new Result()).getResultat().trim().endsWith("demarree");
		}
		out.print("<table class=\"col-xs-12 col-sm-9 col-md-7 col-lg-5 col-sm-offset-2 col-md-offset-3 col-lg-offset-4 aligned\"><tr><td width=\"96px;\"><div><span class=\"label label-");
		if (is_control)
		{
			if (is_online)
				out.print("success\">Online");
			else
				out.print("danger\">Offline");
		}
		else
			out.print("default\">Unknown");
		out.print("</span></div></td><td>" + servlist.get(2 * i) + "</td><td width=\"128px;\"><div class=\"btn-group\"><button type=\"button\" class=\"btn btn-default");
		if (is_online || !is_control)
			out.print(" disabled");
		out.print("\" onclick=\"Start('" + servlist.get(2 * i) + "');\">Start</button><button type=\"button\" class=\"btn btn-default");
		if (!is_online || !is_control)
			out.print(" disabled");
		out.print("\" onclick=\"ConfirmStop('" + servlist.get(2 * i) + "');\">Stop</button></div></td><td width=\"96px;\"><a href=\"" + address.get(request.getParameter("env")) + servlist.get(2 * i).substring(0, servlist.get(2 * i).length() - 2) +"/\" onclick=\"return loadIframe('contentFrame', this.href);\">logs</a></td></tr></table>");
		i++;
	}
%>



		<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>

		<!-- Include all compiled plugins (below), or include individual files as needed -->
		<!-- <script src="js/bootstrap.min.js"></script> -->	
		<script src="js/modernizr.custom.js"></script>		
		<script src="js/cbpHorizontalMenu.js"></script>			
		<!-- <script src="js/cbpHorizontalMenu.min.js"></script> -->
		<script src="js/app.js"></script>
		
	</div>		
		
	</body>
</html>