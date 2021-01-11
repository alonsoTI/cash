package com.financiero.cash.util.spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConfigLoader {
	private static ConfigLoader configLoader;

	private BeanFactory factory;

	private ConfigLoader() {
		String[] configXmls = { "spring-cache-context.xml" };
		factory = new ClassPathXmlApplicationContext(configXmls);
	}

	/**
	 * Implementacion del singleton
	 * 
	 * @return Clase se instancia unica
	 */
	public static synchronized ConfigLoader getInstance() {

		if (configLoader == null) {
			configLoader = new ConfigLoader();
		}
		return configLoader;
	}

	/**
	 * Obtiene un bean del factory creado
	 * 
	 * @param name
	 * @return
	 */
	public Object getBean(String name) {
		return factory.getBean(name);
	}
}
