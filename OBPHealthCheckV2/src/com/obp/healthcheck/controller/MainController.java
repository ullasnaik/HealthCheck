package com.obp.healthcheck.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.obp.healthcheck.helper.ServerListHelper;
import com.obp.healthcheck.modal.Component;
import com.obp.healthcheck.modal.Components;
import com.obp.healthcheck.modal.ServerDetails;

@Controller
public class MainController {
	@Autowired
	private Components components;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView getServerData() {
		System.out.println("Entering controller....");
		ModelAndView model = new ModelAndView("index");
		HashMap<String, Component> map = (HashMap<String, Component>) components.getComponentsMap();
		ExecutorService executorService = Executors.newFixedThreadPool(map.keySet().size());
		List<Callable<ServerDetails>> callables = new ArrayList<Callable<ServerDetails>>();
		for (String cId : map.keySet()) {
			ServerListHelper stask = new ServerListHelper();
			stask.setComponent(map.get(cId));
			callables.add(stask);
		}
		List<ServerDetails> sListRes = new ArrayList<>();
		try {
			List<Future<ServerDetails>> futures = executorService.invokeAll(callables);
			for (Future<ServerDetails> future : futures) {
				sListRes.add(future.get());
				System.out.println(future.get().toString());
			}

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed...");
			executorService.shutdown();
		}
		model.addObject("ServerDetailsList", sListRes);
		model.addObject("environmentName", components.getEnvironmentName());
		return model;
	}

}