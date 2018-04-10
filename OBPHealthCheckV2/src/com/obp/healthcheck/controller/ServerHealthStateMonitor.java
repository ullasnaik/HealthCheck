package com.obp.healthcheck.controller;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;

import org.springframework.beans.factory.annotation.Autowired;

import com.obp.healthcheck.modal.Component;
import com.obp.healthcheck.modal.Components;
import com.obp.healthcheck.util.MBeanServerConnectionUtil;

import weblogic.management.runtime.ComponentRuntimeMBean;

public class ServerHealthStateMonitor {
	@Autowired
	private static Components components;
	private static MBeanServerConnection connection;
	private static JMXConnector connector;
	private static final ObjectName service;

	// Initializing the object name for DomainRuntimeServiceMBean
	// so it can be used throughout the class.
	static {
		try {
			service = new ObjectName(
					"com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
		} catch (MalformedObjectNameException e) {
			throw new AssertionError(e.getMessage());
		}
	}

	/*
	 * Get an array of ServerRuntimeMBeans
	 */
	public static ObjectName[] getServerRuntimes() throws Exception {
		return (ObjectName[]) connection.getAttribute(service, "ServerRuntimes");
	}

	/*
	 * Get an array of WebAppComponentRuntimeMBeans
	 */
	public void getServletData() throws Exception {
		ObjectName domain = (ObjectName) connection.getAttribute(service, "DomainConfiguration");
		ObjectName[] servers = (ObjectName[]) connection.getAttribute(domain, "Servers");
		for (ObjectName server : servers) {
			String aName = (String) connection.getAttribute(server, "Name");
			try {
				ObjectName ser = new ObjectName("com.bea:Name=" + aName + ",Location=" + aName + ",Type=ServerRuntime");
				weblogic.health.HealthState serverHealthState = (weblogic.health.HealthState) connection
						.getAttribute(ser, "HealthState");
				int hState = serverHealthState.getState();
				if (hState == weblogic.health.HealthState.HEALTH_OK)
					System.out.println(aName + " : RUNNING");
				if (hState == weblogic.health.HealthState.HEALTH_WARN)
					System.out.println(aName + " : WARNING");
				if (hState == weblogic.health.HealthState.HEALTH_CRITICAL)
					System.out.println(aName + " : CRITICAL");
				if (hState == weblogic.health.HealthState.HEALTH_FAILED)
					System.out.println(aName + " : FAILED");
				if (hState == weblogic.health.HealthState.HEALTH_OVERLOADED)
					System.out.println(aName + " : OVERLOADED");
			} catch (javax.management.InstanceNotFoundException e) {
				System.out.println(aName + "t State: SHUTDOWN");
			}
		}
	}

	public void printClusterInfo() throws Exception {
		ObjectName[] serverRT = getServerRuntimes();
		for (ObjectName ser : serverRT) {
			ObjectName[] appRT = (ObjectName[]) connection.getAttribute(ser, "ApplicationRuntimes");
			for (ObjectName app : appRT) {
				String appName = (String) connection.getAttribute(app, "Name");
				if (appName.contains("_/")) {
					appName = appName.substring(appName.indexOf("_/") + 2);
				}
				weblogic.health.HealthState healthState = (weblogic.health.HealthState) connection.getAttribute(app,
						"HealthState");
				if (healthState.getState() == weblogic.health.HealthState.HEALTH_OK)
					System.out.println(appName + " : RUNNING");
				if (healthState.getState() == weblogic.health.HealthState.HEALTH_WARN)
					System.out.println(appName + " : WARNING");
				if (healthState.getState() == weblogic.health.HealthState.HEALTH_CRITICAL)
					System.out.println(appName + " : CRITICAL");
				if (healthState.getState() == weblogic.health.HealthState.HEALTH_FAILED)
					System.out.println(appName + " : FAILED");
				if (healthState.getState() == weblogic.health.HealthState.HEALTH_OVERLOADED)
					System.out.println(appName + " : OVERLOADED");
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Map<String,Component> map = components.getComponentsMap();
		for (String cid : map.keySet()) {
			System.out.println("#--------------" + map.get(cid)+ "-----------------#");
		}
	}
}