package com.obp.healthcheck.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;

import com.obp.healthcheck.modal.Component;
import com.obp.healthcheck.modal.ServerDetails;
import com.obp.healthcheck.util.MBeanServerConnectionUtil;

public class ServerListHelper implements Callable<ServerDetails> {
	private MBeanServerConnection connection;
	private static final ObjectName service;
	private static JMXConnector connector;
	private Component component;

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	static {
		try {
			service = new ObjectName(
					"com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
		} catch (MalformedObjectNameException e) {
			throw new AssertionError(e.getMessage());
		}
	}

	@Override
	public ServerDetails call() throws Exception {
		ServerDetails svr = new ServerDetails();
		try {
			svr.setServername(this.component.getName());
			MBeanServerConnectionUtil serverConnectionUtil = new MBeanServerConnectionUtil();
			connection = serverConnectionUtil.initConnection(this.component);

		} catch (Exception e) {
			Map<String, String> map = new HashMap<>();
			map.put("ERROR", "UNABLE TO CONNECT");
			svr.setDomainStat(map);
			return svr;
		}
		getServletData(svr);
		getDeploymentStatus(svr);
		return svr;
	}

	public void getServletData(ServerDetails svr) throws Exception {

		Map<String, String> map = new HashMap<>();
		ObjectName domain = (ObjectName) connection.getAttribute(service, "DomainConfiguration");
		ObjectName[] servers = (ObjectName[]) connection.getAttribute(domain, "Servers");
		for (ObjectName server : servers) {
			String sname = (String) connection.getAttribute(server, "Name");
			try {
				ObjectName ser = new ObjectName("com.bea:Name=" + sname + ",Location=" + sname + ",Type=ServerRuntime");
				weblogic.health.HealthState serverHealthState = (weblogic.health.HealthState) connection
						.getAttribute(ser, "HealthState");
				int hState = serverHealthState.getState();
				if (hState == weblogic.health.HealthState.HEALTH_OK)
					map.put(sname, "RUNNING");
				if (hState == weblogic.health.HealthState.HEALTH_WARN)
					map.put(sname, "WARNING");
				if (hState == weblogic.health.HealthState.HEALTH_CRITICAL)
					map.put(sname, "CRITICAL");
				if (hState == weblogic.health.HealthState.HEALTH_FAILED)
					map.put(sname, "FAILED");
				if (hState == weblogic.health.HealthState.HEALTH_OVERLOADED)
					map.put(sname, "OVERLOADED");
			} catch (Exception e) {
				map.put(sname, "SHUTDOWN");
			}
			svr.setDomainStat(map);
		}

	}

	public Map<String, String> getDeployments(ServerDetails svr) throws Exception {
		ObjectName serverRT = getDomainConfiguration();
		Map<String, String> map = new HashMap<>();
		ObjectName[] appRT = (ObjectName[]) connection.getAttribute(serverRT, "AppDeployments");
		for (ObjectName app : appRT) {
			String appName = (String) connection.getAttribute(app, "ApplicationName");
			if (appName.contains("_/")) {
				appName = appName.substring(appName.indexOf("_/") + 2);
			}
			if (appName.startsWith("obp") || appName.equals("em") || appName.equals("wsm-pm")
					|| appName.equals("oraclediagent")) {
				map.put(appName, "FAILED");
			}

		}
		return map;

	}

	public void getDeploymentStatus(ServerDetails svr) throws Exception {
		ObjectName[] serverRT = getServerRuntimes();
		Map<String, String> map = getDeployments(svr);
		String state = null;
		for (ObjectName ser : serverRT) {
			ObjectName[] appRT = (ObjectName[]) connection.getAttribute(ser, "ApplicationRuntimes");
			for (ObjectName app : appRT) {
				String appName = (String) connection.getAttribute(app, "Name");
				if (appName.contains("_/")) {
					appName = appName.substring(appName.indexOf("_/") + 2);
				}
				try {
					weblogic.health.HealthState healthState = (weblogic.health.HealthState) connection.getAttribute(app,
							"HealthState");
					if (healthState.getState() == weblogic.health.HealthState.HEALTH_OK)
						state = "OK";
					if (healthState.getState() == weblogic.health.HealthState.HEALTH_WARN)
						state = "WARNING";
					if (healthState.getState() == weblogic.health.HealthState.HEALTH_CRITICAL)
						state = "CRITICAL";
					if (healthState.getState() == weblogic.health.HealthState.HEALTH_FAILED)
						state = "FAILED";
					if (healthState.getState() == weblogic.health.HealthState.HEALTH_OVERLOADED)
						state = "OVERLOADED";
				} catch (Exception e) {
					state = "ERROR";
				}
				if (appName.startsWith("obp") || appName.equals("em") || appName.equals("wsm-pm")
						|| appName.equals("oraclediagent") || state.equals("FAILED")) {
					map.put(appName, state);
				}

			}
			svr.setDeploymentStat(map);
		}

	}

	public ObjectName[] getServerRuntimes() throws Exception {
		return (ObjectName[]) connection.getAttribute(service, "ServerRuntimes");
	}

	public ObjectName getDomainConfiguration() throws Exception {
		return (ObjectName) connection.getAttribute(service, "DomainConfiguration");
	}

}
