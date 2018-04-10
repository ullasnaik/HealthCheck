package com.obp.healthcheck.modal;

import java.util.HashMap;
import java.util.Map;

public class Components {
	public String environmentName;
	public Map<String, Component> componentsMap = new HashMap<>();

	public Map<String, Component> getComponentsMap() {
		return componentsMap;
	}

	public void setComponentsMap(Map<String, Component> componentsMap) {
		this.componentsMap = componentsMap;
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

}
