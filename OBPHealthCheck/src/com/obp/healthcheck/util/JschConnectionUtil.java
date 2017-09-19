package com.obp.healthcheck.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class JschConnectionUtil {
	public ArrayList<String> executeCommand(String cmd) {
		AppServerConfigModal appServerConfig= AppServerConfigModal.getInstance();
		String host = appServerConfig.getHost();
		String user = appServerConfig.getUsername();
		String password = appServerConfig.getPassword();
		String command1 = cmd;
		ArrayList<String> response = new ArrayList<>();
		try {

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected to :" + host);

			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command1);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();
			channel.connect();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			boolean wrflg = false;
			String str;
			while (true) {
				while ((str = br.readLine()) != null) {
					if (str.equals("STARTCAPTURE")) {
						wrflg = true;
						continue;
					}
					if (str.equals("ENDCAPTURE")) {
						wrflg = false;
						continue;
					}
					if (wrflg) {
						response.add(str);
					}

				}
				if (channel.isClosed()) {
					System.out.println("exit-status: " + channel.getExitStatus());
					break;
				}
				try {
					Thread.sleep(5);
				} catch (Exception ee) {
				} finally {
					channel.disconnect();
					session.disconnect();
				}
			}
			channel.disconnect();
			session.disconnect();
			System.out.println("Exit from :" + host);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
