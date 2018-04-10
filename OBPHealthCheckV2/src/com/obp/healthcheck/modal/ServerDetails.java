package com.obp.healthcheck.modal;

import java.util.Map;

public class ServerDetails {
	private String servername;
	private Map<String, String> domainStat;
	private Map<String, String> deploymentStat;

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public Map<String, String> getDomainStat() {
		return domainStat;
	}

	public void setDomainStat(Map<String, String> domainStat) {
		this.domainStat = domainStat;
	}

	public Map<String, String> getDeploymentStat() {
		return deploymentStat;
	}

	public void setDeploymentStat(Map<String, String> deploymentStat) {
		this.deploymentStat = deploymentStat;
	}

	@Override
	public String toString() {
		return "ServerDetails [servername=" + servername + ", domainStat=" + domainStat + ", deploymentStat="
				+ deploymentStat + "]";
	}

}
