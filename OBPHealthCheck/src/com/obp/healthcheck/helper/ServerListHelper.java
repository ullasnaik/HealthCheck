package com.obp.healthcheck.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import com.obp.healthcheck.util.JschConnectionUtil;

public class ServerListHelper implements Callable<List<String>>{

	@Override
	public List<String> call() throws Exception {
		List<String> list = new ArrayList<String>();
		JschConnectionUtil jschConnectionUtil = new JschConnectionUtil();
		list = jschConnectionUtil.executeCommand("sh OBPHealthCheck.sh AllAppServerStatus.py");
		return list;
	}

}
