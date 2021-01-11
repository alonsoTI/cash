package com.hiper.cash.dao.ws.ibs;

import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.APP_CONSULTA_TIPO_CAMBIO;
import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.LONG_CONSULTA_TIPO_CAMBIO;
import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.TRX_CONSULTA_TIPO_CAMBIO;

import com.financiero.cash.ibs.util.Constantes;
import com.financiero.cash.ibs.util.IbsUtils;
import com.hiper.cash.dao.TipoCambioDao;
import com.hiper.cash.dao.ws.SixLinux;
import com.hiper.cash.xml.bean.BeanTipoCambio;

public class TipoCambioDaoIbs implements TipoCambioDao {
	private static final String MESSAGE_CONSULTA_TIPO_CAMBIO = "";
	private SixLinux sixLinux = SixLinux.getInstance();

	@Override
	public BeanTipoCambio obtenerTipoCambio() {
		String tramaRespuesta = sixLinux.enviarMensaje(APP_CONSULTA_TIPO_CAMBIO, TRX_CONSULTA_TIPO_CAMBIO,
				LONG_CONSULTA_TIPO_CAMBIO, MESSAGE_CONSULTA_TIPO_CAMBIO);
		String codigoRptaIBS = IbsUtils.obtenerCodigoRespuestaIBS(tramaRespuesta);
		BeanTipoCambio beanTipoCambio=null;
		if (IbsUtils.esRespuestaExitosa(codigoRptaIBS)) {			
			beanTipoCambio= new BeanTipoCambio();
			int posicionActualTrama = Constantes.POSICION_INICIO_DATOS_TRAMA;
			String campoTrama= tramaRespuesta.substring(++posicionActualTrama,
					posicionActualTrama += 13);			
			double precioCompra = IbsUtils.convertirDouble(campoTrama, 3);
			beanTipoCambio.setPrecioCompra(precioCompra);			
			campoTrama= tramaRespuesta.substring(++posicionActualTrama,
					posicionActualTrama += 13);
			double precioVenta = IbsUtils.convertirDouble(campoTrama, 3);
			beanTipoCambio.setPrecioVenta(precioVenta);			 
		}
		return beanTipoCambio;
	}
	
	

}
