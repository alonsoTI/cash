package com.financiero.cash.hilos;

import org.apache.log4j.Logger;

import com.hiper.cash.dao.TaOrdenDao;
import com.hiper.cash.dao.hibernate.TaOrdenDaoHibernate;

public class CashThreadCarga extends Thread {

	private static final Logger logger = Logger
			.getLogger(CashThreadCarga.class);

	long idEnvio = 0;
	long idOrden = 0;
	long idServEmp = 0;

	public CashThreadCarga(long pidEnvio, long pidOrden, long pidServEmp) {
		this.idEnvio = pidEnvio;
		this.idOrden = pidOrden;
		this.idServEmp = pidServEmp;
	}

	public void run() {
		// Aquí el código pesado que tarda mucho
		try {

			long startTime = System.currentTimeMillis();

			TaOrdenDao ordenDao = new TaOrdenDaoHibernate();

			ordenDao.ejecutaBuzonesID(idEnvio, idOrden, idServEmp);

			long endTime = System.currentTimeMillis();

			logger.info("Carga ejecutaBuzonesID  ,Tiempo de Ejecucion: "
					+ (endTime - startTime) / 1000 + " seg");

		} catch (Exception e) {

			logger.error("Error Hilo CashThreadCarga : " + e.getMessage());
			logger.error("----------------Datos enviados por el CashThreadTrx-------------");
			logger.error("idEnvio=" + idEnvio);
			logger.error("idOrden=" + idOrden);
			logger.error("idServEmp=" + idServEmp);
			logger.error("----------------Fin CashThreadTrx-------------");
			e.printStackTrace();

		}
	}

}
