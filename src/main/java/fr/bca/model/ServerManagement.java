package fr.bca.model;

import java.io.Serializable;

public class ServerManagement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String instanceName;
	private String operation;
	private String url;

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
