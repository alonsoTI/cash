/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate.util;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 * 
 * @author esilva
 */
public class HibernateUtil {
	private static final SessionFactory sessionFactory;
	private static final SessionFactory reportsSessionFactory;
	private static final SessionFactory seguridadSessionFactory;
	private static final String HCENTER_DB_CONFIG = "hibernate.cfg.xml";
	private static final String REPORTES_DB_CONFIG = "hibernate-reportes.cfg.xml";
	private static final String SEGURIDAD_DB_CONFIG = "hibernate-seguridad.cfg.xml";
	
	static Logger logger = Logger.getLogger(HibernateUtil.class);
	static {
		try {
			sessionFactory = new Configuration().configure(HCENTER_DB_CONFIG)
					.buildSessionFactory();
		} catch (Throwable ex) {
			logger.error("Fallo creacion de sessionfactory bd hcenter",ex);
			throw new ExceptionInInitializerError(ex);
		}
		try {
			reportsSessionFactory = new Configuration().configure(
					REPORTES_DB_CONFIG).buildSessionFactory();
		} catch (Throwable ex) {
			logger.error("Fallo creacion de sessionfactory bd consultas",ex);
			throw new ExceptionInInitializerError(ex);
		}
		
		try {
			seguridadSessionFactory = new Configuration().configure(SEGURIDAD_DB_CONFIG).buildSessionFactory();
		} catch (Throwable ex) {
			logger.error("Fallo creacion de sessionfactory bd consultas",ex);
			throw new ExceptionInInitializerError(ex);
		}

	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static SessionFactory getReportsSessionFactory() {
		return reportsSessionFactory;
	}
	
	public static SessionFactory getSeguridadSessionFactory() {
		return seguridadSessionFactory;
	}
}
