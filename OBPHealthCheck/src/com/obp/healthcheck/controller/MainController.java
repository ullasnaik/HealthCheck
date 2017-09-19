package com.obp.healthcheck.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.obp.healthcheck.helper.AppListHelper;
import com.obp.healthcheck.helper.DataSorceListHelper;
import com.obp.healthcheck.helper.GenerateVoHelper;
import com.obp.healthcheck.helper.HeapTheradHelper;
import com.obp.healthcheck.helper.ServerListHelper;
import com.obp.healthcheck.util.AppServerConfigModal;

@Controller
public class MainController {
	GenerateVoHelper voHelper = new GenerateVoHelper();

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView getServerData() {
		ServerListHelper serverListHelper = new ServerListHelper();
		FutureTask<List<String>> slist = new FutureTask<List<String>>(serverListHelper);
		ExecutorService executor = Executors.newFixedThreadPool(3);
		executor.submit(slist);
		ModelAndView model = new ModelAndView("index");
		try {
			System.out.println("Cheinking Script completion");
			if (slist.isDone() && slist.isDone()) {
				System.out.println("Done");
				executor.shutdown();
			}
			List<String> slistout = slist.get();
			HashMap<String, ArrayList<ArrayList<String>>> res = voHelper.getConsoleAppServerData(slistout);
			model.addObject("servermap", res);

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed...");
		}

		return model;
	}

	@RequestMapping(value = "/DsStatus", method = RequestMethod.GET)
	public ModelAndView getDsStatData() {
		HeapTheradHelper heapTheradHelper = new HeapTheradHelper();
		DataSorceListHelper dataSorceListHelper = new DataSorceListHelper();
		FutureTask<List<String>> dlist = new FutureTask<List<String>>(dataSorceListHelper);
		FutureTask<List<String>> htlist = new FutureTask<List<String>>(heapTheradHelper);
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.submit(dlist);
		executor.submit(htlist);
		ModelAndView model = new ModelAndView("DsStatus");
		try {
			System.out.println("DsStatus Cheinking Script completion");
			if (dlist.isDone() && htlist.isDone()) {
				System.out.println("Done");
				executor.shutdown();
			}
			List<String> dlistout = dlist.get();
			List<String> htlistout = htlist.get();
			HashMap<String, ArrayList<ArrayList<String>>> dsres = voHelper.getConsoleListData(dlistout);
			HashMap<String, LinkedHashSet<ArrayList<String>>> htsres = voHelper.getConsoleHTListData(htlistout);
			model.addObject("DSList", dsres);
			model.addObject("HTList", htsres);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed...");
		}
		return model;
	}

}