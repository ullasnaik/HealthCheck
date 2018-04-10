package com.obp.healthcheck.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

import com.obp.healthcheck.modal.Component;

public class MBeanServerConnectionUtil {
	private MBeanServerConnection connection;
	private JMXConnector connector;

	public MBeanServerConnection getConnection() {
		return connection;
	}

	public JMXConnector getConnector() {
		return connector;
	}

	public MBeanServerConnection initConnection(Component component) throws IOException, MalformedURLException {
		String protocol = "t3";
		Integer portInteger = Integer.valueOf(component.getPort());
		int port = portInteger.intValue();
		String jndiroot = "/jndi/";
		String mserver = "weblogic.management.mbeanservers.domainruntime";
		JMXServiceURL serviceURL = new JMXServiceURL(protocol, component.getUrl(), port, jndiroot + mserver);
		Hashtable<String, String> map = new Hashtable<String, String>();
		map.put(Context.SECURITY_PRINCIPAL, component.getUsername());
		map.put(Context.SECURITY_CREDENTIALS, component.getPassword());
		map.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
		connector = JMXConnectorFactory.connect(serviceURL, map);
		connection = connector.getMBeanServerConnection();
		return connection;
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			connector.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.finalize();
	}

}
