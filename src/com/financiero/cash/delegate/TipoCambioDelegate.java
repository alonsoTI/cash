package com.financiero.cash.delegate;

import com.hiper.cash.dao.TipoCambioDao;
import com.hiper.cash.dao.ws.ibs.TipoCambioDaoIbs;
import com.hiper.cash.xml.bean.BeanTipoCambio;

public class TipoCambioDelegate {
	private static TipoCambioDelegate instance;
	private TipoCambioDao tipoCambioDaoIbs = new TipoCambioDaoIbs();

	private TipoCambioDelegate() {
	}

	public static TipoCambioDelegate getInstance() {
		if (instance == null) {
			instance = new TipoCambioDelegate();
		}
		return instance;
	}

	public BeanTipoCambio obtenerTipoCambio() {
		return tipoCambioDaoIbs.obtenerTipoCambio();
	}

	public double obtenerTipoCambioCompra() {
		BeanTipoCambio beanTipoCambio = obtenerTipoCambio();
		double tipoCambio = 0.0;
		if (beanTipoCambio != null) {
			tipoCambio = beanTipoCambio.getPrecioCompra();
		}
		return tipoCambio;
	}

	public double obtenerTipoCambioVenta() {
		BeanTipoCambio beanTipoCambio = obtenerTipoCambio();
		double tipoCambio = 0.0;
		if (beanTipoCambio != null) {
			tipoCambio = beanTipoCambio.getPrecioVenta();
		}
		return tipoCambio;
	}
}
