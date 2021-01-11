package com.financiero.cash.delegate;


import static com.hiper.cash.util.Util.compareToFechaSistema;

import com.hiper.cash.dao.TaOrdenDao;
import com.hiper.cash.dao.TpDetalleOrdenDao;
import com.hiper.cash.dao.hibernate.TaOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TpDetalleOrdenDaoHibernate;

/**
 * @author ronvil
 * modificado por andy 09/07/2011
 */
public class OrdenDelegate {

	public static final String ENVIADO = "E";
	public static final String INGRESADO = "0";	
	
	TpDetalleOrdenDao detalleOrdenDAO = new TpDetalleOrdenDaoHibernate();
	TaOrdenDao ordenDAO = new TaOrdenDaoHibernate();

	public OrdenDelegate() {
		// TODO Auto-generated constructor stub
	}

	
	public boolean esFactibleEliminarOrden(String idOrden,String idServEmp) throws Exception {		
		/**
		 * SE OBTIENE LA FECHA DE INICIO DE EJECUCION DE LA ORDEN
		 * */
		String fecha = ordenDAO.obtenerFechaOrden(idOrden,idServEmp);			
		boolean esFactible = false;
		int valor = compareToFechaSistema(fecha);
		if ( valor > 0 ) {
			esFactible = true;
		} else {
			String respuesta = ordenDAO.validarCancelarOrden(idOrden,idServEmp);
			if(respuesta.equals("0")){
				esFactible = true;
			}else{
				esFactible = false;
			}
		}
		return esFactible;
	}




}
