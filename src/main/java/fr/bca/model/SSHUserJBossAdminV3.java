package fr.bca.model;

import com.jcraft.jsch.UserInfo;

public class SSHUserJBossAdminV3 implements UserInfo {

	public static final String SERVER_ADMIN = "S00I210D";
	public static final String SCRIPT_ADMIN = "jbossadmin";
	public static final String USER_CONNECTION = "jbossexp";
	public static final Integer PORT_CONNECTION = 22;

	private String password;

	public SSHUserJBossAdminV3(String password) {
		this.password = password;
	}

	@Override
	public String getPassphrase() {
		return null;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean promptPassphrase(String arg0) {
		return true;
	}

	@Override
	public boolean promptPassword(String arg0) {
		return true;
	}

	@Override
	public boolean promptYesNo(String arg0) {
		return true;
	}

	@Override
	public void showMessage(String arg0) {
		System.out.println(arg0);

	}

}
