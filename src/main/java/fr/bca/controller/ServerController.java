package fr.bca.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import org.apache.commons.codec.binary.Base64;

import fr.bca.model.Result;
import fr.bca.model.SSHUserJBossAdminV3;
import fr.bca.model.ServerManagement;

@RestController
public class ServerController {

	private final String SED_REMOVING_SPECIAL_CHARACTERS = "| sed -r 's/\\x1B\\[([0-9]{1,2}(;[0-9]{1,2})?)?[mGK]//g'";

	@ResponseBody
	@RequestMapping(value = "/server/management", method = RequestMethod.POST, headers = "Accept=application/json")
	public Result rebootPost(@RequestBody ServerManagement serverManagement) throws JSchException, IOException {
		System.out.println("POST " + serverManagement.getInstanceName());
		System.out.println("POST " + serverManagement.getOperation());
		Result res  = sshConnexion(serverManagement.getOperation(), serverManagement.getInstanceName(), new Result());
		res.setResultat(serverManagement.getOperation().toUpperCase() + " --> " + res.getResultat());
		return (res);
	}

	@ResponseBody
	@RequestMapping(value = "/application/info", method = RequestMethod.POST, headers = "Accept=application/json")
	public Result getNapStatus(@RequestBody ServerManagement serverManagement) throws JSchException, IOException {
		String auth;
		Result result = new Result();
		URL url;

		auth = "Basic " + new String(new Base64().encode("NAPBACK:NAPBACK".getBytes()));
		try {
			url = new URL(serverManagement.getUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty ("Authorization", auth);
			String res = IOUtils.toString(conn.getInputStream(), StandardCharsets.UTF_8);
		    result.setResultat(res);
		} catch (Exception e) {
			e.printStackTrace();
			return (null);
		}
		return (result);
	}

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
	
	
	
}